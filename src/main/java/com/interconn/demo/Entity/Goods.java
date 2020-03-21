package com.interconn.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Goods implements Serializable {
    private Integer id;
    private String goods_id;
    private String goods_name;
    private String goods_desc;
    private String cover_img;
    private Double price;
    private String createdUser;
    private Integer category_id;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date createdTime;
    private String modifiedUser;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date modifiedTime;
    private Integer status;
    private Integer isRecommended;
    private static final long serialVersionUID = -5337476048864822539L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
