package com.sahith.devsecops.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sahith.devsecops.dto.ApprovalRequest;
import com.sahith.devsecops.dto.ApprovalResponse;
import com.sahith.devsecops.entity.Approval;
import com.sahith.devsecops.entity.ApprovalStatus;
import com.sahith.devsecops.entity.Build;
import com.sahith.devsecops.entity.Deployment;
import com.sahith.devsecops.entity.DeploymentEnvironment;
import com.sahith.devsecops.entity.DeploymentStatus;
import com.sahith.devsecops.repository.ApprovalRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApprovalServiceTest {

    @Mock
    private ApprovalRepository approvalRepository;

    @Mock
    private DeploymentService deploymentService;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private ApprovalService approvalService;

    @Test
    void approvalDecisionUpdatesApprovalStatus() {
        Deployment deployment = createDeployment();

        Approval approval = new Approval();
        approval.setId(8L);
        approval.setDeployment(deployment);
        approval.setApproverName("reviewer");
        approval.setApprovalStatus(ApprovalStatus.PENDING);

        when(approvalRepository.findById(8L)).thenReturn(Optional.of(approval));
        when(approvalRepository.save(any(Approval.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApprovalResponse response = approvalService.updateDecision(8L, ApprovalStatus.APPROVED, "Looks good");

        assertEquals(ApprovalStatus.APPROVED, response.approvalStatus());
        assertNotNull(response.approvedAt());
    }

    @Test
    void createApprovalDefaultsToPending() {
        Deployment deployment = createDeployment();

        Approval approval = new Approval();
        approval.setId(7L);
        approval.setDeployment(deployment);
        approval.setApproverName("reviewer");
        approval.setApprovalStatus(ApprovalStatus.PENDING);

        when(deploymentService.getDeploymentEntity(6L)).thenReturn(deployment);
        when(approvalRepository.save(any(Approval.class))).thenReturn(approval);

        ApprovalResponse response = approvalService.createApproval(new ApprovalRequest(6L, "reviewer", "Initial"));

        assertEquals(ApprovalStatus.PENDING, response.approvalStatus());
    }

    private Deployment createDeployment() {
        Build build = new Build();
        build.setId(2L);

        Deployment deployment = new Deployment();
        deployment.setId(6L);
        deployment.setBuild(build);
        deployment.setEnvironment(DeploymentEnvironment.PROD);
        deployment.setDeploymentStatus(DeploymentStatus.PENDING);
        deployment.setDeployedBy("sahith");
        return deployment;
    }
}
