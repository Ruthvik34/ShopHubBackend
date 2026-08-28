package com.microservice.ecommercewebappapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    public static final long JWT_TOKEN_VALIDITY = 15 * 60 * 1000;   // 15 min

    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; //7 days

    private String secret =
            "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

    // 🔐 Generate Signing Key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 🔎 Extract Username
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 🔎 Extract Expiration
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 🔎 Generic Claim Extractor
    public <T> T getClaimFromToken(String token, Function<Claims, T> resolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return resolver.apply(claims);
    }

    // 🔎 Parse Token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ⏰ Check Expiry
    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // 🔐 Generate Token
    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .claim("type", "ACCESS")
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String username) {

        return Jwts.builder()
                .subject(username)
                .claim("type", "REFRESH")
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + REFRESH_TOKEN_EXPIRATION
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }
    public String getTokenType(String token) {

        return getClaimFromToken(
                token,
                claims -> claims.get("type", String.class)
        );
    }
    // Validate REFRESH TOKEN
    public Boolean validateRefreshToken(String token) {

        String type = getTokenType(token);

        return type.equals("REFRESH")
                && !isTokenExpired(token);
    }

    
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        String type = getTokenType(token);
        return type != null
                && "ACCESS".equals(type)
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }
}