package com.sahith.devsecops.dto;

import com.sahith.devsecops.entity.QualityGateStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CodeQualityReportResponse(
        Long id,
        Long projectId,
        int bugsCount,
        int vulnerabilitiesCount,
        int codeSmellsCount,
        BigDecimal coveragePercentage,
        QualityGateStatus qualityGateStatus,
        LocalDateTime scannedAt
) {
}
