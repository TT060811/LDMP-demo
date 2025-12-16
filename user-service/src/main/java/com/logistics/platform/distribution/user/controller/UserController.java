package com.logistics.platform.distribution.user.controller;


import com.logistics.platform.distribution.user.dto.LoginRequestDTO;
import com.logistics.platform.distribution.user.dto.LoginResponseDTO;
import com.logistics.platform.distribution.user.dto.UserCreateDTO;
import com.logistics.platform.distribution.user.dto.UserDTO;
import com.logistics.platform.distribution.user.service.UserService;
import com.logistics.platform.distribution.user.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final IpUtil ipUtil;
    //登录
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request, HttpServletRequest httpServletRequest){
        String ip = ipUtil.getIpAddress(httpServletRequest);
        return ResponseEntity.ok(userService.login(request, ip));
    }
    //注册
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserCreateDTO createDTO){
        return ResponseEntity.ok(userService.register(createDTO));
    }
    //根据Id获取用户信息
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
    //根据用户名获取用户信息
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
    //刷新token
    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody String oldToken){
        return ResponseEntity.ok(userService.refreshToken(oldToken));
    }
    //锁定用户
    @PutMapping("/lock/{username}")
    public ResponseEntity<Void> lockUser(@PathVariable String username){
        userService.lockUser(username);
        return ResponseEntity.ok().build();
    }
    //解锁用户
    @PutMapping("/unlock/{username}")
    public ResponseEntity<Void> unlockUser(@PathVariable String username){
        userService.unlockUser(username);
        return ResponseEntity.ok().build();
    }
}
