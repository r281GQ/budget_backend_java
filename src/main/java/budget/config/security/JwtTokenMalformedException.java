package budget.config.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by veghe on 05/04/2017.
 */
public class JwtTokenMalformedException extends AuthenticationException {
    public JwtTokenMalformedException(String message) {
        super(message);
    }
}
