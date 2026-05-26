package com.sahith.devsecops.controller;

import com.sahith.devsecops.dto.AuditLogResponse;
import com.sahith.devsecops.service.AuditLogService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public List<AuditLogResponse> getAllAuditLogs() {
        return auditLogService.getAllAuditLogs();
    }

    @GetMapping("/{id}")
    public AuditLogResponse getAuditLogById(@PathVariable Long id) {
        return auditLogService.getAuditLogById(id);
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public List<AuditLogResponse> getAuditLogsByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId
    ) {
        return auditLogService.getAuditLogsByEntity(entityType, entityId);
    }
}
