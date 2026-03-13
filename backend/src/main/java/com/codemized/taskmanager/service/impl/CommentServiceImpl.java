package com.codemized.taskmanager.service.impl;

import com.codemized.taskmanager.dto.request.CommentRequest;
import com.codemized.taskmanager.dto.response.CommentResponse;
import com.codemized.taskmanager.entity.Comment;
import com.codemized.taskmanager.entity.Task;
import com.codemized.taskmanager.entity.User;
import com.codemized.taskmanager.exception.ForbiddenException;
import com.codemized.taskmanager.exception.ResourceNotFoundException;
import com.codemized.taskmanager.repository.CommentRepository;
import com.codemized.taskmanager.repository.TaskRepository;
import com.codemized.taskmanager.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public CommentResponse create(Long taskId, CommentRequest request, User requester) {
        Task task = getTaskOrThrow(taskId);
        Comment comment = Comment.builder()
                .content(request.content())
                .task(task)
                .author(requester)
                .build();
        return CommentResponse.from(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> findByTask(Long taskId, User requester) {
        getTaskOrThrow(taskId);
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId)
                .stream().map(CommentResponse::from).toList();
    }

    @Override
    @Transactional
    public CommentResponse update(Long id, CommentRequest request, User requester) {
        Comment comment = getCommentOrThrow(id);
        if (!comment.getAuthor().getId().equals(requester.getId())) {
            throw new ForbiddenException("You can only edit your own comments");
        }
        comment.setContent(request.content());
        return CommentResponse.from(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void delete(Long id, User requester) {
        Comment comment = getCommentOrThrow(id);
        if (!comment.getAuthor().getId().equals(requester.getId())) {
            throw new ForbiddenException("You can only delete your own comments");
        }
        commentRepository.delete(comment);
    }

    private Task getTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    }

    private Comment getCommentOrThrow(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found: " + id));
    }
}