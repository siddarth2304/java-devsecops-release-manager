package com.sahith.devsecops.dto;

import jakarta.validation.constraints.NotBlank;

public record RollbackRequest(
        @NotBlank(message = "Rollback reason is required")
        String rollbackReason
) {
}
