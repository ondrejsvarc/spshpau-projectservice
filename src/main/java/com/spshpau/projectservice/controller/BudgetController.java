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
    ResponseEntity<BudgetResponseDto> createProjectBudget(@PathVariable UUID projectId,
                                                          @Valid @RequestBody BudgetCreateDto budgetDto,
                                                          Jwt jwt);

    ResponseEntity<BudgetResponseDto> getProjectBudget(@PathVariable UUID projectId, Jwt jwt);

    ResponseEntity<BudgetResponseDto> updateProjectBudget(@PathVariable UUID projectId,
                                                          @Valid @RequestBody BudgetUpdateDto budgetDto,
                                                          Jwt jwt);

    ResponseEntity<Void> deleteProjectBudget(@PathVariable UUID projectId, Jwt jwt);

    ResponseEntity<RemainingBudgetDto> getRemainingProjectBudget(@PathVariable UUID projectId, Jwt jwt);

    // Expense Endpoints
    ResponseEntity<ExpenseResponseDto> addExpense(@PathVariable UUID projectId,
                                                  @Valid @RequestBody ExpenseCreateDto expenseDto,
                                                  Jwt jwt);
    ResponseEntity<ExpenseResponseDto> getExpenseById(@PathVariable UUID projectId,
                                                      @PathVariable UUID expenseId,
                                                      Jwt jwt);

    ResponseEntity<Page<ExpenseResponseDto>> getExpensesForProject(@PathVariable UUID projectId,
                                                                   Jwt jwt,
                                                                   Pageable pageable);

    ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable UUID projectId,
                                                     @PathVariable UUID expenseId,
                                                     @Valid @RequestBody ExpenseUpdateDto expenseDto,
                                                     Jwt jwt);

    ResponseEntity<Void> removeExpense(@PathVariable UUID projectId,
                                       @PathVariable UUID expenseId,
                                       Jwt jwt);
}
