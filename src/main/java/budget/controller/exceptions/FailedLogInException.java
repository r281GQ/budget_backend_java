package budget.controller.exceptions;

/**
 * Created by veghe on 24/12/2016.
 */
public class FailedLogInException extends RuntimeException {
    public FailedLogInException(String message) {
        super(message);
    }
}
