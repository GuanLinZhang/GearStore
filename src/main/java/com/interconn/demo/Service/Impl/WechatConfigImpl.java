package com.interconn.demo.Service.Impl;

import com.interconn.demo.Dao.WechatConfigDao;
import com.interconn.demo.Entity.WechatConfig;
import com.interconn.demo.Service.WechatConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WechatConfigImpl implements WechatConfigService {
    private final WechatConfigDao wechatConfigDao;

    @Autowired
    public WechatConfigImpl(WechatConfigDao wechatConfigDao) {
        this.wechatConfigDao = wechatConfigDao;
    }

    @Override
    public WechatConfig getWechatConfig() {
        return wechatConfigDao.getWechatConfig();
    }

    @Override
    public int setWechatConfig(WechatConfig config) {
        return wechatConfigDao.setWechatConfig(config);
    }
}
