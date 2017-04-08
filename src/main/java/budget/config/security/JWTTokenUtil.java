package budget.config.security;

import budget.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTokenUtil {

    private Long expiration = 604800L;

    private String secret = "sssshhhh!";

    public String createToken(User user) {
        return create(user);
    }
    
    private String create(User user){
        Map<String, Object> claims = new HashMap<>();
        
        claims.put("sub", user.getEmail());
        claims.put("audience", "web");
        claims.put("id",user.getIdentifier());
        claims.put("role", user.getRole());
        claims.put("created", generateCurrentDate());

        return generate(claims);
    }

    private String generate(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
    }

    private String generateCurrentDate() {
        return new Date(System.currentTimeMillis()).toString();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    public User parseToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            User user = new User();

            user.setEmail(((String) body.get("sub")));
            user.setIdentifier(Long.parseLong(String.valueOf(body.get("id"))));
            user.setRole((String) body.get("role"));
            return user;

        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }
}