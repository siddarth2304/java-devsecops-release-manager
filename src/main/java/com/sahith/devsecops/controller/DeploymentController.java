package com.sahith.devsecops.controller;

import com.sahith.devsecops.dto.DeploymentRequest;
import com.sahith.devsecops.dto.DeploymentResponse;
import com.sahith.devsecops.dto.DeploymentStatusUpdateRequest;
import com.sahith.devsecops.dto.RollbackRequest;
import com.sahith.devsecops.service.DeploymentService;
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
@RequestMapping("/api/deployments")
public class DeploymentController {

    private final DeploymentService deploymentService;

    public DeploymentController(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeploymentResponse createDeployment(@Valid @RequestBody DeploymentRequest request) {
        return deploymentService.createDeployment(request);
    }

    @GetMapping
    public List<DeploymentResponse> getAllDeployments() {
        return deploymentService.getAllDeployments();
    }

    @GetMapping("/{id}")
    public DeploymentResponse getDeploymentById(@PathVariable Long id) {
        return deploymentService.getDeploymentById(id);
    }

    @GetMapping("/build/{buildId}")
    public List<DeploymentResponse> getDeploymentsByBuildId(@PathVariable Long buildId) {
        return deploymentService.getDeploymentsByBuildId(buildId);
    }

    @PutMapping("/{id}/status")
    public DeploymentResponse updateDeploymentStatus(
            @PathVariable Long id,
            @Valid @RequestBody DeploymentStatusUpdateRequest request
    ) {
        return deploymentService.updateDeploymentStatus(id, request.deploymentStatus());
    }

    @PutMapping("/{id}/rollback")
    public DeploymentResponse rollbackDeployment(@PathVariable Long id, @Valid @RequestBody RollbackRequest request) {
        return deploymentService.rollbackDeployment(id, request.rollbackReason());
    }
}
