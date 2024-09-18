package com.amber.springbootmall.service.impl;

import com.amber.springbootmall.dao.OrderDao;
import com.amber.springbootmall.dao.ProductDao;
import com.amber.springbootmall.dao.UserDao;
import com.amber.springbootmall.dto.BuyItem;
import com.amber.springbootmall.dto.CreateOrderRequest;
import com.amber.springbootmall.dto.OrderQueryParams;
import com.amber.springbootmall.model.Order;
import com.amber.springbootmall.model.OrderItem;
import com.amber.springbootmall.model.Product;
import com.amber.springbootmall.model.User;
import com.amber.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Transactional //確保資料庫有正確並同時新增成功
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        //在創建訂單之前，先檢查user是否存在
        User user = userDao.getUserById(userId);

        if(user == null){
            log.warn("該 userId{} 不存在" , userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //計算總價格
        int totalAmount = 0;

        //創建訂單詳細資料需要提供的內容
        List<OrderItem> orderItemList = new ArrayList<>();

        //for( 購買的品項和數量 : 整個購買清單)
        for(BuyItem buyItem :  createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());

            //檢查商品數量是否足夠
            if(product == null) {
                log.warn("該商品 {} 不存在" , buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("該商品 {} 庫存不足，無法購買。剩餘庫存 {}，欲購買數量 {}",
                        buyItem.getProductId(),product.getStock(),buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            //更新庫存數量
            productDao.updateStock(product.getProductId(),product.getStock() - buyItem.getQuantity());


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

    @Override
    public Order getOrderById(Integer orderId) {
        //取得訂單資料
        Order order = orderDao.getOrderById(orderId);

        //取得訂單詳細資料
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        //將訂單與訂單詳細資料合併
        order.setOrderItemList(orderItemList);

        return order;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList  = orderDao.getOrders(orderQueryParams) ;

        //同時要回傳該筆訂單及訂單詳細資料
        for(Order order : orderList){
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);
        }

        return orderList;
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }
}

