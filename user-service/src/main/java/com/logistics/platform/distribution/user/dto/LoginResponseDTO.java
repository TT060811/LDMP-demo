package com.logistics.platform.distribution.user.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponseDTO {
    private String token;               // JWT令牌
    private Long userId;                // 用户ID
    private String username;            // 用户名
    private String realName;            // 真实姓名
    private String userType;            // 用户类型
    private String status;              // 用户状态
    private LocalDateTime lastLoginTime;// 最后登录时间
}


