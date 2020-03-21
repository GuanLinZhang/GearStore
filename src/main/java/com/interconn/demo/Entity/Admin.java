package com.interconn.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class Admin implements Serializable {
    private static final long serialVersionUID = -5589130252682259258L;
    private Integer id;
    private String username;
    private String password;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date createdTime;
    private String createdUser;
    private String modifiedUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date modifiedTime;
    private int status;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
