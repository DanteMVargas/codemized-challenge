package com.codemized.taskmanager.service.impl;

import com.codemized.taskmanager.dto.request.ProjectRequest;
import com.codemized.taskmanager.dto.response.ProjectResponse;
import com.codemized.taskmanager.entity.Project;
import com.codemized.taskmanager.entity.User;
import com.codemized.taskmanager.exception.ForbiddenException;
import com.codemized.taskmanager.exception.ResourceNotFoundException;
import com.codemized.taskmanager.repository.ProjectRepository;
import com.codemized.taskmanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public ProjectResponse create(ProjectRequest request, User owner) {
        Project project = Project.builder()
                .name(request.name())
                .description(request.description())
                .owner(owner)
                .build();
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> findByOwner(User owner) {
        return projectRepository.findByOwnerId(owner.getId())
                .stream().map(ProjectResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse findById(Long id, User requester) {
        Project project = getProjectOrThrow(id);
        checkOwnership(project, requester);
        return ProjectResponse.from(project);
    }

    @Override
    @Transactional
    public ProjectResponse update(Long id, ProjectRequest request, User requester) {
        Project project = getProjectOrThrow(id);
        checkOwnership(project, requester);
        project.setName(request.name());
        project.setDescription(request.description());
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void delete(Long id, User requester) {
        Project project = getProjectOrThrow(id);
        checkOwnership(project, requester);
        projectRepository.delete(project);
    }

    private Project getProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
    }

    private void checkOwnership(Project project, User requester) {
        if (!project.getOwner().getId().equals(requester.getId())) {
            throw new ForbiddenException("You are not the owner of this project");
        }
    }
}