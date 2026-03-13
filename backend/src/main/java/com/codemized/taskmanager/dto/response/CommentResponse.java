package com.codemized.taskmanager.dto.response;

import com.codemized.taskmanager.entity.Comment;
import java.time.LocalDateTime;

public record CommentResponse(Long id, String content, UserResponse author,
                               Long taskId, LocalDateTime createdAt) {
    public static CommentResponse from(Comment c) {
        return new CommentResponse(
            c.getId(), c.getContent(),
            UserResponse.from(c.getAuthor()),
            c.getTask().getId(), c.getCreatedAt()
        );
    }
}