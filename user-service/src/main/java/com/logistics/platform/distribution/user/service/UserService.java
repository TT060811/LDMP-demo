package com.logistics.platform.distribution.user.service;

import com.logistics.platform.distribution.user.dto.LoginRequestDTO;
import com.logistics.platform.distribution.user.dto.LoginResponseDTO;
import com.logistics.platform.distribution.user.dto.UserCreateDTO;
import com.logistics.platform.distribution.user.dto.UserDTO;

public interface UserService {
    /**
     * 用户登录
     */
    LoginResponseDTO login(LoginRequestDTO request, String ip);

    /**
     * 注册用户
     */
    UserDTO register(UserCreateDTO createDTO);

    /**
     * 根据ID查询用户
     */
    UserDTO getUserById(Long id);

    /**
     * 根据用户名查询用户
     */
    UserDTO getUserByUsername(String username);

    /**
     * 刷新Token
     */
    String refreshToken(String oldToken);

    /**
     * 锁定用户
     */
    void lockUser(String username);

    /**
     * 解锁用户
     */
    void unlockUser(String username);
}
