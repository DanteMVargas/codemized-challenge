package com.codemized.taskmanager.service;

import com.codemized.taskmanager.dto.request.CommentRequest;
import com.codemized.taskmanager.dto.response.CommentResponse;
import com.codemized.taskmanager.entity.User;

import java.util.List;

public interface CommentService {
    CommentResponse create(Long taskId, CommentRequest request, User requester);
    List<CommentResponse> findByTask(Long taskId, User requester);
    CommentResponse update(Long id, CommentRequest request, User requester);
    void delete(Long id, User requester);
}