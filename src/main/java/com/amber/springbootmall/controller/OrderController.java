package com.amber.springbootmall.controller;

import com.amber.springbootmall.dto.CreateOrderRequest;
import com.amber.springbootmall.model.Order;
import com.amber.springbootmall.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


}
