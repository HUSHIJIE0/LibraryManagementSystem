package com.hu.library.entity;

import com.hu.library.enums.UserType;

/**
 * 用户类
 * 包含用户名，密码，用户角色（权限）
 */
public class User {
    // 用户名
    private String username;
    // 密码
    private String password;
    // 用户类型，枚举
    private UserType userType;
    // 用户状态
    private int status;

    public User(String username, String password, UserType userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
