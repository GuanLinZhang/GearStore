package com.interconn.demo.Dao;

import com.interconn.demo.Entity.WechatConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface WechatConfigDao {
    WechatConfig getWechatConfig();
    int setWechatConfig(WechatConfig config);
}
