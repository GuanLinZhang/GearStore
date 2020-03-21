package com.interconn.demo.Service;

import com.interconn.demo.Entity.CartItem;
import com.interconn.demo.vo.PageObject;

import java.util.List;

public interface CartService {
    PageObject<CartItem> getCartItem(String openId, Integer pageCurrent) ;

    void addToCart(CartItem newItem);

    void deleteFromCart(List<Integer> idList);

    int updateCartItem(CartItem targetItem);
}
