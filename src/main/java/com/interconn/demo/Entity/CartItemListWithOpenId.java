package com.interconn.demo.Entity;

import lombok.Data;

import java.util.List;

@Data
public class CartItemListWithOpenId {
    private List<CartItem> itemList;
    private String openId;
}
