package com.example.api.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-minutes:30}")
    private long accessMinutes;

    @Value("${jwt.refresh-days:14}")
    private long refreshDays;

    private Key key; // 캐시

    private Key key() {
        if (key == null) {
            if (secret == null || secret.length() < 32) {
                throw new IllegalStateException("jwt.secret must be >= 32 chars");
            }
            key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        return key;
    }

    public String issueAccess(String userId){
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(Duration.ofMinutes(accessMinutes))))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String issueRefresh(String userId){
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(Duration.ofDays(refreshDays))))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndGetSubject(String jwt){
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(jwt).getBody().getSubject();
    }
}