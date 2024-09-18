package com.amber.springbootmall.service;

import com.amber.springbootmall.dto.CreateOrderRequest;
import com.amber.springbootmall.model.Order;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
    Order getOrderById(Integer orderId);
}
