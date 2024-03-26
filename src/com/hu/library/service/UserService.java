package com.hu.library.service;

/**
 * 用户管理类
 */
public interface UserService {
    // 用户注册
    void register();
    // 用户登录
    void login();
    // 用户移除、禁用
    void remove();
}
