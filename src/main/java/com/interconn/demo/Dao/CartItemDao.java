package com.interconn.demo.Dao;

import com.interconn.demo.Entity.CartItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemDao {
    List<CartItem> findPageObjects(@Param("openId") String openId,
                                   @Param("startIndex") Integer startIndex,
                                   @Param("pageSize") Integer pageSize);

    int getRowCount(@Param("openId") String openId);

    CartItem findObjectByOpenIdAndGoodsId(@Param("openId") String openId,
                                                @Param("goods_id") String goods_id);

    int saveObject(CartItem newItem);

    int deleteObject(List<Integer> oldItemIDList);

    int updateObject(CartItem targetItem);
}
