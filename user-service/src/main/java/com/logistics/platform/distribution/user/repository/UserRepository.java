package com.logistics.platform.distribution.user.repository;

import com.logistics.platform.distribution.user.entity.User;
import com.logistics.platform.distribution.user.entity.enums.UserStatus;
import com.logistics.platform.distribution.user.entity.enums.UserType;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //根据用户名，邮箱，手机号查询用户
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    //更新
    @Modifying
    @Query("UPDATE User u SET u.lastLoginTime = :loginTime, u.lastLoginIp = :loginIp, u.loginFailedCount = 0 WHERE u.id = :userId")
    void updateLoginInfo(@Param("userId") Long userId,@Param("loginTime") LocalDateTime loginTime,@Param("loginIp") String loginIp);

    //更新登录失败次数
    @Modifying
    @Query("UPDATE User u SET u.loginFailedCount = u.loginFailedCount + 1 WHERE u.username = :username")
    void increaseLoginFailedCount(@Param("username") String username);


    //更新状态
    @Modifying
    @Query("UPDATE User u SET u.status = :status, u.lockTime = :lockTime WHERE u.username = :username")
    void updateStatus(@Param("username") String username,@Param("status") String status,@Param("lockTime") LocalDateTime lockTime);

    //根据用户类型，用户状态，用户ID查询用户
    Optional<User> findByUserTypeAndStatusAndId(UserType userType, UserStatus userStatus, Long id);
}
