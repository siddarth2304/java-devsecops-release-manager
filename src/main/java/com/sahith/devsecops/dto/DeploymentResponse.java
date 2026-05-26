package com.sahith.devsecops.dto;

import com.sahith.devsecops.entity.DeploymentEnvironment;
import com.sahith.devsecops.entity.DeploymentStatus;
import java.time.LocalDateTime;

public record DeploymentResponse(
        Long id,
        Long buildId,
        DeploymentEnvironment environment,
        DeploymentStatus deploymentStatus,
        String deployedBy,
        LocalDateTime deployedAt,
        boolean rollbackRequired,
        String rollbackReason
) {
}
