package com.hu.library.service.impl;

import com.hu.library.entity.User;
import com.hu.library.enums.UserType;
import com.hu.library.repository.UserRepository;
import com.hu.library.server.SessionManager;
import com.hu.library.service.UserService;

public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void register(UserType userType, String userName, String password) {
//        System.out.println("user register!");
        // 用户存在，直接返回提示
        if (userRepository.queryUser(userName) != null) {
            System.out.println("user already exists!");
            return;
        }
        // 用户不存在，新增用户
        if (userRepository.addUser(userType, userName, password)) {
            System.out.println(userType + " " + userName + " successfully registered.");
        }
    }



    @Override
    public void login(String userName, String password) {
        // 查询用户是否存在
        User user = userRepository.queryUser(userName);
        if (user == null) {
            System.out.println("user not exist!");
            return;
        }
        // 验证密码是否争取，密码未加密处理，简单实现
        if (user.getPassword().equals(password)) {
            SessionManager.getInstance().setCurrentUser(user);
            System.out.println(user.getUserType() + " " + userName + " successfully logged in.");
        } else {
            System.out.println("The password is incorrect");
        }
    }

    @Override
    public void remove(String userName) {

    }

}
