package com.sahith.devsecops.service;

import com.sahith.devsecops.dto.ApprovalRequest;
import com.sahith.devsecops.dto.ApprovalResponse;
import com.sahith.devsecops.entity.Approval;
import com.sahith.devsecops.entity.ApprovalStatus;
import com.sahith.devsecops.entity.Deployment;
import com.sahith.devsecops.exception.BadRequestException;
import com.sahith.devsecops.exception.ResourceNotFoundException;
import com.sahith.devsecops.repository.ApprovalRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final DeploymentService deploymentService;
    private final AuditLogService auditLogService;

    public ApprovalService(
            ApprovalRepository approvalRepository,
            DeploymentService deploymentService,
            AuditLogService auditLogService
    ) {
        this.approvalRepository = approvalRepository;
        this.deploymentService = deploymentService;
        this.auditLogService = auditLogService;
    }

    public ApprovalResponse createApproval(ApprovalRequest request) {
        Deployment deployment = deploymentService.getDeploymentEntity(request.deploymentId());
        Approval approval = new Approval();
        approval.setDeployment(deployment);
        approval.setApproverName(request.approverName());
        approval.setComments(request.comments());
        approval.setApprovalStatus(ApprovalStatus.PENDING);
        Approval savedApproval = approvalRepository.save(approval);
        auditLogService.logAction(
                "APPROVAL_CREATED",
                "APPROVAL",
                savedApproval.getId(),
                savedApproval.getApproverName(),
                "Created approval for deployment id " + deployment.getId()
        );
        return toResponse(savedApproval);
    }

    public List<ApprovalResponse> getAllApprovals() {
        return approvalRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ApprovalResponse getApprovalById(Long id) {
        return toResponse(getApprovalEntity(id));
    }

    public List<ApprovalResponse> getApprovalsByDeploymentId(Long deploymentId) {
        return approvalRepository.findByDeploymentId(deploymentId).stream().map(this::toResponse).toList();
    }

    public ApprovalResponse updateDecision(Long id, ApprovalStatus status, String comments) {
        if (status == ApprovalStatus.PENDING) {
            throw new BadRequestException("Approval decision cannot be set back to PENDING");
        }
        Approval approval = getApprovalEntity(id);
        approval.setApprovalStatus(status);
        approval.setComments(comments);
        approval.setApprovedAt(LocalDateTime.now());
        Approval savedApproval = approvalRepository.save(approval);
        auditLogService.logAction(
                "APPROVAL_DECISION_UPDATED",
                "APPROVAL",
                savedApproval.getId(),
                savedApproval.getApproverName(),
                "Approval decision updated to " + status
        );
        return toResponse(savedApproval);
    }

    public Approval getApprovalEntity(Long id) {
        return approvalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Approval not found with id: " + id));
    }

    private ApprovalResponse toResponse(Approval approval) {
        return new ApprovalResponse(
                approval.getId(),
                approval.getDeployment().getId(),
                approval.getApproverName(),
                approval.getApprovalStatus(),
                approval.getComments(),
                approval.getApprovedAt()
        );
    }
}
