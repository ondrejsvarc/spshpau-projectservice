package com.spshpau.projectservice.services.impl;

import com.spshpau.projectservice.dto.ProjectCreateDto;
import com.spshpau.projectservice.dto.ProjectResponseDto;
import com.spshpau.projectservice.dto.ProjectUpdateDto;
import com.spshpau.projectservice.dto.UserSummaryDto;
import com.spshpau.projectservice.repositories.ProjectMilestoneRepository;
import com.spshpau.projectservice.repositories.ProjectTaskRepository;
import com.spshpau.projectservice.services.exceptions.*;
import com.spshpau.projectservice.model.Project;
import com.spshpau.projectservice.model.SimpleUser;
import com.spshpau.projectservice.otherservices.UserClient;
import com.spshpau.projectservice.repositories.ProjectRepository;
import com.spshpau.projectservice.services.ProjectService;
import com.spshpau.projectservice.services.SimpleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMilestoneRepository projectMilestoneRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final SimpleUserService simpleUserService;
    private final UserClient userClient;


    @Override
    @Transactional
    public ProjectResponseDto createProject(ProjectCreateDto projectDto, UUID ownerId, String ownerUsername, String ownerFirstName, String ownerLastName, String ownerLocation) {
        SimpleUser owner = simpleUserService.getOrCreateSimpleUser(ownerId, ownerUsername, ownerFirstName, ownerLastName, ownerLocation);
        Project project = new Project();
        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());
        project.setOwner(owner);
        Project savedProject = projectRepository.save(project);
        return ProjectResponseDto.fromEntity(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectById(UUID projectId, UUID currentUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        boolean isOwner = project.getOwner().getId().equals(currentUserId);
        boolean isCollaborator = project.getCollaborators().stream()
                .anyMatch(collaborator -> collaborator.getId().equals(currentUserId));

        if (!isOwner && !isCollaborator) {
            throw new UnauthorizedOperationException("User is not authorized to access this project.");
        }

        return ProjectResponseDto.fromEntity(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getOwnedProjects(UUID ownerId, Pageable pageable) {
        Page<Project> projectPage = projectRepository.findByOwnerId(ownerId, pageable);
        return projectPage.map(ProjectResponseDto::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getCollaboratingProjects(UUID collaboratorId, Pageable pageable) {
        Page<Project> projectPage = projectRepository.findByCollaboratorsId(collaboratorId, pageable);
        return projectPage.map(ProjectResponseDto::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserSummaryDto getProjectOwner(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));
        return UserSummaryDto.fromEntity(project.getOwner());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSummaryDto> getProjectCollaborators(UUID projectId, Pageable pageable) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        List<SimpleUser> collaboratorsList = new ArrayList<>(project.getCollaborators());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<UserSummaryDto> dtoList;

        if (collaboratorsList.isEmpty() || startItem >= collaboratorsList.size()) {
            dtoList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, collaboratorsList.size());
            dtoList = collaboratorsList.subList(startItem, toIndex).stream()
                    .map(UserSummaryDto::fromEntity)
                    .collect(Collectors.toList());
        }

        return new PageImpl<>(dtoList, pageable, collaboratorsList.size());
    }

    @Override
    @Transactional
    public ProjectResponseDto updateProject(UUID projectId, ProjectUpdateDto projectDto, UUID ownerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        if (!project.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedOperationException("User is not the owner of this project.");
        }

        if (projectDto.getTitle() != null) {
            project.setTitle(projectDto.getTitle());
        }
        if (projectDto.getDescription() != null) {
            project.setDescription(projectDto.getDescription());
        }
        Project updatedProject = projectRepository.save(project);
        return ProjectResponseDto.fromEntity(updatedProject);
    }

    @Override
    @Transactional
    public void deleteProject(UUID projectId, UUID ownerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        if (!project.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedOperationException("User is not the owner of this project.");
        }
        projectRepository.delete(project);
    }

    @Override
    @Transactional
    public ProjectResponseDto addCollaborator(UUID projectId, UUID collaboratorUserId, UUID ownerId, String bearerToken) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        if (!project.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedOperationException("Only the project owner can add collaborators.");
        }

        // Check if collaborator is already part of the project
        if (project.getCollaborators().stream().anyMatch(c -> c.getId().equals(collaboratorUserId))) {
            throw new CollaboratorAlreadyExistsException("User is already a collaborator on this project.");
        }
        // Check if owner is trying to add themselves as collaborator
        if (project.getOwner().getId().equals(collaboratorUserId)) {
            throw new IllegalArgumentException("Owner cannot be added as a collaborator to their own project.");
        }


        // Fetch collaborator details from UserClient to ensure they exist and get their info
        // Also verify they are in the owner's connections
        List<UserSummaryDto> connections = userClient.findConnectionsByJwt(bearerToken);
        UserSummaryDto collaboratorSummary = connections.stream()
                .filter(conn -> conn.getId().equals(collaboratorUserId))
                .findFirst()
                .orElseThrow(() -> new NotConnectedException("Collaborator must be one of the owner's connections."));

        // Get or create the SimpleUser entity for the collaborator
        SimpleUser collaborator = simpleUserService.getOrCreateSimpleUser(collaboratorSummary);

        project.getCollaborators().add(collaborator);
        collaborator.getCollaboratingProjects().add(project);

        Project updatedProject = projectRepository.save(project);
        return ProjectResponseDto.fromEntity(updatedProject);
    }

    @Override
    @Transactional
    public void removeCollaborator(UUID projectId, UUID collaboratorUserId, UUID ownerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        if (!project.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedOperationException("Only the project owner can remove collaborators.");
        }

        SimpleUser collaborator = simpleUserService.findUserById(collaboratorUserId);

        if (!project.getCollaborators().remove(collaborator)) {
            throw new CollaboratorNotFoundException("Collaborator not found on this project.");
        }
        collaborator.getCollaboratingProjects().remove(project);

        projectRepository.save(project);
    }
}