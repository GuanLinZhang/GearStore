package com.interconn.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Category implements Serializable {
    private static final long serialVersionUID = 6839058341158658860L;
    private Integer id;
    private String category_name;
    private String cover_img;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date createdTime;
    private String createdUser;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT-5")
    private Date modifiedTime;
    private String modifiedUser;
    private Integer status;

}
