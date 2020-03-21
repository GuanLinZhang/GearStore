package com.interconn.demo.Dao;

import com.interconn.demo.Entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao {
    int saveObject(Order order);

    List<Order> findObject(@Param("openId") String openId,
                           @Param("order_id") String orderId,
                           @Param("is_paid") Integer isPaid,
                           @Param("status") Integer status,
                           @Param("startIndex") Integer startIndex,
                           @Param("pageSize") Integer pageSize,
                           @Param("createdUser") String createdUser);

    int updateObject(Order order);

    int getRowCount(Order order);

    Order findObjectById(@Param("order_id") String orderId);

    void deleteObject(String order_id);
}
