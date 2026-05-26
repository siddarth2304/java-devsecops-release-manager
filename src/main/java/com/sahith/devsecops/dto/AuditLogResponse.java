package com.sahith.devsecops.dto;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String action,
        String entityType,
        Long entityId,
        String performedBy,
        LocalDateTime timestamp,
        String details
) {
}
