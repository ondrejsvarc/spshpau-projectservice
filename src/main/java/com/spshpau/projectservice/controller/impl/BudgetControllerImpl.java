package com.spshpau.projectservice.controller.impl;

import com.spshpau.projectservice.controller.BudgetController;
import com.spshpau.projectservice.services.BudgetService;
import com.spshpau.projectservice.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/project/budget")
@RequiredArgsConstructor
public class BudgetControllerImpl implements BudgetController {
    private final BudgetService budgetService;
    private final ProjectService projectService;
}
