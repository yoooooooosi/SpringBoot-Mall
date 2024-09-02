package com.amber.springbootmall.dao.impl;

import com.amber.springbootmall.dao.ProductDao;
import com.amber.springbootmall.model.Product;
import com.amber.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product getProductById(Integer productId) {

        //使用spring JDBC
        String sql = "select * from product where product_id = :product_id";

        //宣告map
        Map<String, Object> map = new HashMap<>();

        //將sql中的參數代入進去
        map.put("product_id", productId);

        //需要使用mapper去將資料庫中的資料轉換為java object
        List<Product> queryList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if(queryList.size() > 0 ){
            return queryList.get(0);
        } else {
            return null;
        }

    }
}
