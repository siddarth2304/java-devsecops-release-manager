package com.sahith.devsecops.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sahith.devsecops.dto.ProjectRequest;
import com.sahith.devsecops.dto.ProjectResponse;
import com.sahith.devsecops.entity.ProjectStatus;
import com.sahith.devsecops.exception.GlobalExceptionHandler;
import com.sahith.devsecops.service.ProjectService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProjectController.class)
@Import(GlobalExceptionHandler.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Test
    void createProjectReturnsCreatedResponse() throws Exception {
        ProjectResponse response = new ProjectResponse(
                1L,
                "Release Manager",
                "https://github.com/example/repo.git",
                "Spring Boot",
                "Sahith",
                ProjectStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(projectService.createProject(any(ProjectRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Release Manager",
                                  "repositoryUrl": "https://github.com/example/repo.git",
                                  "technology": "Spring Boot",
                                  "ownerName": "Sahith",
                                  "status": "ACTIVE"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Release Manager"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
