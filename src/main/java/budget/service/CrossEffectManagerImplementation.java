package budget.service;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.model.Budget;
import budget.model.BudgetPeriod;
import budget.model.Transaction;
import budget.model.User;
import budget.repository.interfaces.*;
import budget.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by veghe on 04/12/2016.
 */
@Service
@Transactional
public class CrossEffectManagerImplementation implements CrossEffectManager {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private EquityRepository equityRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetPeriodRepository budgetPeriodRepository;

    @Autowired
    private PeriodCreatorService periodCreatorServiceImplementation;

    @Autowired
    private EquitySetterService equitySetterService;

    @Autowired
    private BudgetPeriodSetterService budgetPeriodSetterService;

    @Autowired
    private AccountSetterService accountSetterService;

    @Autowired
    private CurrencySetterService currencySetterService;

    @Autowired
    private GroupingRepository groupingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DefaultValueProviderService defaultValueProviderService;

    @Override
    public void deleteTransaction(Transaction transaction) {
        transaction = transactionRepository.get(transaction.getIdentifier());
        accountSetterService.updateAccountOnTransactionDeletion(transaction);

        if (hasEquity(transaction))
            equitySetterService.updateEquityOnTransactionDeletion(transaction);

        transactionRepository.delete(transaction.getIdentifier());

        if (hasBudget(transaction))
            budgetPeriodSetterService.updateBudgetPeriodOnTransactionDeletion(transaction);
    }

    @Override
    public void createTransaction(Transaction transaction) {

        checkDependencies(transaction);

        setDependencies(transaction);

        if (hasBudget(transaction))
            transaction.setBudget(budgetRepository.get(transaction.getBudget().getIdentifier()));

        if (hasEquity(transaction))
            transaction.setEquity(equityRepository.get(transaction.getEquity().getIdentifier()));

        currencySetterService.setValues(transaction);

        if (transaction.getCreationDate() == null)
            transaction.setCreationDate(new Date());

        accountSetterService.updateAccountOnTransactionCreation(transaction);

        transaction.setPeriod(periodCreatorServiceImplementation.createPeriod(transaction.getCreationDate()));

        if (hasEquity(transaction))
            equitySetterService.updateEquityOnTransactionCreation(transaction);

        transactionRepository.create(transaction);
        if (hasBudget(transaction))
            budgetPeriodSetterService.updateBudgetPeriodOnTransactionCreation(transaction);

    }

    private void setDependencies(Transaction transaction) {
        transaction.setGrouping(groupingRepository.get(transaction.getGrouping().getIdentifier()));
        transaction.setAccount(accountRepository.get(transaction.getAccount().getIdentifier()));
        transaction.setUser(getUserById(transaction.getUser().getIdentifier()));
    }

    private void checkDependencies(Transaction transaction) {
        if (transaction.getAccount() == null || transaction.getAccount().getIdentifier() == null)
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.MISSING_ACCOUNT), transaction);

        if (transaction.getAccount() == null || transaction.getAccount().getIdentifier() == null)
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.MISSING_ACCOUNT), transaction);

        if (transaction.getGrouping() == null || transaction.getGrouping().getIdentifier() == null)
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.MISSING_GROUPING), transaction);
    }

    @Override
    public void updateBudget(Budget budget) {
        budgetRepository.update(budget);
        budgetPeriodSetterService.updateBudgetPeriodOnBudgetUpdate(budget);
    }

    @Override
    public void updateBudgetWithoutReCalculation(Budget budget) {
        budgetPeriodSetterService.updateBudgetPeriodOnBudgetUpdateWithoutReCalculation(budget);
        budgetRepository.update(budget);
    }

    @Override
    public void updateBudgetPeriod(BudgetPeriod budgetPeriod) {
        budgetPeriodSetterService.updateBudgetPeriod(budgetPeriod);
    }

    @Override
    public User getUserById(Long identifier) {
        User user = userRepository.get(identifier);
        if (user == null)
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.MISSING_USER), user);
        return user;
    }

    @Override
    public Budget getBudgetById(Long identifier) {
        Budget budget = budgetRepository.get(identifier);
        if (budget == null)
            throw new InvalidDataProvidedException(defaultValueProviderService.getExceptionMessages(InvalidType.MISSING_BUDGET), budget);
        return budget;
    }

    private boolean hasBudget(Transaction transaction) {
        return transaction.getBudget() != null;
    }

    private boolean hasEquity(Transaction transaction) {
        return transaction.getEquity() != null;
    }
}
