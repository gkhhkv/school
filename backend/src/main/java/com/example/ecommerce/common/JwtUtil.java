package com.example.ecommerce.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类，负责 Token 的生成、解析和校验。
 */
public final class JwtUtil {

    private static final String SECRET = "ecommerce-platform-jwt-secret-key-2026";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000L;

    private JwtUtil() {
    }

    /**
     * 生成 JWT Token。
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT 字符串
     */
    public static String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .claims(Map.of("userId", userId, "username", username))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析并校验 Token。
     *
     * @param token JWT 字符串
     * @return Claims
     * @throws JwtException Token 无效或过期
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
