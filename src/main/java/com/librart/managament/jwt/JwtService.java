package com.librart.managament.jwt;

import com.librart.managament.config.CustomUserDetails;
import com.librart.managament.config.CustomUserDetailsService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private String SECRET_KEY;
    private long ACCESS_TOKEN_EXPIRATION;
    private long REFRESH_TOKEN_EXPIRATION;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtService(CustomUserDetailsService customUserDetailsService) {
        Dotenv dotenv = Dotenv.load();
        this.customUserDetailsService = customUserDetailsService;

        this.SECRET_KEY = dotenv.get("JWT_SECRET_KEY");
        this.ACCESS_TOKEN_EXPIRATION = Long.parseLong(dotenv.get("JWT_ACCESSTOKEN_EXPIRATION"));
        this.REFRESH_TOKEN_EXPIRATION = Long.parseLong(dotenv.get("JWT_REFRESHTOKEN_EXPIRATION"));
    }

    private Key getSigningKey() {
        if (StringUtils.isEmpty(SECRET_KEY)) {
            throw new IllegalStateException("JWT_SECRET_KEY is missing or empty");
        }
        byte[] decode = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decode);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claims != null ? claimResolver.apply(claims) : null;
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return StringUtils.isNotEmpty(username)
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public boolean validateToken(String token) {
        String username = extractUsername(token);
        if (StringUtils.isNotEmpty(username) && !isTokenExpired(token)) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
            return isTokenValid(token, userDetails);
        }
        return false;
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        GrantedAuthority role = userDetails.getAuthorities()
                .stream().findFirst()
                .orElse(null);
        if (role != null) {
            // yaha pe replace hata do
            claims.put("role", role.getAuthority()); // ROLE_ADMIN as it is
        }
        if (userDetails instanceof CustomUserDetails) {
            String userId = ((CustomUserDetails) userDetails).getId();
            claims.put("userId", userId);
        }
        return createToken(claims, userDetails.getUsername(), ACCESS_TOKEN_EXPIRATION);
    }

    public String refreshToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername(), REFRESH_TOKEN_EXPIRATION);
    }
}
