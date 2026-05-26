package com.sahith.devsecops.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sahith.devsecops.dto.CodeQualityReportRequest;
import com.sahith.devsecops.dto.CodeQualityReportResponse;
import com.sahith.devsecops.entity.CodeQualityReport;
import com.sahith.devsecops.entity.Project;
import com.sahith.devsecops.entity.QualityGateStatus;
import com.sahith.devsecops.repository.CodeQualityReportRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CodeQualityReportServiceTest {

    @Mock
    private CodeQualityReportRepository reportRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private CodeQualityReportService reportService;

    @Test
    void qualityGatePassesWhenVulnerabilitiesAreZeroAndCoverageAtLeastSeventy() {
        CodeQualityReportResponse response = createReportResponse(0, new BigDecimal("85.00"));
        assertEquals(QualityGateStatus.PASSED, response.qualityGateStatus());
    }

    @Test
    void qualityGateFailsWhenVulnerabilitiesGreaterThanZero() {
        CodeQualityReportResponse response = createReportResponse(2, new BigDecimal("90.00"));
        assertEquals(QualityGateStatus.FAILED, response.qualityGateStatus());
    }

    @Test
    void qualityGateFailsWhenCoverageBelowSeventy() {
        CodeQualityReportResponse response = createReportResponse(0, new BigDecimal("65.50"));
        assertEquals(QualityGateStatus.FAILED, response.qualityGateStatus());
    }

    private CodeQualityReportResponse createReportResponse(int vulnerabilities, BigDecimal coverage) {
        Project project = new Project();
        project.setId(11L);
        project.setOwnerName("sahith");

        CodeQualityReport report = new CodeQualityReport();
        report.setId(100L);
        report.setProject(project);
        report.setBugsCount(1);
        report.setVulnerabilitiesCount(vulnerabilities);
        report.setCodeSmellsCount(3);
        report.setCoveragePercentage(coverage);
        report.setQualityGateStatus(
                vulnerabilities > 0 || coverage.compareTo(BigDecimal.valueOf(70)) < 0
                        ? QualityGateStatus.FAILED : QualityGateStatus.PASSED
        );
        report.setScannedAt(LocalDateTime.now());

        when(projectService.getProjectEntity(11L)).thenReturn(project);
        when(reportRepository.save(any(CodeQualityReport.class))).thenReturn(report);

        return reportService.createReport(new CodeQualityReportRequest(11L, 1, vulnerabilities, 3, coverage));
    }
}
