package com.interconn.demo.Entity;


import lombok.Data;

@Data
public class Express {
    private Integer id;
    private String openId;
    private String telephone;
    private String address;
    private String post_code;
    private Integer status;
    private Integer is_selected;
    private String sender_name;
}
