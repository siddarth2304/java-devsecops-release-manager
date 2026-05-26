package com.sahith.devsecops.service;

import com.sahith.devsecops.dto.BuildRequest;
import com.sahith.devsecops.dto.BuildResponse;
import com.sahith.devsecops.entity.Build;
import com.sahith.devsecops.entity.BuildStatus;
import com.sahith.devsecops.entity.Project;
import com.sahith.devsecops.exception.ResourceNotFoundException;
import com.sahith.devsecops.repository.BuildRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BuildService {

    private final BuildRepository buildRepository;
    private final ProjectService projectService;
    private final AuditLogService auditLogService;

    public BuildService(
            BuildRepository buildRepository,
            ProjectService projectService,
            AuditLogService auditLogService
    ) {
        this.buildRepository = buildRepository;
        this.projectService = projectService;
        this.auditLogService = auditLogService;
    }

    public BuildResponse createBuild(BuildRequest request) {
        Project project = projectService.getProjectEntity(request.projectId());
        Build build = new Build();
        build.setProject(project);
        build.setBranchName(request.branchName());
        build.setCommitHash(request.commitHash());
        build.setBuildStatus(request.buildStatus() == null ? BuildStatus.QUEUED : request.buildStatus());
        build.setTriggeredBy(request.triggeredBy());
        Build savedBuild = buildRepository.save(build);
        auditLogService.logAction(
                "BUILD_CREATED",
                "BUILD",
                savedBuild.getId(),
                savedBuild.getTriggeredBy(),
                "Created build for project id " + project.getId()
        );
        return toResponse(savedBuild);
    }

    public List<BuildResponse> getAllBuilds() {
        return buildRepository.findAll().stream().map(this::toResponse).toList();
    }

    public BuildResponse getBuildById(Long id) {
        return toResponse(getBuildEntity(id));
    }

    public List<BuildResponse> getBuildsByProjectId(Long projectId) {
        return buildRepository.findByProjectId(projectId).stream().map(this::toResponse).toList();
    }

    public BuildResponse updateBuildStatus(Long id, BuildStatus status) {
        Build build = getBuildEntity(id);
        build.setBuildStatus(status);
        if (status == BuildStatus.SUCCESS || status == BuildStatus.FAILED) {
            build.setCompletedAt(LocalDateTime.now());
        }
        Build savedBuild = buildRepository.save(build);
        auditLogService.logAction(
                "BUILD_STATUS_UPDATED",
                "BUILD",
                savedBuild.getId(),
                savedBuild.getTriggeredBy(),
                "Build status updated to " + status
        );
        return toResponse(savedBuild);
    }

    public Build getBuildEntity(Long id) {
        return buildRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Build not found with id: " + id));
    }

    private BuildResponse toResponse(Build build) {
        return new BuildResponse(
                build.getId(),
                build.getProject().getId(),
                build.getBranchName(),
                build.getCommitHash(),
                build.getBuildStatus(),
                build.getTriggeredBy(),
                build.getStartedAt(),
                build.getCompletedAt()
        );
    }
}
