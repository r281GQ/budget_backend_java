package budget.service.interfaces;


import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.ResourceNotFoundException;
import budget.model.*;

import java.util.List;

public interface TransactionService {
    Transaction get(long id) throws ResourceNotFoundException;

    void create(Transaction transaction) throws InvalidDataProvidedException;

    void update(Transaction transaction) throws InvalidDataProvidedException;

    void delete(Transaction transaction) throws ResourceNotFoundException;

    List<Transaction> getByUser(User user);

    List <Transaction> getByAccount(Account account);

    List <Transaction> getByEquity(Equity equity);

    List <Transaction> getByGrouping(Grouping grouping);

    List <Transaction> getByBudget(Budget budget);

    List <Transaction> getByBudgetPeriod(BudgetPeriod budgetPeriod);
}