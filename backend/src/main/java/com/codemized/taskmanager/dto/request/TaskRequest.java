package com.codemized.taskmanager.dto.request;

import com.codemized.taskmanager.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
    @NotBlank @Size(min = 2, max = 200) String title,
    String description,
    TaskStatus status,
    Long assigneeId
) {}