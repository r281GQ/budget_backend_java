package budget.assembler;

import budget.controller.TransactionController;
import budget.model.*;
import budget.resource.TransactionResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Created by veghe on 18/12/2016.
 */
@Component
public class TransactionAssembler extends ResourceAssemblerSupport<Transaction, TransactionResource> {

    public TransactionAssembler() {
        super(TransactionController.class, TransactionResource.class);
    }

    @Override
    public TransactionResource toResource(Transaction entity) {
        TransactionResource transactionResource = new TransactionResource();

        transactionResource.setName(entity.getName());
        transactionResource.setCurrency(entity.getCurrency());
        transactionResource.setUser(entity.getUser());
        transactionResource.setAccount(prepareAccount(entity));
        transactionResource.setAmount(entity.getAmount());
        transactionResource.setAmountAtTheMomentOfTransactionForAccount(entity.getAmountAtTheMomentOfTransactionForAccount());
        transactionResource.setAmountAtTheMomentOfTransactionForBudget(entity.getAmountAtTheMomentOfTransactionForBudget());
        transactionResource.setAmountAtTheMomentOfTransactionForEQ(entity.getAmountAtTheMomentOfTransactionForEQ());
        transactionResource.setBudget(prepareBudget(entity));
        transactionResource.setBudgetPeriod(prepareBudgetPeriod(entity));
        transactionResource.setCreationDate(entity.getCreationDate());
        transactionResource.setEquity(prepareEquity(entity));
        transactionResource.setGrouping(prepareGrouping(entity));
        transactionResource.setIdentifier(entity.getIdentifier());
        transactionResource.setMemo(entity.getMemo());
        transactionResource.setPeriod(entity.getPeriod());

        return transactionResource;
    }

    private Equity prepareEquity(Transaction entity){
        Equity equity = entity.getEquity();
        if(equity != null)
            equity.setUser(null);
        return equity;
    }

    private Grouping prepareGrouping(Transaction entity) {
        Grouping groupingToInject = entity.getGrouping();
        groupingToInject.setUser(null);
        return groupingToInject;
    }

    private BudgetPeriod prepareBudgetPeriod(Transaction entity) {
        BudgetPeriod budgetPeriodToInject = entity.getBudgetPeriod();
        if (budgetPeriodToInject != null) {
            budgetPeriodToInject.setBudget(null);
            budgetPeriodToInject.setUser(null);
        }
        return budgetPeriodToInject;
    }

    private Budget prepareBudget(Transaction entity) {
        Budget budgetToInject = entity.getBudget();
        if (budgetToInject != null)
            budgetToInject.setUser(null);
        return budgetToInject;
    }

    private Account prepareAccount(Transaction entity) {
        Account accountToInject = entity.getAccount();
        accountToInject.setUser(null);
        return accountToInject;
    }
}
