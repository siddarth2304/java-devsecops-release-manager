package com.sahith.devsecops.service;

import com.sahith.devsecops.dto.CodeQualityReportRequest;
import com.sahith.devsecops.dto.CodeQualityReportResponse;
import com.sahith.devsecops.entity.CodeQualityReport;
import com.sahith.devsecops.entity.Project;
import com.sahith.devsecops.entity.QualityGateStatus;
import com.sahith.devsecops.exception.ResourceNotFoundException;
import com.sahith.devsecops.repository.CodeQualityReportRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CodeQualityReportService {

    private static final BigDecimal MINIMUM_COVERAGE = BigDecimal.valueOf(70);

    private final CodeQualityReportRepository reportRepository;
    private final ProjectService projectService;
    private final AuditLogService auditLogService;

    public CodeQualityReportService(
            CodeQualityReportRepository reportRepository,
            ProjectService projectService,
            AuditLogService auditLogService
    ) {
        this.reportRepository = reportRepository;
        this.projectService = projectService;
        this.auditLogService = auditLogService;
    }

    public CodeQualityReportResponse createReport(CodeQualityReportRequest request) {
        Project project = projectService.getProjectEntity(request.projectId());
        CodeQualityReport report = new CodeQualityReport();
        report.setProject(project);
        report.setBugsCount(request.bugsCount());
        report.setVulnerabilitiesCount(request.vulnerabilitiesCount());
        report.setCodeSmellsCount(request.codeSmellsCount());
        report.setCoveragePercentage(request.coveragePercentage());
        report.setQualityGateStatus(determineStatus(request.vulnerabilitiesCount(), request.coveragePercentage()));
        CodeQualityReport savedReport = reportRepository.save(report);
        auditLogService.logAction(
                "QUALITY_REPORT_CREATED",
                "QUALITY_REPORT",
                savedReport.getId(),
                project.getOwnerName(),
                "Created quality report for project id " + project.getId()
        );
        return toResponse(savedReport);
    }

    public List<CodeQualityReportResponse> getAllReports() {
        return reportRepository.findAll().stream().map(this::toResponse).toList();
    }

    public CodeQualityReportResponse getReportById(Long id) {
        return toResponse(getReportEntity(id));
    }

    public List<CodeQualityReportResponse> getReportsByProjectId(Long projectId) {
        return reportRepository.findByProjectId(projectId).stream().map(this::toResponse).toList();
    }

    public CodeQualityReport getReportEntity(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality report not found with id: " + id));
    }

    private QualityGateStatus determineStatus(int vulnerabilitiesCount, BigDecimal coveragePercentage) {
        if (vulnerabilitiesCount > 0 || coveragePercentage.compareTo(MINIMUM_COVERAGE) < 0) {
            return QualityGateStatus.FAILED;
        }
        return QualityGateStatus.PASSED;
    }

    private CodeQualityReportResponse toResponse(CodeQualityReport report) {
        return new CodeQualityReportResponse(
                report.getId(),
                report.getProject().getId(),
                report.getBugsCount(),
                report.getVulnerabilitiesCount(),
                report.getCodeSmellsCount(),
                report.getCoveragePercentage(),
                report.getQualityGateStatus(),
                report.getScannedAt()
        );
    }
}
