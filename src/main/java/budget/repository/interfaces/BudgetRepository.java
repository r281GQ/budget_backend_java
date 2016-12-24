package budget.repository.interfaces;

import budget.model.Budget;
import budget.model.User;

import java.util.List;

/**
 * Created by veghe on 11/09/2016.
 */

public interface BudgetRepository extends Repository<Long, Budget> {

    List<Budget> findByUser(User user);

}
