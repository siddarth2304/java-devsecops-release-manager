package com.sahith.devsecops.dto;

import com.sahith.devsecops.entity.ApprovalStatus;
import jakarta.validation.constraints.NotNull;

public record ApprovalDecisionRequest(
        @NotNull(message = "Approval status is required")
        ApprovalStatus approvalStatus,
        String comments
) {
}
