package budget.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by veghe on 18/08/2016.
 */
@Entity
public class BudgetPeriod implements BudgetModel, Comparable<BudgetPeriod> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long identifier;


    private String name;


    @ManyToOne
    private Budget budget;



    private BigDecimal allowance;


    private BigDecimal balance;


    @ManyToOne
    private User user;


    @Embedded
    private Period period;

    @JsonIgnore
    @OneToMany(mappedBy = "budgetPeriod")
    private List<Transaction> transactionList;

    public BudgetPeriod() {
        setTransactionList(new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    @Override
    public int compareTo(BudgetPeriod o) {
        return getPeriod().compareTo(o.getPeriod());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getIdentifier() == null)
            return false;
        if (obj instanceof BudgetPeriod == false)
            return false;
        if (obj == null)
            return false;
        BudgetPeriod other = (BudgetPeriod) obj;

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

        return "resource class: BudgetPeriod with id: " + identifierDetails;
    }
}
