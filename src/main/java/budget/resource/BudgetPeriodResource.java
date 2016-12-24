package budget.resource;

import budget.model.Budget;
import budget.model.Period;
import budget.model.User;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

/**
 * Created by veghe on 21/11/2016.
 */
public class BudgetPeriodResource extends ResourceSupport implements Comparable<BudgetPeriodResource> {

    private String name;
    private Budget budget;
    private BigDecimal allowance;
    private BigDecimal balance;
    private Long identifier;
    private User user;
    private Period period;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int compareTo(BudgetPeriodResource o) {
        return getPeriod().compareTo(o.getPeriod());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof BudgetPeriodResource == false)
            return false;
        BudgetPeriodResource other = (BudgetPeriodResource) obj;

        if (getIdentifier() == null || other.getIdentifier() == null)
            return false;

        return getIdentifier().equals(other.getIdentifier());
    }
}
