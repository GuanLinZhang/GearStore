package com.interconn.demo.Dao;

import com.interconn.demo.Entity.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemDao {
    List<OrderItem> findObject(OrderItem orderItem);

    int saveObjects(List<OrderItem> orderItemList);

    int deleteObjects(String order_id);

    List<OrderItem> findObjectByOrderId(@Param("order_id") String order_id);
}
