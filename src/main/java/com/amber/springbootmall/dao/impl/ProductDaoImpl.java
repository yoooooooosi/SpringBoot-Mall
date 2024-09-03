package com.amber.springbootmall.dao.impl;

import com.amber.springbootmall.dao.ProductDao;
import com.amber.springbootmall.dto.ProductRequest;
import com.amber.springbootmall.model.Product;
import com.amber.springbootmall.rowmapper.ProductRowMapper;
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

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product(product_name,category,price,stock,description,image_url,created_date,last_modified_date) VALUES(:productName,:category,:price,:stock,:description,:imageUrl,:created_date,:last_modified_date)";

        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().name());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("imageUrl", productRequest.getImageUrl());

        Date now = new Date();
        map.put("created_date", now);
        map.put("last_modified_date", now);

        //當資料庫的id為自動生成的話，可以使用KeyHolder 物件中的GeneratedKeyHolder方法來抓取自動生成的id值
        KeyHolder keyHolder = new GeneratedKeyHolder();

        //使用MapSqlParameterSource()將map轉換為SqlParameterSource使與namedParameterJdbcTemplate.update()兼容
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        //獲取資料庫中自動生成的主鍵，並將她轉型為int
        int productId = keyHolder.getKey().intValue();
        return productId;
    }
}
