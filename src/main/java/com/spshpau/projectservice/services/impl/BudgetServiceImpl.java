package com.spshpau.projectservice.services.impl;

import com.spshpau.projectservice.repositories.BudgetExpenseRepository;
import com.spshpau.projectservice.repositories.ProjectBudgetrepository;
import com.spshpau.projectservice.services.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final ProjectBudgetrepository projectBudgetrepository;
    private final BudgetExpenseRepository budgetExpenseRepository;
}
