package com.interconn.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class OrderItem implements Serializable {
    private static final long serialVersionUID = -6249124875422840976L;
    private Integer id;
    private String order_id;
    private Integer quantity;
    private Double item_price;
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
    private Integer status;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
