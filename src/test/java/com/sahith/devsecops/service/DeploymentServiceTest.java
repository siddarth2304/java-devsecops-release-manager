package com.sahith.devsecops.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sahith.devsecops.dto.DeploymentRequest;
import com.sahith.devsecops.dto.DeploymentResponse;
import com.sahith.devsecops.entity.ApprovalStatus;
import com.sahith.devsecops.entity.Build;
import com.sahith.devsecops.entity.BuildStatus;
import com.sahith.devsecops.entity.Deployment;
import com.sahith.devsecops.entity.DeploymentEnvironment;
import com.sahith.devsecops.entity.DeploymentStatus;
import com.sahith.devsecops.exception.BadRequestException;
import com.sahith.devsecops.repository.ApprovalRepository;
import com.sahith.devsecops.repository.DeploymentRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeploymentServiceTest {

    @Mock
    private DeploymentRepository deploymentRepository;

    @Mock
    private BuildService buildService;

    @Mock
    private ApprovalRepository approvalRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private DeploymentService deploymentService;

    @Test
    void deploymentCannotBeCreatedForFailedBuild() {
        Build build = new Build();
        build.setId(1L);
        build.setBuildStatus(BuildStatus.FAILED);

        when(buildService.getBuildEntity(1L)).thenReturn(build);

        DeploymentRequest request = new DeploymentRequest(
                1L,
                DeploymentEnvironment.QA,
                null,
                "sahith",
                false,
                null
        );

        assertThrows(BadRequestException.class, () -> deploymentService.createDeployment(request));
    }

    @Test
    void deploymentCanBeCreatedForSuccessfulBuild() {
        Build build = new Build();
        build.setId(2L);
        build.setBuildStatus(BuildStatus.SUCCESS);

        Deployment deployment = new Deployment();
        deployment.setId(3L);
        deployment.setBuild(build);
        deployment.setEnvironment(DeploymentEnvironment.QA);
        deployment.setDeploymentStatus(DeploymentStatus.PENDING);
        deployment.setDeployedBy("sahith");
        deployment.setDeployedAt(LocalDateTime.now());

        when(buildService.getBuildEntity(2L)).thenReturn(build);
        when(deploymentRepository.save(any(Deployment.class))).thenReturn(deployment);

        DeploymentResponse response = deploymentService.createDeployment(
                new DeploymentRequest(2L, DeploymentEnvironment.QA, null, "sahith", false, null)
        );

        assertEquals(DeploymentStatus.PENDING, response.deploymentStatus());
    }

    @Test
    void rollbackUpdatesDeploymentStatusToRolledBack() {
        Build build = new Build();
        build.setId(2L);

        Deployment deployment = new Deployment();
        deployment.setId(4L);
        deployment.setBuild(build);
        deployment.setEnvironment(DeploymentEnvironment.QA);
        deployment.setDeploymentStatus(DeploymentStatus.FAILED);
        deployment.setDeployedBy("sahith");

        when(deploymentRepository.findById(4L)).thenReturn(Optional.of(deployment));
        when(deploymentRepository.save(any(Deployment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeploymentResponse response = deploymentService.rollbackDeployment(4L, "Smoke test failure");

        assertEquals(DeploymentStatus.ROLLED_BACK, response.deploymentStatus());
        assertEquals("Smoke test failure", response.rollbackReason());
    }

    @Test
    void prodDeploymentRequiresApproval() {
        Build build = new Build();
        build.setId(5L);
        build.setBuildStatus(BuildStatus.SUCCESS);

        Deployment deployment = new Deployment();
        deployment.setId(6L);
        deployment.setBuild(build);
        deployment.setEnvironment(DeploymentEnvironment.PROD);
        deployment.setDeploymentStatus(DeploymentStatus.PENDING);
        deployment.setDeployedBy("sahith");

        when(deploymentRepository.findById(6L)).thenReturn(Optional.of(deployment));
        when(approvalRepository.findFirstByDeploymentIdAndApprovalStatus(6L, ApprovalStatus.APPROVED))
                .thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> deploymentService.updateDeploymentStatus(6L, DeploymentStatus.DEPLOYED));
    }
}
