package budget.service;

import budget.model.*;
import budget.repository.interfaces.*;
import budget.service.interfaces.CrossEffectManager;
import budget.service.interfaces.DeleteHelperService;
import budget.service.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by veghe on 18/11/2016.
 */
@Service
@Transactional
public class DeleteHelperServiceImplementation implements DeleteHelperService {

    @Autowired
    private BudgetPeriodRepository budgetPeriodRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GroupingRepository groupingRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private EquityRepository equityRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CrossEffectManager crossEffectManager;

    @Override
    public void deleteUser(User user) {
        deleteEquitiesByUser(user);

        deleteBudgetsByUser(user);

        deleteGroupingsByUser(user);

        deleteAccountsByUser(user);

        userRepository.delete(user.getIdentifier());
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        crossEffectManager.deleteTransaction(transaction);
    }

    @Override
    public void deleteEquity(Equity equity) {
        for (Transaction transaction : transactionRepository.findByEquity(equity)) {
            crossEffectManager.deleteTransaction(transaction);
        }
        equityRepository.delete(equity.getIdentifier());
    }

    @Override
    public void deleteGrouping(Grouping grouping) {
        for (Transaction transaction : transactionRepository.findByGrouping(grouping)) {
            crossEffectManager.deleteTransaction(transaction);
        }
        groupingRepository.delete(grouping.getIdentifier());
    }

    private void deleteEquitiesByUser(User user) {
        equityRepository.findByUser(user).forEach(equity -> deleteEquity(equity));
    }


    private void deleteBudgetsByUser(User user) {
        budgetRepository.findByUser(user).forEach(budget -> deleteBudget(budget));
    }

    private void deleteAccountsByUser(User user) {
        accountRepository.findByUser(user).forEach(account -> deleteAccount(account));
    }

    private void deleteGroupingsByUser(User user) {
        groupingRepository.findByUser(user).forEach(grouping -> deleteGrouping(grouping));
    }

    private void deleteTransactionsByAccount(Account account) {
        List<Transaction> transactionList = transactionRepository.findByAccount(account);
        transactionList.forEach(transaction -> crossEffectManager.deleteTransaction(transaction));
    }

    private void deleteTransactionsByBudget(Budget budget) {
        List<Transaction> transactions = transactionRepository.findByBudget(budget);
        transactions.forEach(transaction -> crossEffectManager.deleteTransaction(transaction));
    }

    @Override
    public void deleteBudget(Budget budget) {
        deleteTransactionsByBudget(budget);
        deleteBudgetPeriodsByBudget(budget);
        budgetRepository.delete(budget.getIdentifier());
    }

    @Override
    public void deleteAccount(Account account) {
        deleteTransactionsByAccount(account);
        accountRepository.delete(account.getIdentifier());
    }

    private void deleteBudgetPeriodsByBudget(Budget budget) {
        List<BudgetPeriod> budgetPeriodList = budgetPeriodRepository.findByBudget(budget);
        budgetPeriodList.forEach(budgetPeriod -> budgetPeriodRepository.delete(budgetPeriod.getIdentifier()));
    }
}
