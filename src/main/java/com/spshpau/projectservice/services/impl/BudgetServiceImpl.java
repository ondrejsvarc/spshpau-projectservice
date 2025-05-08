package com.spshpau.projectservice.services.impl;

import com.spshpau.projectservice.dto.*;
import com.spshpau.projectservice.services.exceptions.*;
import com.spshpau.projectservice.model.BudgetExpense;
import com.spshpau.projectservice.model.Project;
import com.spshpau.projectservice.model.ProjectBudget;
import com.spshpau.projectservice.repositories.BudgetExpenseRepository;
import com.spshpau.projectservice.repositories.ProjectBudgetrepository;
import com.spshpau.projectservice.repositories.ProjectRepository;
import com.spshpau.projectservice.services.BudgetService;
import com.spshpau.projectservice.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final ProjectBudgetrepository projectBudgetRepository;
    private final BudgetExpenseRepository budgetExpenseRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;


    private float calculateSpentAmount(UUID budgetId) {
        return budgetExpenseRepository.sumExpensesByBudgetId(budgetId).orElse(0f);
    }

    @Override
    @Transactional
    public BudgetResponseDto createProjectBudget(UUID projectId, BudgetCreateDto budgetDto, UUID currentUserId) {
        if (!projectService.isUserOwnerOfProject(projectId, currentUserId)) {
            throw new UnauthorizedOperationException("Only the project owner can create a budget.");
        }
        if (projectBudgetRepository.existsById(projectId)) {
            throw new BudgetAlreadyExistsException("Budget already exists for project ID: " + projectId);
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        ProjectBudget budget = new ProjectBudget();
        budget.setId(projectId);
        budget.setProject(project);
        budget.setCurrency(budgetDto.getCurrency());
        budget.setTotalAmount(budgetDto.getTotalAmount());

        ProjectBudget savedBudget = projectBudgetRepository.save(budget);
        project.setBudget(savedBudget);
        projectRepository.save(project);

        return BudgetResponseDto.fromEntity(savedBudget, 0f);
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetResponseDto getProjectBudget(UUID projectId, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        ProjectBudget budget = projectBudgetRepository.findById(projectId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found for project ID: " + projectId));
        float spentAmount = calculateSpentAmount(budget.getId());
        return BudgetResponseDto.fromEntity(budget, spentAmount);
    }

    @Override
    @Transactional
    public BudgetResponseDto updateProjectBudget(UUID projectId, BudgetUpdateDto budgetDto, UUID currentUserId) {
        if (!projectService.isUserOwnerOfProject(projectId, currentUserId)) {
            throw new UnauthorizedOperationException("Only the project owner can update the budget.");
        }
        ProjectBudget budget = projectBudgetRepository.findById(projectId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found for project ID: " + projectId + ". Cannot update."));

        boolean updated = false;
        if (budgetDto.getCurrency() != null && !budgetDto.getCurrency().equals(budget.getCurrency())) {
            budget.setCurrency(budgetDto.getCurrency());
            updated = true;
        }
        if (budgetDto.getTotalAmount() != null && !budgetDto.getTotalAmount().equals(budget.getTotalAmount())) {
            budget.setTotalAmount(budgetDto.getTotalAmount());
            updated = true;
        }

        ProjectBudget updatedBudget = budget;
        if (updated) {
            updatedBudget = projectBudgetRepository.save(budget);
        }

        float spentAmount = calculateSpentAmount(updatedBudget.getId());
        return BudgetResponseDto.fromEntity(updatedBudget, spentAmount);
    }

    @Override
    @Transactional
    public void deleteProjectBudget(UUID projectId, UUID currentUserId) {
        if (!projectService.isUserOwnerOfProject(projectId, currentUserId)) {
            throw new UnauthorizedOperationException("Only the project owner can delete a budget.");
        }
        ProjectBudget budget = projectBudgetRepository.findById(projectId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found for project ID: " + projectId));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));
        project.setBudget(null);
        projectRepository.save(project);
    }

    @Override
    @Transactional(readOnly = true)
    public RemainingBudgetDto getRemainingProjectBudget(UUID projectId, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        ProjectBudget budget = projectBudgetRepository.findById(projectId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found for project ID: " + projectId));
        float spentAmount = calculateSpentAmount(budget.getId());
        return new RemainingBudgetDto(budget.getTotalAmount(), spentAmount, budget.getTotalAmount() - spentAmount, budget.getCurrency());
    }

    @Override
    @Transactional
    public ExpenseResponseDto addExpenseToBudget(UUID projectId, ExpenseCreateDto expenseDto, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        ProjectBudget budget = projectBudgetRepository.findById(projectId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found for project ID: " + projectId + ". Cannot add expense."));

        BudgetExpense expense = new BudgetExpense();
        expense.setBudget(budget);
        expense.setAmount(expenseDto.getAmount());
        expense.setDate(expenseDto.getDate() != null ? expenseDto.getDate() : new Date());
        expense.setComment(expenseDto.getComment());

        BudgetExpense savedExpense = budgetExpenseRepository.save(expense);
        return ExpenseResponseDto.fromEntity(savedExpense);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponseDto getExpenseById(UUID projectId, UUID expenseId, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        if (!projectBudgetRepository.existsById(projectId)) {
            throw new BudgetNotFoundException("Budget not found for project ID: " + projectId);
        }
        BudgetExpense expense = budgetExpenseRepository.findByIdAndBudgetId(expenseId, projectId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID " + expenseId + " not found for budget " + projectId));
        return ExpenseResponseDto.fromEntity(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseResponseDto> getExpensesForProjectBudget(UUID projectId, UUID currentUserId, Pageable pageable) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        if (!projectBudgetRepository.existsById(projectId)) {
            throw new BudgetNotFoundException("Budget not found for project ID: " + projectId);
        }
        Page<BudgetExpense> expensesPage = budgetExpenseRepository.findByBudgetId(projectId, pageable);
        return expensesPage.map(ExpenseResponseDto::fromEntity);
    }


    @Override
    @Transactional
    public ExpenseResponseDto updateExpense(UUID projectId, UUID expenseId, ExpenseUpdateDto expenseDto, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        if (!projectBudgetRepository.existsById(projectId)) {
            throw new BudgetNotFoundException("Budget not found for project ID: " + projectId);
        }
        BudgetExpense expense = budgetExpenseRepository.findByIdAndBudgetId(expenseId, projectId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID " + expenseId + " not found for budget " + projectId));

        if (expenseDto.getAmount() != null) expense.setAmount(expenseDto.getAmount());
        if (expenseDto.getDate() != null) expense.setDate(expenseDto.getDate());
        if (expenseDto.getComment() != null) expense.setComment(expenseDto.getComment());

        BudgetExpense updatedExpense = budgetExpenseRepository.save(expense);
        return ExpenseResponseDto.fromEntity(updatedExpense);
    }

    @Override
    @Transactional
    public void removeExpense(UUID projectId, UUID expenseId, UUID currentUserId) {
        projectService.verifyUserIsProjectMember(projectId, currentUserId);
        if (!projectBudgetRepository.existsById(projectId)) {
            throw new BudgetNotFoundException("Budget not found for project ID: " + projectId);
        }
        BudgetExpense expense = budgetExpenseRepository.findByIdAndBudgetId(expenseId, projectId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID " + expenseId + " not found for budget " + projectId));
        budgetExpenseRepository.delete(expense);
    }
}
