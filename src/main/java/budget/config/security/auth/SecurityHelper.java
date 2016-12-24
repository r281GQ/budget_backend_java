package budget.config.security.auth;

import budget.config.security.BudgetUser;
import budget.controller.exceptions.UnAuthorizedException;
import budget.model.*;
import budget.repository.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by veghe on 16/11/2016.
 */
@Component("securityHelper")
public class SecurityHelper {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetPeriodRepository budgetPeriodRepository;

    @Autowired
    private EquityRepository equityRepository;

    @Autowired
    private GroupingRepository groupingRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public boolean doesAccountBelongToLoggedInUser(long id) {
        Account account = new Account();
        account.setIdentifier(id);

        return isRealResource(account);
    }

    public boolean doesBudgetBelongToLoggedInUser(long id) {
        Budget budget = new Budget();
        budget.setIdentifier(id);

        return isRealResource(budget);
    }

    public boolean doesTransactionBelongToLoggedInUser(long id) {
        Transaction transaction = new Transaction();
        transaction.setIdentifier(id);

        return isRealResource(transaction);
    }

    private boolean isRealResource(Transaction transaction) {
        transaction = transactionRepository.get(transaction.getIdentifier());
        if (transaction == null) {
            transaction = new Transaction();
            transaction.setIdentifier(0L);
            throw new UnAuthorizedException();
        }
        return transaction.getUser().equals(getLoggedInUser());
    }

    public boolean doesEquityBelongToLoggedInUser(long id) {
        Equity equity = new Equity();
        equity.setIdentifier(id);

        return isRealResource(equity);
    }

    public boolean doesGroupingBelongToLoggedInUser(long id) {
        Grouping grouping = new Grouping();
        grouping.setIdentifier(id);

        return isRealResource(grouping);
    }

    public boolean doResourcesBelongToPrincipal(Transaction transaction) {
        boolean mandatory = isRealResource(transaction.getAccount()) && isRealResource(transaction.getGrouping());

        if (transaction.getEquity() != null)
            mandatory = mandatory && isRealResource(transaction.getEquity());
        if (transaction.getBudget() != null)
            mandatory = mandatory && isRealResource(transaction.getBudget());
        return mandatory;
    }

    public boolean isRealResource(Budget budget) {
        budget = budgetRepository.get(budget.getIdentifier());
        if (budget == null) {
            budget = new Budget();
            budget.setIdentifier(0L);
            throw new UnAuthorizedException();
        }
        return budget.getUser().equals(getLoggedInUser());
    }

    public boolean isRealResource(BudgetPeriod budgetPeriod) {
        budgetPeriod = budgetPeriodRepository.get(budgetPeriod.getIdentifier());
        if (budgetPeriod == null) {
            budgetPeriod = new BudgetPeriod();
            budgetPeriod.setIdentifier(0L);
            throw new UnAuthorizedException();
        }
        return budgetPeriod.getUser().equals(getLoggedInUser());
    }

    public boolean isRealResource(Equity equity) {
        equity = equityRepository.get(equity.getIdentifier());
        if (equity == null) {
            equity = new Equity();
            equity.setIdentifier(0L);
            throw new UnAuthorizedException();
        }
        return equity.getUser().equals(getLoggedInUser());
    }

    public boolean isRealResource(Grouping grouping) {
        grouping = groupingRepository.get(grouping.getIdentifier());
        if (grouping == null) {
            grouping = new Grouping();
            grouping.setIdentifier(0L);
            throw new UnAuthorizedException();
        }
        return grouping.getUser().equals(getLoggedInUser());
    }

    public boolean isRealResource(Account account) {
        account = accountRepository.get(account.getIdentifier());
        if (account == null) {
            account = new Account();
            account.setIdentifier(0L);
            throw new UnAuthorizedException();
        }
        return account.getUser().equals(getLoggedInUser());
    }

    private User getLoggedInUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        BudgetUser budgetUser = (BudgetUser) securityContext.getAuthentication().getPrincipal();

        User user = budgetUser.getUser();
        return user;
    }
}
