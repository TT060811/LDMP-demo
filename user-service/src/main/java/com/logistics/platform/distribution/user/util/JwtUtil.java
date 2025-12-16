package com.logistics.platform.distribution.user.util;

import com.logistics.platform.distribution.user.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtConfig jwtConfig;

    // 生成Token（修正：setSubject存储userId，与解析对应）
    public String generateToken(Long userId, String username, String userType) {
        Key key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
        return Jwts.builder()
                .setSubject(userId.toString()) // 核心修正：userId存在subject中
                .claim("username", username)   // 扩展信息存在claim中
                .claim("userType", userType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpire()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 解析Token获取Claims（私有方法，内部调用）
    private Claims parseToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 从Token获取用户ID（修正：从subject中获取）
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(parseToken(token).getSubject());
    }

    // 从Token获取用户名
    public String getUsernameFromToken(String token) {
        return parseToken(token).get("username", String.class);
    }

    // 从Token获取用户类型
    public String getUserTypeFromToken(String token) {
        return parseToken(token).get("userType", String.class);
    }

    // 验证Token是否过期
    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    // 新增：验证Token签名是否有效（防止篡改）
    public boolean validateToken(String token) {
        try {
            parseToken(token); // 解析失败会抛异常
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}