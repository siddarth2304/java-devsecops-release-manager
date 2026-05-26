package com.sahith.devsecops.controller;

import com.sahith.devsecops.dto.BuildRequest;
import com.sahith.devsecops.dto.BuildResponse;
import com.sahith.devsecops.dto.BuildStatusUpdateRequest;
import com.sahith.devsecops.service.BuildService;
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
@RequestMapping("/api/builds")
public class BuildController {

    private final BuildService buildService;

    public BuildController(BuildService buildService) {
        this.buildService = buildService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BuildResponse createBuild(@Valid @RequestBody BuildRequest request) {
        return buildService.createBuild(request);
    }

    @GetMapping
    public List<BuildResponse> getAllBuilds() {
        return buildService.getAllBuilds();
    }

    @GetMapping("/{id}")
    public BuildResponse getBuildById(@PathVariable Long id) {
        return buildService.getBuildById(id);
    }

    @GetMapping("/project/{projectId}")
    public List<BuildResponse> getBuildsByProjectId(@PathVariable Long projectId) {
        return buildService.getBuildsByProjectId(projectId);
    }

    @PutMapping("/{id}/status")
    public BuildResponse updateBuildStatus(
            @PathVariable Long id,
            @Valid @RequestBody BuildStatusUpdateRequest request
    ) {
        return buildService.updateBuildStatus(id, request.buildStatus());
    }
}
