package budget.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by veghe on 08/08/2016.
 */

@Entity
public class Account implements BudgetModel {

    public Account() {
        setTransactions(new ArrayList<>());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long identifier;

    @NotNull
    private BigDecimal balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<Transaction> transactions;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getIdentifier() == null)
            return false;
        if (obj instanceof Account == false)
            return false;
        if (obj == null)
            return false;
        Account other = (Account) obj;

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

        return "resource class: Account with id: " + identifierDetails;
    }
}
