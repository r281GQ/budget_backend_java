package budget.controller.exceptions;

/**
 * Created by veghe on 20/12/2016.
 */
public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(){}

    public UnAuthorizedException(String message){
        super(message);
    }

}
