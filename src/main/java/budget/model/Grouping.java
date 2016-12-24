package budget.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by veghe on 13/08/2016.
 */
@Entity
public class Grouping implements BudgetModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long identifier;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "grouping")
    @JsonIgnore
    private List<Transaction> transactions;

    public Grouping() {
        setTransactions(new ArrayList<>());
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getIdentifier() == null)
            return false;
        if (obj instanceof Grouping == false)
            return false;
        if (obj == null)
            return false;
        Grouping other = (Grouping) obj;

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

        return "resource class: Grouping with id: " + identifierDetails;
    }
}
