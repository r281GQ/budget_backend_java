package budget.accessories.validators;

import budget.model.*;
import budget.repository.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by veghe on 03/12/2016.
 */
@Service
public class ValidationServiceImplementation implements ValidationService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BudgetPeriodRepository budgetPeriodRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EquityRepository equityRepository;

    @Autowired
    private GroupingRepository groupingRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public boolean hasIdentifier(BudgetModel budgetModel) {
        return budgetModel.getIdentifier() != null;
    }

    @Override
    public boolean isUpdateAble(Account account) {
        return areUnChangeAblePropertiesTheSame(account, extractDataBaseAccount(account));
    }

    @Override
    public boolean isUpdateAble(BudgetPeriod budgetPeriod) {

        BudgetPeriod inDB = budgetPeriodRepository.get(budgetPeriod.getIdentifier());

        boolean periodsEquals = inDB.getPeriod().getRepresentation().equals(budgetPeriod.getPeriod().getRepresentation());

        boolean balancesEquals = inDB.getBalance().doubleValue() == budgetPeriod.getBalance().doubleValue();

        boolean usersEquals = inDB.getUser().equals(budgetPeriod.getUser());

        boolean budgetEquals = inDB.getBudget().equals(budgetPeriod.getBudget());

        return periodsEquals && balancesEquals && usersEquals && budgetEquals;
    }

    @Override
    public boolean isUpdateAble(Budget budget) {
        Budget inDB = budgetRepository.get(budget.getIdentifier());
        return inDB.getUser().equals(budget.getUser());
    }

    @Override
    public boolean isUpdateAble(User user) {
        User inDB = userRepository.get(user.getIdentifier());
        return inDB.getRole().equals(user.getRole());
    }

    @Override
    public boolean isUpdateAble(Equity equity) {
        Equity inDB = equityRepository.get(equity.getIdentifier());
        inDB.getType().equals(equity.getType());
        return false;
    }

    @Override
    public boolean isUpdateAble(Transaction transaction) {
        Transaction inDB = transactionRepository.get(transaction.getIdentifier());
        return transaction.getUser().equals(inDB.getUser());

    }

    @Override
    public boolean isUpdateAble(Grouping grouping) {

        Grouping inDB = groupingRepository.get(grouping.getIdentifier());

        return inDB.getUser().getIdentifier() == grouping.getUser().getIdentifier() && inDB.getType().equals(grouping.getType());
    }

    private Account extractDataBaseAccount(Account account) {
        return accountRepository.get(account.getIdentifier());
    }

    private boolean areUnChangeAblePropertiesTheSame(Account provided, Account database) {
        return database.getBalance().floatValue() == provided.getBalance().floatValue() && provided.getUser().getIdentifier() == database.getUser().getIdentifier() && provided.getCurrency().equals(database.getCurrency());
    }
}
