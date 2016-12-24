package budget.resource;

import budget.model.Currency;
import budget.model.User;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

/**
 * Created by veghe on 11/08/2016.
 */

public class AccountResource extends ResourceSupport {

    private Long identifier;
    private User user;
    private String name;
    private BigDecimal balance;
    private Currency currency;

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
        if (obj instanceof AccountResource == false)
            return false;
        AccountResource other = (AccountResource) obj;
        return getIdentifier() == other.getIdentifier();
    }
}
