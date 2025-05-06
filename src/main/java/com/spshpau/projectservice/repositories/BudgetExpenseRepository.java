package com.spshpau.projectservice.repositories;

import com.spshpau.projectservice.model.BudgetExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BudgetExpenseRepository extends JpaRepository<BudgetExpense, UUID> {
}
