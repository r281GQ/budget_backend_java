package budget.repository.interfaces;

import budget.model.Budget;
import budget.model.BudgetPeriod;
import budget.model.Period;
import budget.model.User;

import java.util.List;

/**
 * Created by veghe on 19/08/2016.
 */
public interface BudgetPeriodRepository extends Repository<Long, BudgetPeriod> {

    List<BudgetPeriod> findByBudget(Budget budget);

    List<BudgetPeriod> findByBudgetOrderedByDate(Budget budget);

    BudgetPeriod findByBudgetAndPeriod(Budget budget, Period period);

    List<BudgetPeriod> findByUser(User user);
}
