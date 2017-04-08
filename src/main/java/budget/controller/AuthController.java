package budget.controller;

import budget.config.security.AuthRequest;
import budget.config.security.AuthResponse;
import budget.config.security.BudgetUser;
import budget.config.security.JWTTokenUtil;
import budget.controller.exceptions.FailedLogInException;
import budget.model.User;
import budget.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by veghe on 07/04/2017.
 */
@RestController
public class AuthController {

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {

        SecurityContextHolder.getContext().setAuthentication(validate(authRequest));

        User user = loadUserFromDB(authRequest);

        return populateAuthResponse(user);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/signUp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse create(@RequestBody @Valid User user) {

        String originalPassword = user.getPassword();

        userService.create(user);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(user.getEmail());
        authRequest.setPassword(originalPassword);

        SecurityContextHolder.getContext().setAuthentication(validate(authRequest));

        return populateAuthResponse(loadUserFromDB(authRequest));
    }

    private AuthResponse populateAuthResponse(User user) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUser(user);
        authResponse.setToken(jwtTokenUtil.createToken(user));
        return authResponse;
    }

    private User loadUserFromDB(AuthRequest authRequest) {
        return ((BudgetUser) userDetailsService.loadUserByUsername(authRequest.getEmail())).getUser();
    }

    private Authentication validate(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            return authentication;
        } catch (Exception e) {
            throw new FailedLogInException(authRequest.getEmail());
        }
    }
}
