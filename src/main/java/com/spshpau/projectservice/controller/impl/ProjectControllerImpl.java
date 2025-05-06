package com.spshpau.projectservice.controller.impl;

import com.spshpau.projectservice.controller.ProjectController;
import com.spshpau.projectservice.services.ProjectService;
import com.spshpau.projectservice.services.SimpleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectControllerImpl implements ProjectController {
    private final ProjectService projectService;
    private final SimpleUserService simpleUserService;
}
