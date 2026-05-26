package com.sahith.devsecops.dto;

import com.sahith.devsecops.entity.ProjectStatus;
import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        String name,
        String repositoryUrl,
        String technology,
        String ownerName,
        ProjectStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
