package com.amber.springbootmall.service.impl;

import com.amber.springbootmall.dao.OrderDao;
import com.amber.springbootmall.dao.ProductDao;
import com.amber.springbootmall.dto.BuyItem;
import com.amber.springbootmall.dto.CreateOrderRequest;
import com.amber.springbootmall.model.Order;
import com.amber.springbootmall.model.OrderItem;
import com.amber.springbootmall.model.Product;
import com.amber.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Transactional //確保資料庫有正確並同時新增成功
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        //計算總價格
        int totalAmount = 0;

        //創建訂單詳細資料需要提供的內容
        List<OrderItem> orderItemList = new ArrayList<>();

        //for( 購買的品項和數量 : 整個購買清單)
        for(BuyItem buyItem :  createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());

            //計算
            int amount = product.getPrice()*buyItem.getQuantity();
            totalAmount += amount;

            //將各項所購買的商品以及內容一一儲存進OrderItem內
            OrderItem orderItem = new OrderItem();
            orderItem.setAmount(amount);
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setProductId(buyItem.getProductId());

            orderItemList.add(orderItem);
        }

        //創建訂單
        Integer orderId = orderDao.createOrder(userId , totalAmount);

        //創建完訂單後，要建立詳細內容
        orderDao.createOrderItems(orderId,orderItemList);

        return orderId;
    }
}
