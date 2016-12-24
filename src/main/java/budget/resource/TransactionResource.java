package budget.resource;

import budget.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by veghe on 17/12/2016.
 */
public class TransactionResource extends ResourceSupport {

    private Long identifier;

    @JsonIgnore
    private BigDecimal amountAtTheMomentOfTransactionForAccount;

    @JsonIgnore
    private BigDecimal amountAtTheMomentOfTransactionForEQ;

    @JsonIgnore
    private BigDecimal amountAtTheMomentOfTransactionForBudget;

    private Account account;

    private BudgetPeriod budgetPeriod;

    private Equity equity;

    private Period period;

    private User user;

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public BigDecimal getAmountAtTheMomentOfTransactionForAccount() {
        return amountAtTheMomentOfTransactionForAccount;
    }

    public void setAmountAtTheMomentOfTransactionForAccount(BigDecimal amountAtTheMomentOfTransactionForAccount) {
        this.amountAtTheMomentOfTransactionForAccount = amountAtTheMomentOfTransactionForAccount;
    }

    public BigDecimal getAmountAtTheMomentOfTransactionForEQ() {
        return amountAtTheMomentOfTransactionForEQ;
    }

    public void setAmountAtTheMomentOfTransactionForEQ(BigDecimal amountAtTheMomentOfTransactionForEQ) {
        this.amountAtTheMomentOfTransactionForEQ = amountAtTheMomentOfTransactionForEQ;
    }

    public BigDecimal getAmountAtTheMomentOfTransactionForBudget() {
        return amountAtTheMomentOfTransactionForBudget;
    }

    public void setAmountAtTheMomentOfTransactionForBudget(BigDecimal amountAtTheMomentOfTransactionForBudget) {
        this.amountAtTheMomentOfTransactionForBudget = amountAtTheMomentOfTransactionForBudget;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BudgetPeriod getBudgetPeriod() {
        return budgetPeriod;
    }

    public void setBudgetPeriod(BudgetPeriod budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    public Equity getEquity() {
        return equity;
    }

    public void setEquity(Equity equity) {
        this.equity = equity;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public Grouping getGrouping() {
        return grouping;
    }

    public void setGrouping(Grouping grouping) {
        this.grouping = grouping;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    private Budget budget;

    private Grouping grouping;

    private Currency currency;

    private BigDecimal amount;

    private String name;

    private String memo;

    private Date creationDate;

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof TransactionResource == false)
            return false;
        TransactionResource other = (TransactionResource) obj;
        if (getIdentifier() == null || other.getIdentifier() == null)
            return false;
        return getIdentifier().equals(other.getIdentifier());
    }
}
