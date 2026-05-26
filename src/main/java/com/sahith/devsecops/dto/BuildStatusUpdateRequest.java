package com.sahith.devsecops.dto;

import com.sahith.devsecops.entity.BuildStatus;
import jakarta.validation.constraints.NotNull;

public record BuildStatusUpdateRequest(
        @NotNull(message = "Build status is required")
        BuildStatus buildStatus
) {
}
