package com.lcwd.electronic.store.security;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@Log4j2
public class JwtHelper {
    public static final long TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    public static final String SECRET_KEY = "yQ5oWzoR5+jEjyTqE6OV1vgnvgUkZlZpNqEvQHYKAV9k4nW5uC7Pvwns7NHktk5RMNqJ9l2Sb6byNWFDVGu+Yw==";

    private SecretKey getSigningKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleNames = userDetails.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        claims.put("roles", roleNames);
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        String token= Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        log.info("Token generated successfully for user: {}", subject);
        return token;

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            log.info("Token validation successful");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimFromToken(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExipred(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

}
