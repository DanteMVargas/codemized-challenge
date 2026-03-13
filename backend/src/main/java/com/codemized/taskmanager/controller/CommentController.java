package com.codemized.taskmanager.controller;

import com.codemized.taskmanager.dto.request.CommentRequest;
import com.codemized.taskmanager.dto.response.CommentResponse;
import com.codemized.taskmanager.entity.User;
import com.codemized.taskmanager.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@PathVariable Long taskId,
                                   @Valid @RequestBody CommentRequest request,
                                   @AuthenticationPrincipal User user) {
        return commentService.create(taskId, request, user);
    }

    @GetMapping
    public List<CommentResponse> getByTask(@PathVariable Long taskId,
                                            @AuthenticationPrincipal User user) {
        return commentService.findByTask(taskId, user);
    }

    @PutMapping("/{id}")
    public CommentResponse update(@PathVariable Long taskId,
                                   @PathVariable Long id,
                                   @Valid @RequestBody CommentRequest request,
                                   @AuthenticationPrincipal User user) {
        return commentService.update(id, request, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long taskId,
                        @PathVariable Long id,
                        @AuthenticationPrincipal User user) {
        commentService.delete(id, user);
    }
}