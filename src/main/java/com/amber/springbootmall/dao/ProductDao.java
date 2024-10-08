package com.amber.springbootmall.dao;

import com.amber.springbootmall.constant.ProductCategory;
import com.amber.springbootmall.dto.ProductQueryParams;
import com.amber.springbootmall.dto.ProductRequest;
import com.amber.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProduct(Integer productId);
    void updateStock(Integer productId, Integer stock);
    List<Product> getProducts(ProductQueryParams productQueryParams);
    Integer countProduct(ProductQueryParams productQueryParams);
}
