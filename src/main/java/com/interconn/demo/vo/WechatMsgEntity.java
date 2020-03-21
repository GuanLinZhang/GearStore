package com.interconn.demo.vo;

import lombok.Data;

@Data
public class WechatMsgEntity {
    private String access_token;
    private String ticket;
    private String noncestr;
    private String timestamp;
    private String str;
    private String signature;
}
