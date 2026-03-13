package com.codemized.taskmanager.service.impl;

import com.codemized.taskmanager.dto.request.TaskRequest;
import com.codemized.taskmanager.dto.response.TaskResponse;
import com.codemized.taskmanager.entity.*;
import com.codemized.taskmanager.exception.ForbiddenException;
import com.codemized.taskmanager.exception.ResourceNotFoundException;
import com.codemized.taskmanager.repository.ProjectRepository;
import com.codemized.taskmanager.repository.TaskRepository;
import com.codemized.taskmanager.repository.UserRepository;
import com.codemized.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TaskResponse create(Long projectId, TaskRequest request, User requester) {
        Project project = getProjectOrThrow(projectId);
        checkOwnership(project, requester);

        User assignee = null;
        if (request.assigneeId() != null) {
            assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
        }

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status() != null ? request.status() : TaskStatus.TODO)
                .project(project)
                .assignee(assignee)
                .build();
        return TaskResponse.from(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> findByProject(Long projectId, User requester) {
        Project project = getProjectOrThrow(projectId);
        checkOwnership(project, requester);
        return taskRepository.findByProjectId(projectId)
                .stream().map(TaskResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse findById(Long id, User requester) {
        Task task = getTaskOrThrow(id);
        checkOwnership(task.getProject(), requester);
        return TaskResponse.from(task);
    }

    @Override
    @Transactional
    public TaskResponse update(Long id, TaskRequest request, User requester) {
        Task task = getTaskOrThrow(id);
        checkOwnership(task.getProject(), requester);
        task.setTitle(request.title());
        task.setDescription(request.description());
        if (request.status() != null) task.setStatus(request.status());
        if (request.assigneeId() != null) {
            User assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }
        return TaskResponse.from(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponse assign(Long taskId, Long assigneeId, User requester) {
        Task task = getTaskOrThrow(taskId);
        checkOwnership(task.getProject(), requester);
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + assigneeId));
        task.setAssignee(assignee);
        return TaskResponse.from(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void delete(Long id, User requester) {
        Task task = getTaskOrThrow(id);
        checkOwnership(task.getProject(), requester);
        taskRepository.delete(task);
    }

    private Project getProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
    }

    private Task getTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    }

    private void checkOwnership(Project project, User requester) {
        if (!project.getOwner().getId().equals(requester.getId())) {
            throw new ForbiddenException("You don't have access to this project");
        }
    }
}