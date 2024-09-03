package com.amber.springbootmall.controller;

import com.amber.springbootmall.dto.ProductRequest;
import com.amber.springbootmall.model.Product;
import com.amber.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    //查詢單筆商品
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);

        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //新增單筆商品
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        //使用@RequestBody 表示用來接住前端所回傳的參數，並使用@Valid來使ProductRequest中所設定的annotation生效

        //叫productService去創建該資料，並回傳該筆資料的id
        Integer productId = productService.createProduct(productRequest);

        //透過創建好的id進行查詢
        Product product = productService.getProductById(productId);

        //在body放入該筆資料並回傳
        return ResponseEntity.status(HttpStatus.CREATED).body(product);

    }

}
