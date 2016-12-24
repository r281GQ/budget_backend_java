package budget.accessories;

import org.springframework.http.HttpStatus;

/**
 * Created by veghe on 21/11/2016.
 */
public class DetailedRestErrorMessage {

    private final HttpStatus httpStatus;

    private final int httpCode;

    private final String reason;

    private final String resource;

    public DetailedRestErrorMessage(HttpStatus httpStatus, String reason, String resource) {
        this.httpStatus = httpStatus;
        this.httpCode = this.httpStatus.value();
        this.reason = reason;
        this.resource = resource;
    }

    public String getReason() {
        return reason;
    }

    public String getResource() {
        return resource;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
