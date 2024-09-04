package com.amber.springbootmall.dao;

import com.amber.springbootmall.dto.ProductRequest;
import com.amber.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProduct(Integer productId);
    List<Product> getProducts();
}
