package budget.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by veghe on 08/08/2016.
 */

@Entity
public class Transaction implements BudgetModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long identifier;

    @JsonIgnore
    private BigDecimal amountAtTheMomentOfTransactionForAccount;

    @JsonIgnore
    private BigDecimal amountAtTheMomentOfTransactionForEQ;

    @JsonIgnore
    private BigDecimal amountAtTheMomentOfTransactionForBudget;

    @NotNull
    @ManyToOne
    private Account account;

    @Null
    @ManyToOne
    private BudgetPeriod budgetPeriod;

    @ManyToOne
    private Equity equity;

//    @Null
    @Embedded
    private Period period;

    @NotNull
    @ManyToOne
    private User user;

    @ManyToOne
    private Budget budget;

    @NotNull
    @ManyToOne
    private Grouping grouping;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    @Min(0)
    private BigDecimal amount;

    @NotNull
    private String name;

    private String memo;

//    @Null
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public Equity getEquity() {
        return equity;
    }

    public void setEquity(Equity equity) {
        this.equity = equity;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public BudgetPeriod getBudgetPeriod() {
        return budgetPeriod;
    }

    public void setBudgetPeriod(BudgetPeriod budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getIdentifier() == null)
            return false;
        if (obj instanceof Transaction == false)
            return false;
        if (obj == null)
            return false;
        Transaction other = (Transaction) obj;

        if (other.getIdentifier() == null)
            return false;

        return getIdentifier().equals(other.getIdentifier());
    }

    @Override
    public int hashCode() {
        return getIdentifier() == null ? 0 : getIdentifier().hashCode();
    }

    @Override
    public String toString() {
        String identifierDetails = (getIdentifier() == null || getIdentifier() == 0) ? "id was not provided" : getIdentifier().toString();

        return "resource class: Transaction with id: " + identifierDetails;
    }
}
