package com.spshpau.projectservice.controller;

import com.spshpau.projectservice.dto.ProjectCreateDto;
import com.spshpau.projectservice.dto.ProjectResponseDto;
import com.spshpau.projectservice.dto.ProjectUpdateDto;
import com.spshpau.projectservice.dto.UserSummaryDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProjectController {
    ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectCreateDto projectDto, Jwt jwt);

    ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable UUID projectId, Jwt jwt);

    ResponseEntity<Page<ProjectResponseDto>> getMyOwnedProjects(Jwt jwt, Pageable pageable);

    ResponseEntity<Page<ProjectResponseDto>> getMyCollaboratingProjects(Jwt jwt, Pageable pageable);


    ResponseEntity<UserSummaryDto> getProjectOwner(@PathVariable UUID projectId);

    ResponseEntity<Page<UserSummaryDto>> getProjectCollaborators(@PathVariable UUID projectId, Pageable pageable);

    ResponseEntity<ProjectResponseDto> updateProjectInfo(@PathVariable UUID projectId, @Valid @RequestBody ProjectUpdateDto projectDto, Jwt jwt);

    ResponseEntity<Void> deleteProject(@PathVariable UUID projectId, Jwt jwt);

    ResponseEntity<ProjectResponseDto> addCollaborator(@PathVariable UUID projectId, @PathVariable UUID collaboratorId, Jwt jwt);

    ResponseEntity<Void> removeCollaborator(@PathVariable UUID projectId, @PathVariable UUID collaboratorId, Jwt jwt);
}
