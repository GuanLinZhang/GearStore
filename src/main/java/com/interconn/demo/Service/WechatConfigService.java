package com.interconn.demo.Service;

import com.interconn.demo.Entity.WechatConfig;

public interface WechatConfigService {
    WechatConfig getWechatConfig();
    int setWechatConfig(WechatConfig config);
}
