package com.amber.springbootmall.dao.impl;

import com.amber.springbootmall.dao.UserDao;
import com.amber.springbootmall.dto.UserRegisterRequest;
import com.amber.springbootmall.model.User;
import com.amber.springbootmall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {

        String sql = "INSERT INTO `user`(email,password,created_date,last_modified_date)" +
                "VALUES(:email,:password,:createdDate,:lastModifiedDate)";

        Map<String , Object> map = new HashMap<>();

        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        //當資料庫的id為自動生成的話，可以使用KeyHolder 物件中的GeneratedKeyHolder方法來抓取自動生成的id值
        KeyHolder keyHolder = new GeneratedKeyHolder();

        //使用MapSqlParameterSource()將map轉換為SqlParameterSource使與namedParameterJdbcTemplate.update()兼容
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int userId = keyHolder.getKey().intValue();

        return userId;
    }

    @Override
    public User getUserById(Integer userId) {

        String sql = "SELECT * FROM `user` WHERE user_id=:userId";

        Map<String , Object> map = new HashMap<>();
        map.put("userId", userId);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if(userList.size()>0){
            return userList.get(0);
        } else {
            return null;
        }

    }

    @Override
    public User getUserByEmail(String email) {

        String sql = "SELECT * FROM `user` WHERE email=:email";
        Map<String , Object> map = new HashMap<>();
        map.put("email", email);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if(userList.size()>0){
            return userList.get(0);
        } else {
            return null;
        }

    }
}
