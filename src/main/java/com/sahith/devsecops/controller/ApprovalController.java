package com.sahith.devsecops.controller;

import com.sahith.devsecops.dto.ApprovalDecisionRequest;
import com.sahith.devsecops.dto.ApprovalRequest;
import com.sahith.devsecops.dto.ApprovalResponse;
import com.sahith.devsecops.service.ApprovalService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApprovalResponse createApproval(@Valid @RequestBody ApprovalRequest request) {
        return approvalService.createApproval(request);
    }

    @GetMapping
    public List<ApprovalResponse> getAllApprovals() {
        return approvalService.getAllApprovals();
    }

    @GetMapping("/{id}")
    public ApprovalResponse getApprovalById(@PathVariable Long id) {
        return approvalService.getApprovalById(id);
    }

    @GetMapping("/deployment/{deploymentId}")
    public List<ApprovalResponse> getApprovalsByDeploymentId(@PathVariable Long deploymentId) {
        return approvalService.getApprovalsByDeploymentId(deploymentId);
    }

    @PutMapping("/{id}/decision")
    public ApprovalResponse updateDecision(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalDecisionRequest request
    ) {
        return approvalService.updateDecision(id, request.approvalStatus(), request.comments());
    }
}
