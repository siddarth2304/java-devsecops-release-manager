package com.sahith.devsecops.dto;

import com.sahith.devsecops.entity.DeploymentStatus;
import jakarta.validation.constraints.NotNull;

public record DeploymentStatusUpdateRequest(
        @NotNull(message = "Deployment status is required")
        DeploymentStatus deploymentStatus
) {
}
