package com.hu.library.repository;

import com.hu.library.entity.User;
import com.hu.library.enums.UserType;

public interface UserRepository {
    User queryUser(String userName);

    boolean addUser(UserType userType, String userName, String password);


}
