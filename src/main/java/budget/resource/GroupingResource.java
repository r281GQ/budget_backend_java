package budget.resource;

import budget.model.Type;
import budget.model.User;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by veghe on 01/12/2016.
 */
public class GroupingResource extends ResourceSupport {

    private Long identifier;
    private String name;
    private User user;
    private Type type;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof GroupingResource == false)
            return false;
        GroupingResource other = (GroupingResource) obj;
        if (getIdentifier() == null || other.getIdentifier() == null)
            return false;
        return getIdentifier().equals(other.getIdentifier());
    }
}
