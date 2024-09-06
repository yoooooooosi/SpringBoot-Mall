package com.amber.springbootmall.dao;

import com.amber.springbootmall.dto.UserRegisterRequest;
import com.amber.springbootmall.model.User;

public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
}
