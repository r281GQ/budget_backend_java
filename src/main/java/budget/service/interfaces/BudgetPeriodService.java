package budget.service.interfaces;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.Budget;
import budget.model.BudgetPeriod;
import budget.model.Period;
import budget.model.User;

import java.util.List;

/**
 * Created by veghe on 19/08/2016.
 */
public interface BudgetPeriodService {

    BudgetPeriod getById(long id) throws ResourceNotFoundException;

    void update(BudgetPeriod budgetPeriod) throws InvalidDataProvidedException;

    List<BudgetPeriod> getByBudget(Budget budget);

    List<BudgetPeriod> getByUser(User user);

    BudgetPeriod getByBudgetAndPeriod(Budget budget, Period period) throws ResourceNotFoundException;
}
