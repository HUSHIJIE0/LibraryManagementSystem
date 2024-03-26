package com.hu.library.enums;

// 用户类型枚举
public enum UserType {
    Admin("admin"),
    User("user");

    private final String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    public static UserType fromString(String userType) {
        for (UserType type : UserType.values()) {
            if (type.userType.equalsIgnoreCase(userType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的用户类型：" + userType);
    }
}


