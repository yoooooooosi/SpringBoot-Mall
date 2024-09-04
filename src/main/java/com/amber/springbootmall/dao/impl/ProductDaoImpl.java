package com.amber.springbootmall.dao.impl;

import com.amber.springbootmall.constant.ProductCategory;
import com.amber.springbootmall.dao.ProductDao;
import com.amber.springbootmall.dto.ProductQueryParams;
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
        String sql = "INSERT INTO product(product_name,category,price,stock,description,image_url,created_date,last_modified_date) VALUES(:productName,:category,:price,:stock,:description,:imageUrl,:createdDate,:lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().name());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("imageUrl", productRequest.getImageUrl());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        //當資料庫的id為自動生成的話，可以使用KeyHolder 物件中的GeneratedKeyHolder方法來抓取自動生成的id值
        KeyHolder keyHolder = new GeneratedKeyHolder();

        //使用MapSqlParameterSource()將map轉換為SqlParameterSource使與namedParameterJdbcTemplate.update()兼容
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        //獲取資料庫中自動生成的主鍵，並將她轉型為int
        int productId = keyHolder.getKey().intValue();
        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {

        String sql = "UPDATE product SET product_name = :productName,category = :category,image_url =:imageUrl,price =:price, stock =:stock,description = :description,last_modified_date =:lastModifiedDate WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().name());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);

    }

    @Override
    public void deleteProduct(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {

        //WHERE 1=1 對於查詢結果是沒有影響的，主要是壤後面的sql能夠去做拼接
        String sql = "SELECT * FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        //查詢條件
        if(productQueryParams.getCategory() != null){
            sql = sql + " AND  category = :category";
            map.put("category", productQueryParams.getCategory().name());
        }

        if(productQueryParams.getSearch() != null){
            sql = sql + " AND  product_name  LIKE :search";
            map.put("search", "%" + productQueryParams.getSearch() + "%");
        }

        //排序
        sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        //分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

        List<Product> productList = namedParameterJdbcTemplate.query(sql,map, new ProductRowMapper());


        return productList;
    }

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {

        String sql = "SELECT count(*) FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        //查詢條件
        if(productQueryParams.getCategory() != null){
            sql = sql + " AND  category = :category";
            map.put("category", productQueryParams.getCategory().name());
        }

        if(productQueryParams.getSearch() != null){
            sql = sql + " AND  product_name  LIKE :search";
            map.put("search", "%" + productQueryParams.getSearch() + "%");
        }


        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return total;
    }
}
