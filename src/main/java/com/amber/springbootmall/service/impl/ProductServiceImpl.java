package com.amber.springbootmall.service.impl;

import com.amber.springbootmall.dao.ProductDao;
import com.amber.springbootmall.model.Product;
import com.amber.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }
}
