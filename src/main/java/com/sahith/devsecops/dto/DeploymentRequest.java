package com.sahith.devsecops.dto;

import com.sahith.devsecops.entity.DeploymentEnvironment;
import com.sahith.devsecops.entity.DeploymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeploymentRequest(
        @NotNull(message = "Build ID is required")
        Long buildId,
        @NotNull(message = "Environment is required")
        DeploymentEnvironment environment,
        DeploymentStatus deploymentStatus,
        @NotBlank(message = "Deployed by is required")
        String deployedBy,
        boolean rollbackRequired,
        String rollbackReason
) {
}
