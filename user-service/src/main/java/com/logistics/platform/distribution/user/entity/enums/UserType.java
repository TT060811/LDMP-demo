package com.logistics.platform.distribution.user.entity.enums;

import lombok.Getter;

@Getter
public enum UserType {

    ADMiN("ADMIN","管理员"),
    CUSTOMER("CUSTOMER", "客户"),
    WAREHOUSE("WAREHOUSE", "仓库管理员"),
    DELIVERYMAN("DELIVERYMAN", "配送员");

    private String code;
    private String desc;


    UserType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 从字符串转换为枚举
    public static UserType fromCode(String code) {
        for (UserType userType : UserType.values()) {
            if (userType.code.equals(code)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Invalid UserType code: " + code);
    }

}
