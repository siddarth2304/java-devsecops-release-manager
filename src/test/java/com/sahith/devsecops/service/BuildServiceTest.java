package com.sahith.devsecops.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sahith.devsecops.dto.BuildRequest;
import com.sahith.devsecops.dto.BuildResponse;
import com.sahith.devsecops.entity.Build;
import com.sahith.devsecops.entity.BuildStatus;
import com.sahith.devsecops.entity.Project;
import com.sahith.devsecops.repository.BuildRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BuildServiceTest {

    @Mock
    private BuildRepository buildRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private BuildService buildService;

    @Test
    void createBuildForValidProject() {
        Project project = new Project();
        project.setId(10L);

        Build build = new Build();
        build.setId(20L);
        build.setProject(project);
        build.setBranchName("main");
        build.setCommitHash("abc123");
        build.setBuildStatus(BuildStatus.QUEUED);
        build.setTriggeredBy("sahith");
        build.setStartedAt(LocalDateTime.now());

        when(projectService.getProjectEntity(10L)).thenReturn(project);
        when(buildRepository.save(any(Build.class))).thenReturn(build);

        BuildResponse response = buildService.createBuild(
                new BuildRequest(10L, "main", "abc123", null, "sahith")
        );

        assertEquals(10L, response.projectId());
        assertEquals(BuildStatus.QUEUED, response.buildStatus());
    }

    @Test
    void buildStatusUpdateToSuccessSetsCompletedAt() {
        Project project = new Project();
        project.setId(10L);

        Build build = new Build();
        build.setId(20L);
        build.setProject(project);
        build.setTriggeredBy("sahith");
        build.setBuildStatus(BuildStatus.RUNNING);

        when(buildRepository.findById(20L)).thenReturn(Optional.of(build));
        when(buildRepository.save(any(Build.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BuildResponse response = buildService.updateBuildStatus(20L, BuildStatus.SUCCESS);

        assertEquals(BuildStatus.SUCCESS, response.buildStatus());
        assertNotNull(response.completedAt());
    }
}
