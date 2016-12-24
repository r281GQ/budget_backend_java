package budget.config.security;

import budget.controller.exceptions.FailedLogInException;
import budget.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by veghe on 06/11/2016.
 */
@Component
public class BudgetUserService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BudgetUser userDetails = new BudgetUser(userService.getByEmail(username));

        if(userDetails == null)
            throw new FailedLogInException(username);

        return userDetails;
    }
}
