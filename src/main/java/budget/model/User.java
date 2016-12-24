package budget.model;

import budget.accessories.validators.interfaces.ValidEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by veghe on 28/07/2016.
 */
@Entity
public class User implements BudgetModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long identifier;

    @NotNull
    private String name;

    private String role;

    @NotNull
    @ValidEmail
    private String email;

    @NotNull
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Account> accounts;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Grouping> groupings;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Equity> equities;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Budget> budgets;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<BudgetPeriod> budgetPeriods;

    public User() {
        setTransactions(new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public List<BudgetPeriod> getBudgetPeriods() {
        return budgetPeriods;
    }

    public void setBudgetPeriods(List<BudgetPeriod> budgetPeriods) {
        this.budgetPeriods = budgetPeriods;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
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

    public List<Grouping> getGroupings() {
        return groupings;
    }

    public void setGroupings(List<Grouping> groupings) {
        this.groupings = groupings;
    }

    public List<Equity> getEquities() {
        return equities;
    }

    public void setEquities(List<Equity> equities) {
        this.equities = equities;
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getIdentifier() == null)
            return false;
        if (obj instanceof User == false)
            return false;
        if (obj == null)
            return false;
        User other = (User) obj;

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
        String emailDetails = getEmail() == null ? "email was not provided" : getEmail();

        return "resource class: User with id: " + identifierDetails + " and with email (optional): " + emailDetails;
    }
}
