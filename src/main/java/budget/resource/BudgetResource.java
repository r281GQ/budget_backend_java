package budget.resource;

import budget.model.User;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

/**
 * Created by veghe on 20/11/2016.
 */
public class BudgetResource extends ResourceSupport {

    private BigDecimal defaultAllowance;
    private Long identifier;
    private String name;
    private User user;

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
        if (obj instanceof BudgetResource == false)
            return false;
        BudgetResource other = (BudgetResource) obj;
        if (getIdentifier() == null || other.getIdentifier() == null)
            return false;
        return getIdentifier().equals(other.getIdentifier());
    }
}
