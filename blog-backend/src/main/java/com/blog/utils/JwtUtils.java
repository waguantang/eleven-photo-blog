package com.blog.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    private final SecretKey key;
    private final long expiration;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtUtils(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expiration}") long expiration,
                    RedisTemplate<String, Object> redisTemplate) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
        this.redisTemplate = redisTemplate;
    }

    public String generateToken(Long userId, String username) {
        String token = Jwts.builder()
                .subject(userId.toString())
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
        // Store in Redis
        redisTemplate.opsForValue().set("token:" + token, userId.toString(), expiration, TimeUnit.MILLISECONDS);
        return token;
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            // Check Redis
            return Boolean.TRUE.equals(redisTemplate.hasKey("token:" + token));
        } catch (JwtException e) {
            return false;
        }
    }

    public void invalidateToken(String token) {
        redisTemplate.delete("token:" + token);
    }
}
