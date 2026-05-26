package com.sahith.devsecops.dto;

import com.sahith.devsecops.entity.BuildStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BuildRequest(
        @NotNull(message = "Project ID is required")
        Long projectId,
        @NotBlank(message = "Branch name is required")
        String branchName,
        @NotBlank(message = "Commit hash is required")
        String commitHash,
        BuildStatus buildStatus,
        @NotBlank(message = "Triggered by is required")
        String triggeredBy
) {
}
