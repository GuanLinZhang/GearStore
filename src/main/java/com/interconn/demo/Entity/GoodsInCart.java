package com.interconn.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GoodsInCart implements Serializable {
    private static final long serialVersionUID = 7496180224447564894L;
    private int cartItemId;
    private String openId;
    private Integer quantity;
    private String goods_id;
    private String goods_name;
    private String goods_desc;
    private String cover_img;
    private Double price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date createdTime;
    private String createdUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date modifiedTime;
    private String modifiedUser;
}
