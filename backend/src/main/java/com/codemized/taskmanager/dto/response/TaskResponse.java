package com.codemized.taskmanager.dto.response;

import com.codemized.taskmanager.entity.Task;
import com.codemized.taskmanager.entity.TaskStatus;
import java.time.LocalDateTime;

public record TaskResponse(Long id, String title, String description,
                            TaskStatus status, Long projectId,
                            UserResponse assignee, LocalDateTime createdAt) {
    public static TaskResponse from(Task t) {
        return new TaskResponse(
            t.getId(), t.getTitle(), t.getDescription(), t.getStatus(),
            t.getProject().getId(),
            t.getAssignee() != null ? UserResponse.from(t.getAssignee()) : null,
            t.getCreatedAt()
        );
    }
}