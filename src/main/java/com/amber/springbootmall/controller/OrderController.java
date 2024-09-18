package com.amber.springbootmall.controller;

import com.amber.springbootmall.dto.CreateOrderRequest;
import com.amber.springbootmall.dto.OrderQueryParams;
import com.amber.springbootmall.model.Order;
import com.amber.springbootmall.service.OrderService;
import com.amber.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    //創建訂單(建立訂單的前提須要有userId -> 帳號存在的情況下)
    @PostMapping("users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId ,
                                             @RequestBody @Valid CreateOrderRequest createOrderRequest){

        Integer orderId = orderService.createOrder(userId,createOrderRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    //查詢訂單
    @GetMapping("users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @PathVariable Integer userId,
            //分頁
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,//取得幾筆數據
            @RequestParam(defaultValue = "0") @Min(0) Integer offset//跳過多少筆資料
    ){
        //特別設置一個類別存放需要回傳的參數類別(以避免造成錯誤以及提升優化)
        OrderQueryParams params = new OrderQueryParams();
        params.setUserId(userId);
        params.setLimit(limit);
        params.setOffset(offset);

        //取得Order List
        List<Order> orderList = orderService.getOrders(params);

        //取得order總數
        Integer count = orderService.countOrder(params);

        //分頁
        Page<Order> page = new Page<>();
        page.setTotal(count);
        page.setLimit(limit);
        page.setOffset(offset);
        page.setResults(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);

    }


}
