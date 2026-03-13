package com.codemized.taskmanager.controller;

import com.codemized.taskmanager.dto.request.ProjectRequest;
import com.codemized.taskmanager.dto.response.ProjectResponse;
import com.codemized.taskmanager.entity.User;
import com.codemized.taskmanager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@Valid @RequestBody ProjectRequest request,
                                   @AuthenticationPrincipal User user) {
        return projectService.create(request, user);
    }

    @GetMapping
    public List<ProjectResponse> getMyProjects(@AuthenticationPrincipal User user) {
        return projectService.findByOwner(user);
    }

    @GetMapping("/{id}")
    public ProjectResponse getById(@PathVariable Long id,
                                    @AuthenticationPrincipal User user) {
        return projectService.findById(id, user);
    }

    @PutMapping("/{id}")
    public ProjectResponse update(@PathVariable Long id,
                                   @Valid @RequestBody ProjectRequest request,
                                   @AuthenticationPrincipal User user) {
        return projectService.update(id, request, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                        @AuthenticationPrincipal User user) {
        projectService.delete(id, user);
    }
}