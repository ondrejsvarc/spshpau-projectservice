package com.spshpau.projectservice.services.impl;

import com.spshpau.projectservice.dto.MilestoneCreateDto;
import com.spshpau.projectservice.dto.MilestoneResponseDto;
import com.spshpau.projectservice.dto.MilestoneUpdateDto;
import com.spshpau.projectservice.services.exceptions.MilestoneNotFoundException;
import com.spshpau.projectservice.services.exceptions.ProjectNotFoundException;
import com.spshpau.projectservice.services.exceptions.UnauthorizedOperationException;
import com.spshpau.projectservice.model.Project;
import com.spshpau.projectservice.model.ProjectMilestone;
import com.spshpau.projectservice.repositories.ProjectMilestoneRepository;
import com.spshpau.projectservice.repositories.ProjectRepository;
import com.spshpau.projectservice.services.ProjectMilestoneService;
import com.spshpau.projectservice.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectMilestoneServiceImpl implements ProjectMilestoneService {

    private final ProjectRepository projectRepository;
    private final ProjectMilestoneRepository projectMilestoneRepository;
    private final ProjectService projectService;

    @Override
    @Transactional
    public MilestoneResponseDto createMilestone(UUID projectId, MilestoneCreateDto milestoneDto, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        ProjectMilestone milestone = new ProjectMilestone();
        milestone.setProject(project);
        milestone.setTitle(milestoneDto.getTitle());
        milestone.setDescription(milestoneDto.getDescription());
        milestone.setDueDate(milestoneDto.getDueDate());

        ProjectMilestone savedMilestone = projectMilestoneRepository.save(milestone);
        return MilestoneResponseDto.fromEntity(savedMilestone);
    }

    @Override
    @Transactional(readOnly = true)
    public MilestoneResponseDto getMilestoneById(UUID projectId, UUID milestoneId, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        ProjectMilestone milestone = projectMilestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new MilestoneNotFoundException("Milestone not found with ID: " + milestoneId));
        if (!milestone.getProject().getId().equals(projectId)) {
            throw new UnauthorizedOperationException("Milestone does not belong to the specified project.");
        }
        return MilestoneResponseDto.fromEntity(milestone);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MilestoneResponseDto> getMilestonesForProject(UUID projectId, UUID currentUserId, Pageable pageable) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        Page<ProjectMilestone> milestonePage = projectMilestoneRepository.findByProjectId(projectId, pageable);
        return milestonePage.map(MilestoneResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public MilestoneResponseDto updateMilestone(UUID projectId, UUID milestoneId, MilestoneUpdateDto milestoneDto, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        ProjectMilestone milestone = projectMilestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new MilestoneNotFoundException("Milestone not found with ID: " + milestoneId));

        if (!milestone.getProject().getId().equals(projectId)) {
            throw new UnauthorizedOperationException("Milestone does not belong to the specified project.");
        }

        if (milestoneDto.getTitle() != null) milestone.setTitle(milestoneDto.getTitle());
        if (milestoneDto.getDescription() != null) milestone.setDescription(milestoneDto.getDescription());
        if (milestoneDto.getDueDate() != null) milestone.setDueDate(milestoneDto.getDueDate());
        if (milestoneDto.getDueDate() == null && milestone.getDueDate() != null) {
            milestone.setDueDate(null);
        }


        ProjectMilestone updatedMilestone = projectMilestoneRepository.save(milestone);
        return MilestoneResponseDto.fromEntity(updatedMilestone);
    }

    @Override
    @Transactional
    public void deleteMilestone(UUID projectId, UUID milestoneId, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        ProjectMilestone milestone = projectMilestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new MilestoneNotFoundException("Milestone not found with ID: " + milestoneId));
        if (!milestone.getProject().getId().equals(projectId)) {
            throw new UnauthorizedOperationException("Milestone does not belong to the specified project.");
        }
        projectMilestoneRepository.delete(milestone);
    }
}
