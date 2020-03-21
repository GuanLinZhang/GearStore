package com.interconn.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Cart implements Serializable {
    private static final long serialVersionUID = 5062839649405311469L;
    private int id;
    private String openId;
    private int capacity;
    private int status;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date createdTime;
    private String createdUser;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date modifiedTime;
    private String modifiedUser;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

}
