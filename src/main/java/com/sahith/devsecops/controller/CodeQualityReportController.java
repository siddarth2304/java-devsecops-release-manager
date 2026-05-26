package com.sahith.devsecops.controller;

import com.sahith.devsecops.dto.CodeQualityReportRequest;
import com.sahith.devsecops.dto.CodeQualityReportResponse;
import com.sahith.devsecops.service.CodeQualityReportService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quality-reports")
public class CodeQualityReportController {

    private final CodeQualityReportService reportService;

    public CodeQualityReportController(CodeQualityReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CodeQualityReportResponse createReport(@Valid @RequestBody CodeQualityReportRequest request) {
        return reportService.createReport(request);
    }

    @GetMapping
    public List<CodeQualityReportResponse> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public CodeQualityReportResponse getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @GetMapping("/project/{projectId}")
    public List<CodeQualityReportResponse> getReportsByProjectId(@PathVariable Long projectId) {
        return reportService.getReportsByProjectId(projectId);
    }
}
