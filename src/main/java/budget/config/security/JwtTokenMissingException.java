package budget.config.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by veghe on 05/04/2017.
 */
public class JwtTokenMissingException extends AuthenticationException {
    public JwtTokenMissingException(String message) {
        super(message);
    }
}
