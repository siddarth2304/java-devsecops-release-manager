package com.sahith.devsecops.dto;

import com.sahith.devsecops.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
        @NotBlank(message = "Project name is required")
        String name,
        @NotBlank(message = "Repository URL is required")
        String repositoryUrl,
        String technology,
        @NotBlank(message = "Owner name is required")
        String ownerName,
        ProjectStatus status
) {
}
