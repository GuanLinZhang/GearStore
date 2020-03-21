package com.interconn.demo.Entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class WechatConfig implements Serializable {
    private static final long serialVersionUID = -6238951045637656123L;

    private String appId;
    private String appSecret;
    private String state;

    public WechatConfig(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


}
