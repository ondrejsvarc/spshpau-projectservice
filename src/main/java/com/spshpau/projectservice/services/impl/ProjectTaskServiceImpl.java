package com.spshpau.projectservice.services.impl;

import com.spshpau.projectservice.dto.TaskCreateDto;
import com.spshpau.projectservice.dto.TaskResponseDto;
import com.spshpau.projectservice.dto.TaskUpdateDto;
import com.spshpau.projectservice.services.exceptions.*;
import com.spshpau.projectservice.model.Project;
import com.spshpau.projectservice.model.ProjectTask;
import com.spshpau.projectservice.model.SimpleUser;
import com.spshpau.projectservice.repositories.ProjectRepository;
import com.spshpau.projectservice.repositories.ProjectTaskRepository;
import com.spshpau.projectservice.services.ProjectService;
import com.spshpau.projectservice.services.ProjectTaskService;
import com.spshpau.projectservice.services.SimpleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectTaskServiceImpl implements ProjectTaskService {

    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final SimpleUserService simpleUserService;
    private final ProjectService projectService;

    @Override
    @Transactional
    public TaskResponseDto createTask(UUID projectId, TaskCreateDto taskDto, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        ProjectTask task = new ProjectTask();
        task.setProject(project);
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setCreatedAt(Timestamp.from(Instant.now()));
        task.setDueDate(taskDto.getDueDate());
        task.setStatus(taskDto.getStatus());

        if (taskDto.getAssignedUserId() != null) {
            projectService.verifyUserIsProjectMember(projectId, taskDto.getAssignedUserId());
            SimpleUser assignedUser = simpleUserService.findUserById(taskDto.getAssignedUserId());
            task.setAssignedUser(assignedUser);
        }

        ProjectTask savedTask = projectTaskRepository.save(task);
        return TaskResponseDto.fromEntity(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(UUID projectId, UUID taskId, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        ProjectTask task = projectTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));
        if (!task.getProject().getId().equals(projectId)) {
            throw new UnauthorizedOperationException("Task does not belong to the specified project.");
        }
        return TaskResponseDto.fromEntity(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponseDto> getTasksForProject(UUID projectId, UUID currentUserId, Pageable pageable) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        Page<ProjectTask> taskPage = projectTaskRepository.findByProjectId(projectId, pageable);
        return taskPage.map(TaskResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(UUID projectId, UUID taskId, TaskUpdateDto taskDto, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        ProjectTask task = projectTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));

        if (!task.getProject().getId().equals(projectId)) {
            throw new UnauthorizedOperationException("Task does not belong to the specified project.");
        }

        if (taskDto.getTitle() != null) task.setTitle(taskDto.getTitle());
        if (taskDto.getDescription() != null) task.setDescription(taskDto.getDescription());
        if (taskDto.getDueDate() != null) task.setDueDate(taskDto.getDueDate());
        if (taskDto.getStatus() != null) task.setStatus(taskDto.getStatus());

        if (taskDto.getAssignedUserId() != null) {
            projectService.verifyUserIsProjectMember(projectId, taskDto.getAssignedUserId());
            SimpleUser assignedUser = simpleUserService.findUserById(taskDto.getAssignedUserId());
            task.setAssignedUser(assignedUser);
        }

        ProjectTask updatedTask = projectTaskRepository.save(task);
        return TaskResponseDto.fromEntity(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(UUID projectId, UUID taskId, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        ProjectTask task = projectTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));
        if (!task.getProject().getId().equals(projectId)) {
            throw new UnauthorizedOperationException("Task does not belong to the specified project.");
        }
        projectTaskRepository.delete(task);
    }

    @Override
    @Transactional
    public TaskResponseDto assignUserToTask(UUID projectId, UUID taskId, UUID assigneeUserId, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        projectService.verifyUserIsProjectMember(projectId, assigneeUserId);

        ProjectTask task = projectTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));
        if (!task.getProject().getId().equals(projectId)) {
            throw new UnauthorizedOperationException("Task does not belong to the specified project.");
        }

        SimpleUser userToAssign = simpleUserService.findUserById(assigneeUserId);
        task.setAssignedUser(userToAssign);
        ProjectTask updatedTask = projectTaskRepository.save(task);
        return TaskResponseDto.fromEntity(updatedTask);
    }

    @Override
    @Transactional
    public TaskResponseDto removeUserFromTask(UUID projectId, UUID taskId, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        ProjectTask task = projectTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));
        if (!task.getProject().getId().equals(projectId)) {
            throw new UnauthorizedOperationException("Task does not belong to the specified project.");
        }
        task.setAssignedUser(null);
        ProjectTask updatedTask = projectTaskRepository.save(task);
        return TaskResponseDto.fromEntity(updatedTask);
    }
}
