package budget.config.security;

import budget.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;

/**
 * Created by veghe on 04/04/2017.
 */
public class JwtAuthProvider implements AuthenticationProvider {

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthToken jwtAuthenticationToken = (JwtAuthToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        User parsedUser = jwtTokenUtil.parseToken(token);

        if (parsedUser == null) {
            throw new AuthenticationServiceException("JWT token is not valid");
        }

        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(parsedUser.getRole());

        jwtAuthenticationToken = new JwtAuthToken(parsedUser);

        jwtAuthenticationToken.setAuthenticated(true);

        return jwtAuthenticationToken;
    }



    @Override
    public boolean supports(Class<?> authentication) {
        return (authentication.isInstance(new JwtAuthToken(new User())));
    }
}
