package budget.service.interfaces;

import budget.model.Budget;
import budget.model.BudgetPeriod;
import budget.model.Transaction;
import budget.model.User;

/**
 * Created by veghe on 04/12/2016.
 */
public interface CrossEffectManager {

    void deleteTransaction(Transaction transaction);

    void createTransaction(Transaction transaction);

    void updateBudget(Budget budget);

    void updateBudgetWithoutReCalculation(Budget budget);

    void updateBudgetPeriod(BudgetPeriod budgetPeriod);

    User getUserById(Long identifier);

    Budget getBudgetById(Long identifier);
}
