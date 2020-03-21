package com.interconn.demo.Service;


import com.interconn.demo.Entity.CartItem;
import com.interconn.demo.Entity.Order;
import com.interconn.demo.Exception.OrderException;
import com.interconn.demo.vo.PageObject;

import java.util.List;

public interface OrderService {
    int ORDER_BEEN_PAID = 1;
    Order createOrder(List<CartItem> itemList, String openId);

    PageObject<Order> findPageOrder(Order order, Integer pageCurrent);

    List<Order> findOrder(Order order);
    void updateOrder(Order order) throws Exception;

    int getRowCount(Order order);

    void payOrder(Order order) throws OrderException;

    void deleteOrder(String order_id);

    void payBackToUser(Double totalPrice, String order_id);
}

