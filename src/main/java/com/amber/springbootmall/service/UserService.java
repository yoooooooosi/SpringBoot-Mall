package com.amber.springbootmall.service;

import com.amber.springbootmall.dto.UserRegisterRequest;
import com.amber.springbootmall.model.User;

public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
}
