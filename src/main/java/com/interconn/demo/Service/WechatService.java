package com.interconn.demo.Service;

import com.interconn.demo.Entity.WechatConfig;
import com.interconn.demo.Entity.WechatUser;
import com.interconn.demo.vo.PageObject;

import java.util.List;
import java.util.Map;

public interface WechatService {

    WechatConfig getWechatConfig();

    WechatUser getWechatProfile(String code, String appId, String appSecret, String referrer);

    WechatUser findUserByOpenId(String openId);

    String findQRCodeByOpenId(WechatUser user);

    WechatUser saveWechatUser(Map<String,String> wechatResultMap, String openId);

    int batchDeleteWechatUserLogically(List<String> ids);

    PageObject<WechatUser> findPageWechatUsers(WechatUser user, Integer pageCurrent);

    Double getIntegral(WechatUser user);

    void updateWechatUser(WechatUser user);
}
