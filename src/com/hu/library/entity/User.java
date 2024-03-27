package com.hu.library.entity;

import com.hu.library.enums.UserType;

/**
 * 用户类
 * 包含用户名，密码，用户角色（权限）
 */
public class User {
    // 用户名
    private String userName;
    // 密码
    private String password;
    // 用户类型，枚举
    private UserType userType;
    // 用户状态
    private int status;

    public User(String userName, String password, UserType userType) {
        this.userName = userName;
        this.password = password;
        this.userType = userType;
    }

    public User() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
