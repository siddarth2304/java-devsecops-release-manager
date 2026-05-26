package com.sahith.devsecops.service;

import com.sahith.devsecops.dto.DeploymentRequest;
import com.sahith.devsecops.dto.DeploymentResponse;
import com.sahith.devsecops.entity.ApprovalStatus;
import com.sahith.devsecops.entity.Build;
import com.sahith.devsecops.entity.BuildStatus;
import com.sahith.devsecops.entity.Deployment;
import com.sahith.devsecops.entity.DeploymentEnvironment;
import com.sahith.devsecops.entity.DeploymentStatus;
import com.sahith.devsecops.exception.BadRequestException;
import com.sahith.devsecops.exception.ResourceNotFoundException;
import com.sahith.devsecops.repository.ApprovalRepository;
import com.sahith.devsecops.repository.DeploymentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DeploymentService {

    private final DeploymentRepository deploymentRepository;
    private final BuildService buildService;
    private final ApprovalRepository approvalRepository;
    private final AuditLogService auditLogService;

    public DeploymentService(
            DeploymentRepository deploymentRepository,
            BuildService buildService,
            ApprovalRepository approvalRepository,
            AuditLogService auditLogService
    ) {
        this.deploymentRepository = deploymentRepository;
        this.buildService = buildService;
        this.approvalRepository = approvalRepository;
        this.auditLogService = auditLogService;
    }

    public DeploymentResponse createDeployment(DeploymentRequest request) {
        Build build = buildService.getBuildEntity(request.buildId());
        if (build.getBuildStatus() != BuildStatus.SUCCESS) {
            throw new BadRequestException("Deployment is only allowed for successful builds");
        }

        Deployment deployment = new Deployment();
        deployment.setBuild(build);
        deployment.setEnvironment(request.environment());
        deployment.setDeploymentStatus(request.deploymentStatus() == null
                ? DeploymentStatus.PENDING : request.deploymentStatus());
        deployment.setDeployedBy(request.deployedBy());
        deployment.setRollbackRequired(request.rollbackRequired());
        deployment.setRollbackReason(request.rollbackReason());
        validateProdApproval(deployment.getEnvironment(), deployment.getDeploymentStatus(), null);

        Deployment savedDeployment = deploymentRepository.save(deployment);
        auditLogService.logAction(
                "DEPLOYMENT_CREATED",
                "DEPLOYMENT",
                savedDeployment.getId(),
                savedDeployment.getDeployedBy(),
                "Created deployment for build id " + build.getId()
        );
        return toResponse(savedDeployment);
    }

    public List<DeploymentResponse> getAllDeployments() {
        return deploymentRepository.findAll().stream().map(this::toResponse).toList();
    }

    public DeploymentResponse getDeploymentById(Long id) {
        return toResponse(getDeploymentEntity(id));
    }

    public List<DeploymentResponse> getDeploymentsByBuildId(Long buildId) {
        return deploymentRepository.findByBuildId(buildId).stream().map(this::toResponse).toList();
    }

    public DeploymentResponse updateDeploymentStatus(Long id, DeploymentStatus status) {
        Deployment deployment = getDeploymentEntity(id);
        validateProdApproval(deployment.getEnvironment(), status, deployment.getId());
        deployment.setDeploymentStatus(status);
        Deployment savedDeployment = deploymentRepository.save(deployment);
        auditLogService.logAction(
                "DEPLOYMENT_STATUS_UPDATED",
                "DEPLOYMENT",
                savedDeployment.getId(),
                savedDeployment.getDeployedBy(),
                "Deployment status updated to " + status
        );
        return toResponse(savedDeployment);
    }

    public DeploymentResponse rollbackDeployment(Long id, String rollbackReason) {
        Deployment deployment = getDeploymentEntity(id);
        if (deployment.getDeploymentStatus() != DeploymentStatus.FAILED) {
            throw new BadRequestException("Only failed deployments can be rolled back");
        }
        deployment.setDeploymentStatus(DeploymentStatus.ROLLED_BACK);
        deployment.setRollbackRequired(true);
        deployment.setRollbackReason(rollbackReason);
        Deployment savedDeployment = deploymentRepository.save(deployment);
        auditLogService.logAction(
                "ROLLBACK_EXECUTED",
                "DEPLOYMENT",
                savedDeployment.getId(),
                savedDeployment.getDeployedBy(),
                "Rollback executed with reason: " + rollbackReason
        );
        return toResponse(savedDeployment);
    }

    public Deployment getDeploymentEntity(Long id) {
        return deploymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deployment not found with id: " + id));
    }

    private void validateProdApproval(
            DeploymentEnvironment environment,
            DeploymentStatus status,
            Long deploymentId
    ) {
        if (environment == DeploymentEnvironment.PROD && status == DeploymentStatus.DEPLOYED) {
            boolean approved = deploymentId != null
                    && approvalRepository.findFirstByDeploymentIdAndApprovalStatus(
                    deploymentId, ApprovalStatus.APPROVED).isPresent();
            if (!approved) {
                throw new BadRequestException("PROD deployments require an approved approval decision");
            }
        }
    }

    private DeploymentResponse toResponse(Deployment deployment) {
        return new DeploymentResponse(
                deployment.getId(),
                deployment.getBuild().getId(),
                deployment.getEnvironment(),
                deployment.getDeploymentStatus(),
                deployment.getDeployedBy(),
                deployment.getDeployedAt(),
                deployment.isRollbackRequired(),
                deployment.getRollbackReason()
        );
    }
}
