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

    /**
     * Creates a new project.
     *
     * @param projectDto The project creation data.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the created ProjectResponseDto and HTTP status.
     */
    ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectCreateDto projectDto, Jwt jwt);

    /**
     * Retrieves a project by its ID.
     *
     * @param projectId The ID of the project to retrieve.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the ProjectResponseDto and HTTP status.
     */
    ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable UUID projectId, Jwt jwt);

    /**
     * Retrieves a paginated list of projects owned by the current authenticated user.
     *
     * @param jwt The JWT token for authentication and authorization.
     * @param pageable Pagination information.
     * @return A ResponseEntity containing a Page of ProjectResponseDto and HTTP status.
     */
    ResponseEntity<Page<ProjectResponseDto>> getMyOwnedProjects(Jwt jwt, Pageable pageable);

    /**
     * Retrieves a paginated list of projects where the current authenticated user is a collaborator.
     *
     * @param jwt The JWT token for authentication and authorization.
     * @param pageable Pagination information.
     * @return A ResponseEntity containing a Page of ProjectResponseDto and HTTP status.
     */
    ResponseEntity<Page<ProjectResponseDto>> getMyCollaboratingProjects(Jwt jwt, Pageable pageable);

    /**
     * Retrieves the owner of a specified project.
     *
     * @param projectId The ID of the project.
     * @return A ResponseEntity containing the UserSummaryDto of the project owner and HTTP status.
     */
    ResponseEntity<UserSummaryDto> getProjectOwner(@PathVariable UUID projectId);

    /**
     * Retrieves a paginated list of collaborators for a specified project.
     *
     * @param projectId The ID of the project.
     * @param pageable Pagination information.
     * @return A ResponseEntity containing a Page of UserSummaryDto for the collaborators and HTTP status.
     */
    ResponseEntity<Page<UserSummaryDto>> getProjectCollaborators(@PathVariable UUID projectId, Pageable pageable);

    /**
     * Updates the information of an existing project.
     *
     * @param projectId The ID of the project to update.
     * @param projectDto The project update data.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the updated ProjectResponseDto and HTTP status.
     */
    ResponseEntity<ProjectResponseDto> updateProjectInfo(@PathVariable UUID projectId, @Valid @RequestBody ProjectUpdateDto projectDto, Jwt jwt);

    /**
     * Deletes a project.
     *
     * @param projectId The ID of the project to delete.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity with no content and HTTP status.
     */
    ResponseEntity<Void> deleteProject(@PathVariable UUID projectId, Jwt jwt);

    /**
     * Adds a collaborator to a project.
     *
     * @param projectId The ID of the project.
     * @param collaboratorId The ID of the user to add as a collaborator.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the updated ProjectResponseDto and HTTP status.
     */
    ResponseEntity<ProjectResponseDto> addCollaborator(@PathVariable UUID projectId, @PathVariable UUID collaboratorId, Jwt jwt);

    /**
     * Removes a collaborator from a project.
     *
     * @param projectId The ID of the project.
     * @param collaboratorId The ID of the user to remove as a collaborator.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity with no content and HTTP status.
     */
    ResponseEntity<Void> removeCollaborator(@PathVariable UUID projectId, @PathVariable UUID collaboratorId, Jwt jwt);
}
