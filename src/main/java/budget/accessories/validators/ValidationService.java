package budget.accessories.validators;

import budget.model.*;

/**
 * Created by veghe on 02/12/2016.
 */
public interface ValidationService {

    boolean hasIdentifier (BudgetModel budgetModel);

    boolean isUpdateAble(Account account);

    boolean isUpdateAble(BudgetPeriod budgetPeriod);

    boolean isUpdateAble(Budget budget);

    boolean isUpdateAble(User user);

    boolean isUpdateAble(Equity equity);

    boolean isUpdateAble(Transaction transaction);

    boolean isUpdateAble(Grouping grouping);

}
