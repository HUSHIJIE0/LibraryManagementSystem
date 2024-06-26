package com.hu.library.service;

import com.hu.library.enums.UserType;

/**
 * 用户管理类
 */
public interface UserService {
    /**
     * 用户注册
     * @param userType
     * @param userName
     * @param password
     */
    void register(UserType userType, String userName, String password);

    /**
     * 用户登录
     * @param userName
     * @param password
     */
    void login(String userName, String password);

    /**
     * 用户移除、禁用
     * @param userName
     */
    void remove(String userName);
}
