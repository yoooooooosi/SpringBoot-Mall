package com.amber.springbootmall.service;

import com.amber.springbootmall.dto.CreateOrderRequest;
import com.amber.springbootmall.dto.OrderQueryParams;
import com.amber.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
    Order getOrderById(Integer orderId);

    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Integer countOrder(OrderQueryParams orderQueryParams);
}
