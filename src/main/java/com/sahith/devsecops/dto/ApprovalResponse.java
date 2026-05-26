package com.sahith.devsecops.dto;

import com.sahith.devsecops.entity.ApprovalStatus;
import java.time.LocalDateTime;

public record ApprovalResponse(
        Long id,
        Long deploymentId,
        String approverName,
        ApprovalStatus approvalStatus,
        String comments,
        LocalDateTime approvedAt
) {
}
