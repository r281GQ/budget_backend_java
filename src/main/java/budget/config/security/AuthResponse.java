package budget.config.security;

import budget.model.User;

/**
 * Created by veghe on 05/04/2017.
 */
public class AuthResponse {

    private String token;

    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
