package budget.repository.interfaces;

import budget.model.*;

import java.util.List;

/**
 * Created by veghe on 14/08/2016.
 */
public interface TransactionRepository extends Repository<Long, Transaction> {

    List<Transaction> findByUser(User user);

    List<Transaction> findByAccount(Account account);

    List<Transaction> findByBudget(Budget budget);

    List<Transaction> findBudgetPeriod(BudgetPeriod budgetPeriod);

    List<Transaction> findByEquity(Equity equity);

    List<Transaction> findByGrouping(Grouping grouping);
}
