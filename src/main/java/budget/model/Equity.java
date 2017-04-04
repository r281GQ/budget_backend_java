package budget.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by veghe on 16/08/2016.
 */
@Entity
public class Equity implements BudgetModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long identifier;


    private String name;


    private BigDecimal balance;


    @ManyToOne
    private User user;


    @Enumerated(EnumType.STRING)
    private Currency currency;


    @Enumerated(EnumType.STRING)
    private EQType type;

    @JsonIgnore
    @OneToMany(mappedBy = "equity")
    private List<Transaction> transactions;

    public EQType getType() {
        return type;
    }

    public void setType(EQType type) {
        this.type = type;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getIdentifier() == null)
            return false;
        if (obj instanceof Equity == false)
            return false;
        if (obj == null)
            return false;
        Equity other = (Equity) obj;
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

        return "resource class: Equity with id: " + identifierDetails;
    }
}
