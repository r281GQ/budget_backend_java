package budget.controller.exceptions;

/**
 * Created by veghe on 23/11/2016.
 */
public class BudgetRestException extends RuntimeException {

    protected final String resource;
    protected final Long id;
    protected final String field;
    protected final String reason;

    protected BudgetRestException(String resource, Long id, String field, String reason) {
        super(resource+" "+id+" "+field+" "+reason);
        this.field = field;
        this.id = id;
        this.reason = reason;
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    public Long getId() {
        return id;
    }

    public String getField() {
        return field;
    }

    public String getReason() {
        return reason;
    }
}
