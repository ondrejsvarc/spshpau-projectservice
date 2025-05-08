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

    ResponseEntity<TaskResponseDto> createTask(@PathVariable UUID projectId,
                                               @Valid @RequestBody TaskCreateDto taskDto,
                                               Jwt jwt);

    ResponseEntity<TaskResponseDto> getTaskById(@PathVariable UUID projectId,
                                                @PathVariable UUID taskId,
                                                Jwt jwt);

    ResponseEntity<Page<TaskResponseDto>> getTasksForProject(@PathVariable UUID projectId,
                                                             Jwt jwt,
                                                             Pageable pageable);

    ResponseEntity<TaskResponseDto> updateTask(@PathVariable UUID projectId,
                                               @PathVariable UUID taskId,
                                               @Valid @RequestBody TaskUpdateDto taskDto,
                                               Jwt jwt);

    ResponseEntity<Void> deleteTask(@PathVariable UUID projectId,
                                    @PathVariable UUID taskId,
                                    Jwt jwt);

    ResponseEntity<TaskResponseDto> assignUserToTask(@PathVariable UUID projectId,
                                                     @PathVariable UUID taskId,
                                                     @PathVariable UUID assigneeUserId,
                                                     Jwt jwt);

    ResponseEntity<TaskResponseDto> removeUserFromTask(@PathVariable UUID projectId,
                                                       @PathVariable UUID taskId,
                                                       Jwt jwt);
}
