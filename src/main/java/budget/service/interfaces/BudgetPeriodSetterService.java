package budget.service.interfaces;

import budget.model.Budget;
import budget.model.BudgetPeriod;
import budget.model.Transaction;

/**
 * Created by veghe on 04/12/2016.
 */

public interface BudgetPeriodSetterService {
    void updateBudgetPeriodOnTransactionCreation(Transaction transaction);

    void updateBudgetPeriodOnTransactionDeletion(Transaction transaction);

    void updateBudgetPeriodOnBudgetUpdate(Budget budget);

    void updateBudgetPeriodOnBudgetUpdateWithoutReCalculation(Budget budget);

    void updateBudgetPeriod(BudgetPeriod budgetPeriod);
}

