package com.interconn.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WechatUser implements Serializable {
    private static final long serialVersionUID = 3295914350451210112L;
    private Integer id;
    private String openId;
    private String nickName;
    private String headImg;
    private String sex;
    private String cityName;
    private String country;
    private String province;
    private Double integral;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date createdTime;
    private String createdUser;
    private String modifiedUser;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date modifiedTime;
    private String referrer;
    private String QRCode;
    private Integer express_id;
    private String telephone;
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
