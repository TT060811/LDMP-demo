package com.logistics.platform.distribution.user.filter;

import com.logistics.platform.distribution.user.entity.User;
import com.logistics.platform.distribution.user.entity.enums.UserStatus;
import com.logistics.platform.distribution.user.repository.UserRepository;
import com.logistics.platform.distribution.user.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * 解耦：不依赖UserService，直接用UserRepository查询用户，切断循环依赖
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // 直接注入Repository和JwtUtil，绕过UserService
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 构造函数注入（仅依赖Repository和JwtUtil，无循环）
    public JwtAuthenticationFilter(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        Long userId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                userId = jwtUtil.getUserIdFromToken(token);
            } catch (Exception e) {
                logger.warn("Token解析失败", e);
                filterChain.doFilter(request, response);
                return;
            }
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 1. 校验Token是否过期
            if (jwtUtil.isTokenExpired(token)) {
                logger.warn("Token已过期：{}", token);
                filterChain.doFilter(request, response);
                return;
            }

            try {
                // 2. 直接从Repository查询用户（复用UserServiceImpl的状态校验逻辑）
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("用户不存在"));

                // 3. 状态校验（与UserServiceImpl完全一致）
                if (user.getStatus() == UserStatus.LOCKED) {
                    throw new RuntimeException("用户被锁定");
                }
                if (user.getStatus() == UserStatus.DISABLED) {
                    throw new RuntimeException("用户被禁用");
                }
                if (user.getStatus() == UserStatus.INACTIVE) {
                    throw new RuntimeException("用户未激活");
                }

                // 4. 构建认证Token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, // 直接用User实体，无需DTO
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserType().getCode()))
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception e) {
                logger.error("用户认证失败", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}