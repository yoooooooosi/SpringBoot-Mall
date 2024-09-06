package com.amber.springbootmall.service.impl;

import com.amber.springbootmall.dao.UserDao;
import com.amber.springbootmall.dto.UserRegisterRequest;
import com.amber.springbootmall.model.User;
import com.amber.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {

        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
}
