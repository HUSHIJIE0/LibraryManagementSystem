package com.hu.library.repository;

import com.hu.library.entity.User;
import com.hu.library.enums.UserType;

/**
 * 用户持久化层处理接口，提供基础的用户操作定义
 * 具体实现当前为xml文件，可后续扩展数据库等类型，按需注入业务服务层
 */
public interface UserRepository {
    /**
     * 查询用户是否存在
     * @param userName
     * @return
     */
    User queryUser(String userName);

    /**
     * 新增一个用户
     * @param userType
     * @param userName
     * @param password
     * @return
     */
    boolean addUser(UserType userType, String userName, String password);


}
