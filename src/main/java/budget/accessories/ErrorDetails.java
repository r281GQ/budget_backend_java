package budget.accessories;

/**
 * Created by veghe on 22/11/2016.
 */
public class ErrorDetails {

    private final Long id;
    private final String resource;
    private final String field;
    private final String reason;

    public ErrorDetails(String resource, String reason, String field, Long id) {
        this.id = id;
        this.field = field;
        this.reason = reason;
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    public String getField() {
        return field;
    }

    public String getReason() {
        return reason;
    }

    public Long getId() {
        return id;
    }
}
