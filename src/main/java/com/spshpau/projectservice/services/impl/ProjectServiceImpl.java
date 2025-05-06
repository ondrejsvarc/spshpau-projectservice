package com.spshpau.projectservice.services.impl;

import com.spshpau.projectservice.repositories.ProjectMilestoneRepository;
import com.spshpau.projectservice.repositories.ProjectRepository;
import com.spshpau.projectservice.repositories.ProjectTaskRepository;
import com.spshpau.projectservice.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMilestoneRepository projectMilestoneRepository;
    private final ProjectTaskRepository projectTaskRepository;
}
