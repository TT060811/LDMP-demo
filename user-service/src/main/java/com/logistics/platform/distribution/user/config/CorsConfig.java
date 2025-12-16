package com.logistics.platform.distribution.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 跨域资源共享配置
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许前端开发服务器的两个可能地址（127.0.0.1和localhost）
                .allowedOriginPatterns(
                        "http://127.0.0.1:5173",
                        "http://localhost:5173"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 显式指定允许的方法（包含OPTIONS预检请求）
                .allowedHeaders("*") // 允许所有请求头（包含Authorization等）
                .allowCredentials(true) // 允许携带Cookie
                .maxAge(3600); // 预检请求缓存时间（1小时）
    }
}