package com.logistics.platform.distribution.waybill.filter;

import com.logistics.platform.distribution.waybill.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }



        String authHeader = request.getHeader("Authorization");
        logger.info("接收到的Authorization：{}", authHeader);
        String token = null;
        Long userId = null;

        // 1. 提取 Token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7).trim();
            try {
                userId = jwtUtil.getUserIdFromToken(token);
                logger.debug("解析Token成功，userId：{}", userId);
            } catch (Exception e) {
                logger.warn("Token解析失败：{}", e.getMessage());
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 2. 验证 Token 并设置认证信息
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 验证 Token 是否过期（保持不变）
            if (jwtUtil.isTokenExpired(token)) {
                logger.warn("Token已过期：{}", token);
                filterChain.doFilter(request, response);
                return;
            }

            // 新增：从 Token 中获取 userType（如 "CUSTOMER"）
            String userType = jwtUtil.getUserTypeFromToken(token);
            logger.debug("解析Token成功，userType：{}", userType);

            // 构建角色：Spring Security 要求角色以 "ROLE_" 为前缀（如 "ROLE_CUSTOMER"）
            String role = "ROLE_" + userType;

            // 分配正确的角色（替换原有的 "ROLE"）
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userId, // 认证主体（用户ID）
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(role)) // 角色为 ROLE_CUSTOMER
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken); // 设置认证信息
        }

        filterChain.doFilter(request, response);
    }
}