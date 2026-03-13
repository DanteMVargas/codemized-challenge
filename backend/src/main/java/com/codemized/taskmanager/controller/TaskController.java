package com.codemized.taskmanager.controller;

import com.codemized.taskmanager.dto.request.TaskRequest;
import com.codemized.taskmanager.dto.response.TaskResponse;
import com.codemized.taskmanager.entity.User;
import com.codemized.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/projects/{projectId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@PathVariable Long projectId,
                                @Valid @RequestBody TaskRequest request,
                                @AuthenticationPrincipal User user) {
        return taskService.create(projectId, request, user);
    }

    @GetMapping("/projects/{projectId}/tasks")
    public List<TaskResponse> getByProject(@PathVariable Long projectId,
                                            @AuthenticationPrincipal User user) {
        return taskService.findByProject(projectId, user);
    }

    @GetMapping("/tasks/{id}")
    public TaskResponse getById(@PathVariable Long id,
                                 @AuthenticationPrincipal User user) {
        return taskService.findById(id, user);
    }

    @PutMapping("/tasks/{id}")
    public TaskResponse update(@PathVariable Long id,
                                @Valid @RequestBody TaskRequest request,
                                @AuthenticationPrincipal User user) {
        return taskService.update(id, request, user);
    }

    @PatchMapping("/tasks/{id}/assign/{assigneeId}")
    public TaskResponse assign(@PathVariable Long id,
                                @PathVariable Long assigneeId,
                                @AuthenticationPrincipal User user) {
        return taskService.assign(id, assigneeId, user);
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                        @AuthenticationPrincipal User user) {
        taskService.delete(id, user);
    }
}