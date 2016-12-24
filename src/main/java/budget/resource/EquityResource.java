package budget.resource;

import budget.model.Currency;
import budget.model.EQType;
import budget.model.Equity;
import budget.model.User;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

/**
 * Created by veghe on 25/11/2016.
 */
public class EquityResource extends ResourceSupport {
    private Long identifier;
    private String name;
    private User user;
    private BigDecimal balance;
    private Currency currency;
    private EQType type;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public EQType getType() {
        return type;
    }

    public void setType(EQType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Equity == false)
            return false;
        Equity other = (Equity) obj;
        if (getIdentifier() == null || other.getIdentifier() == null)
            return false;
        return getIdentifier().equals(other.getIdentifier());
    }
}
