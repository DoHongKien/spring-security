package com.kiendh.springsecurity.config.jwt;

import com.kiendh.springsecurity.config.security.CustomUserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    @Value("${jwt.secretkey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refreshExpiration}")
    private Long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String genToken(Map<String, Object> extractClaims, CustomUserDetail userDetail) {
        return buildToken(extractClaims, userDetail, expiration);
    }

    public String genRefreshToken(CustomUserDetail userDetail) {
        return buildToken(Map.of(), userDetail, refreshExpiration);
    }

    public boolean isTokenValid(String token, CustomUserDetail userDetails) {
        final String username = extractUsername(token);
        return (username.equals(String.valueOf(userDetails.getId()))) && !isTokenExpired(token);
    }

    private String buildToken(Map<String, Object> extraClaims, CustomUserDetail userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(String.valueOf(userDetails.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(genSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(genSignKey()).build().parseClaimsJws(token).getBody();
    }

    private Key genSignKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
