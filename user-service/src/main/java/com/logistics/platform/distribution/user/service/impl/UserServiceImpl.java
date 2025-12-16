package com.logistics.platform.distribution.user.service.impl;

import com.logistics.platform.distribution.user.dto.LoginRequestDTO;
import com.logistics.platform.distribution.user.dto.LoginResponseDTO;
import com.logistics.platform.distribution.user.dto.UserCreateDTO;
import com.logistics.platform.distribution.user.dto.UserDTO;
import com.logistics.platform.distribution.user.entity.User;
import com.logistics.platform.distribution.user.entity.enums.UserStatus;
import com.logistics.platform.distribution.user.entity.enums.UserType;
import com.logistics.platform.distribution.user.repository.UserRepository;
import com.logistics.platform.distribution.user.service.UserService;
import com.logistics.platform.distribution.user.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 最大登录失败次数
    private static final int MAX_LOGIN_FAILED_COUNT = 5;
    // 账号锁定时长（分钟）
    private static final int Lock_MINUTES = 1;



    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request, String ip) {
        //校验
        if (!StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            throw new RuntimeException("用户名或密码不能为空");
        }
        //查询用户
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        //校验用户状态
        if(user.getStatus()== UserStatus.LOCKED){
            if (user.getLockTime()!=null&&user.getLockTime().plusMinutes(Lock_MINUTES).isBefore(LocalDateTime.now())){
                user.setStatus(UserStatus.ACTIVE);
                user.setLockTime( null);
                user.setLoginFailedCount( 0);
                userRepository.save(user);
            }else {
                throw new RuntimeException("用户被锁定");
            }
        }
        if (user.getStatus()==UserStatus.DISABLED){
            throw new RuntimeException("用户被禁用");
        }
        if (user.getStatus()==UserStatus.INACTIVE){
            throw new RuntimeException("用户未激活");
        }
        //校验密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            //登录失败次数加1
            userRepository.increaseLoginFailedCount(request.getUsername());
            int loginFailedCount = user.getLoginFailedCount()+1;
            //次数过多时锁定账号
            if (loginFailedCount >= MAX_LOGIN_FAILED_COUNT) {
                userRepository.updateStatus(request.getUsername(), UserStatus.LOCKED.getCode(), LocalDateTime.now());
                throw new RuntimeException("密码错误次数过多，用户被锁定");
            }
            throw new RuntimeException("密码错误，剩余"+(MAX_LOGIN_FAILED_COUNT-loginFailedCount)+"次机会");
        }
        //更新登录信息
        LocalDateTime now = LocalDateTime.now();
        userRepository.updateLoginInfo(user.getId(), now, ip);
        //生成JWT令牌
        String token = jwtUtil.generateToken(user.getId(),user.getUsername(), user.getUserType().getCode());
        //构造响应
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setUserType(user.getUserType().getCode());
        response.setStatus(user.getStatus().getCode());
        response.setLastLoginTime(now);

        return response;
    }

    @Override
    public UserDTO register(UserCreateDTO createDTO) {
        //校验用户名，手机号，邮箱是否存在
        if (userRepository.findByUsername(createDTO.getUsername()).isPresent()){
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.findByPhone(createDTO.getPhone()).isPresent()){
            throw new RuntimeException("手机号已存在");
        }
        if (userRepository.findByEmail(createDTO.getEmail()).isPresent()){
            throw new RuntimeException("邮箱已存在");
        }
        //构造用户
        User user = new User();
        user.setUsername(createDTO.getUsername());
        user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        user.setPhone(createDTO.getPhone());
        user.setEmail(createDTO.getEmail());
        user.setRealName(createDTO.getRealName());
        user.setUserType(UserType.fromCode(createDTO.getUserType()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRegisterTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        //保存用户
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);

    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return convertToDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return convertToDTO(user);
    }

    @Override
    public String refreshToken(String oldToken) {
        //解析 Token
        Long userId = jwtUtil.getUserIdFromToken(oldToken);
        String username = jwtUtil.getUsernameFromToken(oldToken);
        String userType = jwtUtil.getUserTypeFromToken(oldToken);
        //检验用户是否存在
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        //生成新的 Token
        return jwtUtil.generateToken(userId,username, userType);
    }

    @Override
    public void lockUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setStatus(UserStatus.LOCKED);
        user.setLockTime(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void unlockUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setStatus(UserStatus.ACTIVE);
        user.setLockTime(null);
        user.setLoginFailedCount(0);
        userRepository.save(user);
    }
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());                  // 用户ID
        dto.setUsername(user.getUsername());      // 登录账号
        dto.setPhone(user.getPhone());            // 手机号
        dto.setEmail(user.getEmail());            // 邮箱
        dto.setRealName(user.getRealName());      // 真实姓名
        dto.setUserType(user.getUserType().getCode()); // 用户类型（ADMIN/CUSTOMER等）
        dto.setStatus(user.getStatus().getCode());     // 用户状态（ACTIVE/LOCKED等）
        dto.setLastLoginTime(user.getLastLoginTime()); // 最后登录时间
        dto.setRegisterTime(user.getRegisterTime());   // 注册时间
        return dto;
    }
}
