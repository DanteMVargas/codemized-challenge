package com.codemized.taskmanager.service;

import com.codemized.taskmanager.dto.request.ProjectRequest;
import com.codemized.taskmanager.dto.response.ProjectResponse;
import com.codemized.taskmanager.entity.User;

import java.util.List;

public interface ProjectService {
    ProjectResponse create(ProjectRequest request, User owner);
    List<ProjectResponse> findByOwner(User owner);
    ProjectResponse findById(Long id, User requester);
    ProjectResponse update(Long id, ProjectRequest request, User requester);
    void delete(Long id, User requester);
}