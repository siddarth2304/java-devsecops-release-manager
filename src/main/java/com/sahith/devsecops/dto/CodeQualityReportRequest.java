package com.sahith.devsecops.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CodeQualityReportRequest(
        @NotNull(message = "Project ID is required")
        Long projectId,
        int bugsCount,
        int vulnerabilitiesCount,
        int codeSmellsCount,
        @NotNull(message = "Coverage percentage is required")
        @DecimalMin(value = "0.0", message = "Coverage must be at least 0")
        @DecimalMax(value = "100.0", message = "Coverage cannot exceed 100")
        BigDecimal coveragePercentage
) {
}
