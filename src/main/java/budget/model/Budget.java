package budget.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by veghe on 18/08/2016.
 */
@Entity
public class Budget implements BudgetModel {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long identifier;

    @NotNull
    @Min(0)
    private BigDecimal defaultAllowance;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "budget")
    private List<Transaction> transactions;

    @JsonIgnore
    @OneToMany(mappedBy = "budget")
    private List<BudgetPeriod> budgetPeriods;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Budget() {
        setTransactions(new ArrayList<>());
        setBudgetPeriods(new ArrayList<>());
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<BudgetPeriod> getBudgetPeriods() {
        return budgetPeriods;
    }

    public void setBudgetPeriods(List<BudgetPeriod> budgetPeriods) {
        this.budgetPeriods = budgetPeriods;
    }

    public BigDecimal getDefaultAllowance() {
        return defaultAllowance;
    }

    public void setDefaultAllowance(BigDecimal defaultAllowance) {
        this.defaultAllowance = defaultAllowance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getIdentifier() == null)
            return false;
        if (obj instanceof Budget == false)
            return false;
        if (obj == null)
            return false;
        Budget other = (Budget) obj;

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

        return "resource class: Budget with id: " + identifierDetails;
    }
}
