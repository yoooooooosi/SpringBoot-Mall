package com.amber.springbootmall.dao;

import com.amber.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);
}
