package com.spshpau.projectservice.controller;

import com.spshpau.projectservice.dto.TaskCreateDto;
import com.spshpau.projectservice.dto.TaskResponseDto;
import com.spshpau.projectservice.dto.TaskUpdateDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface ProjectTaskController {

    /**
     * Creates a new task for a specified project.
     *
     * @param projectId The ID of the project for which to create the task.
     * @param taskDto The task creation data.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the created TaskResponseDto and HTTP status.
     */
    ResponseEntity<TaskResponseDto> createTask(@PathVariable UUID projectId,
                                               @Valid @RequestBody TaskCreateDto taskDto,
                                               Jwt jwt);

    /**
     * Retrieves a specific task by its ID for a given project.
     *
     * @param projectId The ID of the project to which the task belongs.
     * @param taskId The ID of the task to retrieve.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the TaskResponseDto and HTTP status.
     */
    ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID projectId,
                                                @PathVariable UUID taskId,
                                                Jwt jwt);

    /**
     * Retrieves a paginated list of tasks for a specified project.
     *
     * @param projectId The ID of the project for which to retrieve tasks.
     * @param jwt The JWT token for authentication and authorization.
     * @param pageable Pagination information.
     * @return A ResponseEntity containing a Page of TaskResponseDto and HTTP status.
     */
    ResponseEntity<Page<TaskResponseDto>> getTasksForProject(@PathVariable UUID projectId,
                                                             Jwt jwt,
                                                             Pageable pageable);

    /**
     * Updates an existing task for a specified project.
     *
     * @param projectId The ID of the project to which the task belongs.
     * @param taskId The ID of the task to update.
     * @param taskDto The task update data.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the updated TaskResponseDto and HTTP status.
     */
    ResponseEntity<TaskResponseDto> updateTask(@PathVariable UUID projectId,
                                               @PathVariable UUID taskId,
                                               @Valid @RequestBody TaskUpdateDto taskDto,
                                               Jwt jwt);

    /**
     * Deletes a task from a specified project.
     *
     * @param projectId The ID of the project from which to delete the task.
     * @param taskId The ID of the task to delete.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity with no content and HTTP status.
     */
    ResponseEntity<Void> deleteTask(@PathVariable UUID projectId,
                                    @PathVariable UUID taskId,
                                    Jwt jwt);

    /**
     * Assigns a user to a specific task within a project.
     *
     * @param projectId The ID of the project to which the task belongs.
     * @param taskId The ID of the task to which the user will be assigned.
     * @param assigneeUserId The ID of the user to assign to the task.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the updated TaskResponseDto and HTTP status.
     */
    ResponseEntity<TaskResponseDto> assignUserToTask(@PathVariable UUID projectId,
                                                     @PathVariable UUID taskId,
                                                     @PathVariable UUID assigneeUserId,
                                                     Jwt jwt);

    /**
     * Removes the assigned user from a specific task within a project.
     *
     * @param projectId The ID of the project to which the task belongs.
     * @param taskId The ID of the task from which the user will be unassigned.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the updated TaskResponseDto (with no assigned user) and HTTP status.
     */
    ResponseEntity<TaskResponseDto> removeUserFromTask(@PathVariable UUID projectId,
                                                       @PathVariable UUID taskId,
                                                       Jwt jwt);
}
