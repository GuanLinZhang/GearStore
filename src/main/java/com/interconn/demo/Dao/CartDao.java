package com.interconn.demo.Dao;

import com.interconn.demo.Entity.GoodsInCart;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao {
    List<GoodsInCart> getCartItemByOpenId(@Param("openId") String openId);
}
