package com.spshpau.projectservice.controller;

import com.spshpau.projectservice.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

public interface BudgetController {

    // Budget Endpoints
    /**
     * Creates a new budget for a specified project.
     *
     * @param projectId The ID of the project for which to create the budget.
     * @param budgetDto The budget creation data.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the created BudgetResponseDto and HTTP status.
     */
    ResponseEntity<BudgetResponseDto> createProjectBudget(@PathVariable UUID projectId,
                                                          @Valid @RequestBody BudgetCreateDto budgetDto,
                                                          Jwt jwt);

    /**
     * Retrieves the budget for a specified project.
     *
     * @param projectId The ID of the project for which to retrieve the budget.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the BudgetResponseDto and HTTP status.
     */
    ResponseEntity<BudgetResponseDto> getProjectBudget(@PathVariable UUID projectId, Jwt jwt);

    /**
     * Updates an existing budget for a specified project.
     *
     * @param projectId The ID of the project for which to update the budget.
     * @param budgetDto The budget update data.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the updated BudgetResponseDto and HTTP status.
     */
    ResponseEntity<BudgetResponseDto> updateProjectBudget(@PathVariable UUID projectId,
                                                          @Valid @RequestBody BudgetUpdateDto budgetDto,
                                                          Jwt jwt);

    /**
     * Deletes the budget for a specified project.
     *
     * @param projectId The ID of the project for which to delete the budget.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity with no content and HTTP status.
     */
    ResponseEntity<Void> deleteProjectBudget(@PathVariable UUID projectId, Jwt jwt);

    /**
     * Retrieves the remaining budget for a specified project.
     *
     * @param projectId The ID of the project for which to retrieve the remaining budget.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the RemainingBudgetDto and HTTP status.
     */
    ResponseEntity<RemainingBudgetDto> getRemainingProjectBudget(@PathVariable UUID projectId, Jwt jwt);

    // Expense Endpoints
    /**
     * Adds a new expense to the budget of a specified project.
     *
     * @param projectId The ID of the project to which the expense will be added.
     * @param expenseDto The expense creation data.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the created ExpenseResponseDto and HTTP status.
     */
    ResponseEntity<ExpenseResponseDto> addExpense(@PathVariable UUID projectId,
                                                  @Valid @RequestBody ExpenseCreateDto expenseDto,
                                                  Jwt jwt);

    /**
     * Retrieves a specific expense by its ID for a given project.
     *
     * @param projectId The ID of the project to which the expense belongs.
     * @param expenseId The ID of the expense to retrieve.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the ExpenseResponseDto and HTTP status.
     */
    ResponseEntity<ExpenseResponseDto> getExpenseById(@PathVariable UUID projectId,
                                                      @PathVariable UUID expenseId,
                                                      Jwt jwt);

    /**
     * Retrieves a paginated list of expenses for a specified project.
     *
     * @param projectId The ID of the project for which to retrieve expenses.
     * @param jwt The JWT token for authentication and authorization.
     * @param pageable Pagination information.
     * @return A ResponseEntity containing a Page of ExpenseResponseDto and HTTP status.
     */
    ResponseEntity<Page<ExpenseResponseDto>> getExpensesForProject(@PathVariable UUID projectId,
                                                                   Jwt jwt,
                                                                   Pageable pageable);

    /**
     * Updates an existing expense for a specified project.
     *
     * @param projectId The ID of the project to which the expense belongs.
     * @param expenseId The ID of the expense to update.
     * @param expenseDto The expense update data.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity containing the updated ExpenseResponseDto and HTTP status.
     */
    ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable UUID projectId,
                                                     @PathVariable UUID expenseId,
                                                     @Valid @RequestBody ExpenseUpdateDto expenseDto,
                                                     Jwt jwt);

    /**
     * Removes an expense from a specified project.
     *
     * @param projectId The ID of the project from which to remove the expense.
     * @param expenseId The ID of the expense to remove.
     * @param jwt The JWT token for authentication and authorization.
     * @return A ResponseEntity with no content and HTTP status.
     */
    ResponseEntity<Void> removeExpense(@PathVariable UUID projectId,
                                       @PathVariable UUID expenseId,
                                       Jwt jwt);
}
