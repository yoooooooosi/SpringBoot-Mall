package com.amber.springbootmall.controller;

import com.amber.springbootmall.constant.ProductCategory;
import com.amber.springbootmall.dto.ProductQueryParams;
import com.amber.springbootmall.dto.ProductRequest;
import com.amber.springbootmall.model.Product;
import com.amber.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;


    //查詢商品列表 (查詢條件、排序、分頁)
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search
            ) {
        //使用@RequestParam取得從前端回傳的參數ProductCategory category(來查看想要查詢的類別) -> required = false 不一定要回傳

        //特別設置一個類別存放需要回傳的參數類別(以避免造成錯誤以及提升優化)
        ProductQueryParams params = new ProductQueryParams();
        params.setCategory(category);
        params.setSearch(search);

        List<Product> productList = productService.getProducts(params);

        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

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

    //修改單筆商品
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId ,
                                                 @RequestBody @Valid ProductRequest productRequest){
        //除了從路徑獲取productId，也要接收前端所回傳需要修改的值

        //先確認該筆資料是否存在
        Product product = productService.getProductById(productId);

        if(product != null){

            productService.updateProduct(productId,productRequest);

            //查詢修改後的商品
            Product updateProduct = productService.getProductById(productId);

            return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }

    //刪除單筆商品
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Integer productId){

        //為什麼刪除商品不用去查詢是否存在 -> 對前端而言只要不存在就好了

        productService.deleteProduct(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
