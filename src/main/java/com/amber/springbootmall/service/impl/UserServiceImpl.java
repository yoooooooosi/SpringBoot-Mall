package com.amber.springbootmall.service.impl;

import com.amber.springbootmall.dao.UserDao;
import com.amber.springbootmall.dto.UserLoginRequest;
import com.amber.springbootmall.dto.UserRegisterRequest;
import com.amber.springbootmall.model.User;
import com.amber.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;


@Component
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    //處理有關register問題，不單單只有創建資料
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        //在建立資料之前，要先去檢查
        //要去檢查同一個email不得相同
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if(user != null){
            //log會依照{}順序填入值
            log.warn("該email {} 已經被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        //檢查帳號密碼是正確
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        if(user == null){
            log.warn("該email {} 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if(user.getPassword().equals(userLoginRequest.getPassword())){
            return user;
        } else {
            log.warn("該email {} 密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }
}
