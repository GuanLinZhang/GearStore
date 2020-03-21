package com.interconn.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Order implements Serializable {
    private static final long serialVersionUID = -3368941173758160577L;
    private Integer id;
    private String openId;
    private String order_id;
    private Integer is_paid;
    private String address;
    private String telephone;
    private String contact;
    private String post_code;
    private String order_desc;
    private Double total_price;
    private Integer status;
    private WechatUser user;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date createdTime;
    private String createdUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date modifiedTime;
    private String modifiedUser;
    private List<OrderItem> orderItemList;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
