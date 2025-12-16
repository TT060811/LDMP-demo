package com.logistics.platform.distribution.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类（与application.yml中的jwt配置严格匹配）
 */
@Configuration
@ConfigurationProperties(prefix = "jwt") // 绑定根节点的jwt配置
public class JwtConfig {
    // 1. 密钥（对应配置文件jwt.secret）
    private String secret;
    // 2. 过期时间（对应配置文件jwt.expire，注意属性名从expiration改为expire）
    private Long expire;
    // 3. 请求头名称（对应配置文件jwt.header，补充缺失的属性）
    private String header;

    // ========== getter/setter 必须完整 ==========
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}