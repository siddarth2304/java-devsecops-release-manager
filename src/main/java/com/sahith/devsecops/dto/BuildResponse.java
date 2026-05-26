package com.sahith.devsecops.dto;

import com.sahith.devsecops.entity.BuildStatus;
import java.time.LocalDateTime;

public record BuildResponse(
        Long id,
        Long projectId,
        String branchName,
        String commitHash,
        BuildStatus buildStatus,
        String triggeredBy,
        LocalDateTime startedAt,
        LocalDateTime completedAt
) {
}
