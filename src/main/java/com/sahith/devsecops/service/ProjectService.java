package com.sahith.devsecops.service;

import com.sahith.devsecops.dto.ProjectRequest;
import com.sahith.devsecops.dto.ProjectResponse;
import com.sahith.devsecops.entity.Project;
import com.sahith.devsecops.entity.ProjectStatus;
import com.sahith.devsecops.exception.ResourceNotFoundException;
import com.sahith.devsecops.repository.ProjectRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AuditLogService auditLogService;

    public ProjectService(ProjectRepository projectRepository, AuditLogService auditLogService) {
        this.projectRepository = projectRepository;
        this.auditLogService = auditLogService;
    }

    public ProjectResponse createProject(ProjectRequest request) {
        Project project = new Project();
        applyRequest(project, request);
        if (project.getStatus() == null) {
            project.setStatus(ProjectStatus.ACTIVE);
        }
        Project savedProject = projectRepository.save(project);
        auditLogService.logAction(
                "PROJECT_CREATED",
                "PROJECT",
                savedProject.getId(),
                savedProject.getOwnerName(),
                "Created project " + savedProject.getName()
        );
        return toResponse(savedProject);
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ProjectResponse getProjectById(Long id) {
        return toResponse(getProjectEntity(id));
    }

    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = getProjectEntity(id);
        applyRequest(project, request);
        if (project.getStatus() == null) {
            project.setStatus(ProjectStatus.ACTIVE);
        }
        return toResponse(projectRepository.save(project));
    }

    public void deleteProject(Long id) {
        Project project = getProjectEntity(id);
        projectRepository.delete(project);
    }

    public Project getProjectEntity(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    private void applyRequest(Project project, ProjectRequest request) {
        project.setName(request.name());
        project.setRepositoryUrl(request.repositoryUrl());
        project.setTechnology(request.technology());
        project.setOwnerName(request.ownerName());
        project.setStatus(request.status());
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getRepositoryUrl(),
                project.getTechnology(),
                project.getOwnerName(),
                project.getStatus(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
