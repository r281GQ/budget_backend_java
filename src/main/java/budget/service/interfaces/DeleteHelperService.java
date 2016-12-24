package budget.service.interfaces;

import budget.model.*;

/**
 * Created by veghe on 18/11/2016.
 */
public interface DeleteHelperService {
    void deleteUser(User user);






    void deleteBudget(Budget budget);
    void deleteTransaction(Transaction transaction);
    void deleteEquity(Equity equity);
    void deleteGrouping(Grouping grouping);
    void deleteAccount(Account byId);
}
