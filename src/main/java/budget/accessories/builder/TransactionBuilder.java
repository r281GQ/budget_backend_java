package budget.accessories.builder;

import budget.model.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by veghe on 21/08/2016.
 */
public class TransactionBuilder {

    private User user;

    private BigDecimal amount;

    private Account account;

    private Grouping grouping;

    private Currency currency;

    private Long identifier;

    private Budget budget;

    private Period period;

    private BudgetPeriod budgetPeriod;

    private Equity equity;

    private Date creationDate;

    private String name;

    private BigDecimal amountAtTheMomentOfTransactionForAccount;

    private BigDecimal amountAtTheMomentOfTransactionForEQ;

    private BigDecimal getAmountAtTheMomentOfTransactionForBudget;

    private TransactionBuilder() {
    }

    private TransactionBuilder(User user, Account account) {
        this.account = account;
        this.user = user;
        this.currency = Currency.GBP;
    }

    public static TransactionBuilder initialize(User user, Account account) {
        return new TransactionBuilder(user, account);
    }

    public TransactionBuilder setAmount(double amount) {
        this.amount = new BigDecimal(amount);
        return this;
    }

    public TransactionBuilder setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public TransactionBuilder setEquity(Equity equity) {
        this.equity = equity;
        return this;
    }

    public TransactionBuilder setIdentifier(long identifier) {
        this.identifier = identifier;
        return this;
    }

    public TransactionBuilder setBudget(Budget budget) {
        this.budget = budget;
        return this;
    }

    public TransactionBuilder setAmountAtTheMomentOfTransactionForAccount(double amount) {
        this.amountAtTheMomentOfTransactionForAccount = new BigDecimal(amount);
        return this;
    }

    public TransactionBuilder setAmountAtTheMomentOfTransactionForEQ(double amount) {
        this.amountAtTheMomentOfTransactionForEQ = new BigDecimal(amount);
        return this;
    }

    public TransactionBuilder setAmountAtTheMomentOfTransactionForBudget(double amount) {
        this.getAmountAtTheMomentOfTransactionForBudget = new BigDecimal(amount);
        return this;
    }

    public TransactionBuilder setBudgetPeriod(BudgetPeriod period) {
        this.budgetPeriod = period;
        return this;
    }

    public TransactionBuilder setPeriod(Period period) {
        this.period = period;
        return this;
    }

    public TransactionBuilder setGrouping(Grouping grouping) {
        this.grouping = grouping;
        return this;
    }

    public TransactionBuilder setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public TransactionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public Transaction build() {
        Transaction transaction = new Transaction();

        transaction.setUser(this.user);
        transaction.setAccount(this.account);
        transaction.setGrouping(this.grouping);
        transaction.setAmount(this.amount);
        transaction.setName(this.name);
        transaction.setCreationDate(this.creationDate);
        transaction.setEquity(this.equity);
        transaction.setIdentifier(this.identifier);
        transaction.setPeriod(this.period);
        transaction.setBudgetPeriod(this.budgetPeriod);
        transaction.setBudget(this.budget);
        transaction.setCurrency(this.currency);
        transaction.setAmountAtTheMomentOfTransactionForAccount(this.amountAtTheMomentOfTransactionForAccount);
        transaction.setAmountAtTheMomentOfTransactionForEQ(this.amountAtTheMomentOfTransactionForEQ);
        transaction.setAmountAtTheMomentOfTransactionForBudget(this.getAmountAtTheMomentOfTransactionForBudget);

        return transaction;
    }
}
