package com.amber.springbootmall.service;


import com.amber.springbootmall.dto.ProductQueryParams;
import com.amber.springbootmall.dto.ProductRequest;
import com.amber.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProduct(Integer productId);
    List<Product> getProducts(ProductQueryParams productQueryParams);
}
