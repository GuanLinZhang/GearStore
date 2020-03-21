package com.interconn.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CartItem implements Serializable {
    private static final long serialVersionUID = -1228075187522350165L;
    private Integer id;
    private String openId;
    private Integer quantity;
    private String goods_id;
    private Integer cart_id;
    private Integer status;
//    private double price;
    private Goods goods;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date createdTime;
    private String createdUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date modifiedTime;
    private String modifiedUser;
}
