package com.sahith.devsecops.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApprovalRequest(
        @NotNull(message = "Deployment ID is required")
        Long deploymentId,
        @NotBlank(message = "Approver name is required")
        String approverName,
        String comments
) {
}
