package com.sahith.devsecops.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sahith.devsecops.dto.ProjectRequest;
import com.sahith.devsecops.dto.ProjectResponse;
import com.sahith.devsecops.entity.Project;
import com.sahith.devsecops.entity.ProjectStatus;
import com.sahith.devsecops.exception.ResourceNotFoundException;
import com.sahith.devsecops.repository.ProjectRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void createProjectSuccessfully() {
        ProjectRequest request = new ProjectRequest(
                "Release Manager",
                "https://github.com/example/repo.git",
                "Spring Boot",
                "Sahith",
                ProjectStatus.ACTIVE
        );

        Project project = new Project();
        project.setId(1L);
        project.setName(request.name());
        project.setRepositoryUrl(request.repositoryUrl());
        project.setTechnology(request.technology());
        project.setOwnerName(request.ownerName());
        project.setStatus(ProjectStatus.ACTIVE);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse response = projectService.createProject(request);

        assertEquals("Release Manager", response.name());
        assertEquals(ProjectStatus.ACTIVE, response.status());
        verify(projectRepository).save(any(Project.class));
        verify(auditLogService).logAction(any(String.class), any(String.class), any(Long.class),
                any(String.class), any(String.class));
    }

    @Test
    void throwsErrorWhenProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.getProjectById(99L));
    }
}
