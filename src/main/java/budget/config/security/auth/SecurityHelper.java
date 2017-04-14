package budget.config.security.auth;

import budget.controller.exceptions.InvalidDataProvidedException;
import budget.controller.exceptions.UnAuthorizedException;
import budget.model.*;
import budget.repository.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by veghe on 16/11/2016.
 */

@Component("securityHelper")
public class SecurityHelper {

    @Autowired
    private UserRepository userRepository;

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
        if(!isRealResource(account))
            throw new UnAuthorizedException("Account does not belong to principal");
        return true;
    }

    public boolean doesBudgetBelongToLoggedInUser(long id) {
        Budget budget = new Budget();
        budget.setIdentifier(id);

        if(!isRealResource(budget))
            throw new UnAuthorizedException("Budget does not belong to principal");
        return  true;
    }

    public boolean doesTransactionBelongToLoggedInUser(long id) {
        Transaction transaction = new Transaction();
        transaction.setIdentifier(id);
        if(!isRealResource(transaction))
            throw new UnAuthorizedException("Transaction does not belong to principal");
        return  true;
    }

    public boolean doesGroupingBelongToLoggedInUser(long id) {
        Grouping grouping = new Grouping();
        grouping.setIdentifier(id);

        if(!isRealResource(grouping))
            throw new UnAuthorizedException("Grouping does not belong to principal");
        return true;
    }

    public boolean doesEquityBelongToLoggedInUser(long id) {
        Equity equity = new Equity();
        equity.setIdentifier(id);
        if(!isRealResource(equity))
            throw new UnAuthorizedException("Equity does not belong to principal");
        return true;
    }

    public boolean isUserProvidedPrincipal(long id){
        User user = new User();
        user.setIdentifier(id);
        if(user == null || user.getIdentifier() == null)
            throw new InvalidDataProvidedException("Either user is not present or user is missing identifier", user);
        if(getLoggedInUser().getIdentifier() != user.getIdentifier())
            throw new UnAuthorizedException("Principal is not the attached user on the resource");
        return true;
    }

    public boolean doesBudgetPeriodBelongToLoggedInUser(long id) {
        BudgetPeriod budgetPeriod = new BudgetPeriod();
        budgetPeriod.setIdentifier(id);
        if(!isRealResource(budgetPeriod))
            throw new UnAuthorizedException("BudgetPeriod does not belong to principal");
        return true;
    }

    public boolean isUserProvidedPrincipal(User user){
        if(user == null || user.getIdentifier() == null)
            throw new InvalidDataProvidedException("Either user is not present or user is missing identifier", user);
        if(getLoggedInUser().getIdentifier() != user.getIdentifier())
            throw new UnAuthorizedException("Principal is not the attached user on the resource");
        return true;
    }

    public boolean doResourcesBelongToPrincipal(Transaction transaction) {
        boolean mandatory = isRealResource(transaction.getAccount()) && isRealResource(transaction.getGrouping());

        if (transaction.getEquity() != null)
            mandatory = mandatory && isRealResource(transaction.getEquity());
        if (transaction.getBudget() != null)
            mandatory = mandatory && isRealResource(transaction.getBudget());

        if(!mandatory)
            throw new UnAuthorizedException("Some of the attached resources does not belong to principal");
        return true;
    }

    private boolean isRealResource(Transaction transaction) {
        if(transaction == null || transaction.getIdentifier() == null)
            throw new InvalidDataProvidedException("Either there is no transaction present or transaction is missing identifier", transaction);
        transaction = transactionRepository.get(transaction.getIdentifier());
        if(transaction == null)
            throw new UnAuthorizedException("Access forbidden to requested transaction");
        return transaction.getUser().equals(getLoggedInUser());
    }

    public boolean isRealResource(Budget budget) {
        if(budget == null || budget.getIdentifier() == null)
            throw new InvalidDataProvidedException("Either there is no budget present or budget is missing identifier", budget);
        budget = budgetRepository.get(budget.getIdentifier());
        if(budget == null)
            throw new UnAuthorizedException("Access forbidden to requested budget");
        return budget.getUser().equals(getLoggedInUser());
    }

    public boolean isRealResource(BudgetPeriod budgetPeriod) {
        if(budgetPeriod == null || budgetPeriod.getIdentifier() == null)
            throw new InvalidDataProvidedException("Either there is no budgetPeriod present or budgetPeriod is missing identifier", budgetPeriod);
        budgetPeriod = budgetPeriodRepository.get(budgetPeriod.getIdentifier());
        if(budgetPeriod == null)
            throw new UnAuthorizedException("Access forbidden to requested budgetPeriod");
        return budgetPeriod.getUser().equals(getLoggedInUser());
    }

    public boolean isRealResource(Equity equity) {
        if(equity == null || equity.getIdentifier() == null)
            throw new InvalidDataProvidedException("Either there is no equity present or equity is missing identifier", equity);
        equity = equityRepository.get(equity.getIdentifier());
        if(equity == null)
            throw new UnAuthorizedException("Access forbidden to requested equity");
        return equity.getUser().equals(getLoggedInUser());
    }

    public boolean isRealResource(Grouping grouping) {
        if(grouping == null || grouping.getIdentifier() == null)
            throw new InvalidDataProvidedException("Either there is no grouping present or grouping is missing identifier", grouping);
        grouping = groupingRepository.get(grouping.getIdentifier());
        if(grouping == null)
            throw new UnAuthorizedException("Access forbidden to requested grouping");
        return grouping.getUser().equals(getLoggedInUser());
    }

    public boolean isRealResource(Account account) {
        if(account == null || account.getIdentifier() == null)
            throw new InvalidDataProvidedException("Either there is no account present or account is missing identifier", account);
        account = accountRepository.get(account.getIdentifier());
        if(account == null)
            throw new UnAuthorizedException("Access forbidden to requested account");
        return account.getUser().equals(getLoggedInUser());
    }

    private User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
