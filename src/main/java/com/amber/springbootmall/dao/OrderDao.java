package com.amber.springbootmall.dao;

import com.amber.springbootmall.model.Order;
import com.amber.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {
    Integer createOrder(Integer userId , Integer totalAmount);
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
}
