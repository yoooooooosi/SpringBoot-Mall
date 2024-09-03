package com.amber.springbootmall.service;

import com.amber.springbootmall.dto.ProductRequest;
import com.amber.springbootmall.model.Product;

public interface ProductService {

    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
}
