package com.logistics.platform.distribution.user.entity.enums;


import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("ACTIVE", "激活"),
    INACTIVE("INACTIVE", "未激活"),
    LOCKED("LOCKED", "锁定"),
    DISABLED("DISABLED", "禁用");

    private final String code;
    private final String desc;

    UserStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserStatus fromCode(String code) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.code.equals(code)) {
                return userStatus;
            }
        }
        throw new IllegalArgumentException("Invalid UserStatus code: " + code);
    }

}
