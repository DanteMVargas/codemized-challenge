package com.codemized.taskmanager.service;

import com.codemized.taskmanager.dto.request.TaskRequest;
import com.codemized.taskmanager.dto.response.TaskResponse;
import com.codemized.taskmanager.entity.User;

import java.util.List;

public interface TaskService {
    TaskResponse create(Long projectId, TaskRequest request, User requester);
    List<TaskResponse> findByProject(Long projectId, User requester);
    TaskResponse findById(Long id, User requester);
    TaskResponse update(Long id, TaskRequest request, User requester);
    TaskResponse assign(Long taskId, Long assigneeId, User requester);
    void delete(Long id, User requester);
}