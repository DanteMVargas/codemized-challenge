package com.codemized.taskmanager.dto.response;

import com.codemized.taskmanager.entity.Project;
import java.time.LocalDateTime;

public record ProjectResponse(Long id, String name, String description,
                               UserResponse owner, LocalDateTime createdAt) {
    public static ProjectResponse from(Project p) {
        return new ProjectResponse(
            p.getId(), p.getName(), p.getDescription(),
            UserResponse.from(p.getOwner()), p.getCreatedAt()
        );
    }
}