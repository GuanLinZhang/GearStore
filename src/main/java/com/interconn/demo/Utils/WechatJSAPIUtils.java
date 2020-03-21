package com.interconn.demo.Utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
  * access_token是公众号的全局唯一接口调用凭据
  * sapi_ticket是公众号用于调用微信JS接口的临时票据
  * access_token的每天调用限额2000次
  * 长期存储access_token
  * */
@Slf4j
public class WechatJSAPIUtils {
    private static Map<String, String> resultMap = new HashMap<>();

    public static Map<String, String> getAccessTokenAndJSTicket() {
        String time = resultMap.get("time");
        String accessToken = resultMap.get("access_token");
        Long nowDate = new Date().getTime();
        if (accessToken != null && time != null && nowDate - Long.parseLong(time) < (1.5*60*60*1000)) {
            log.info("accessToken存在，且没有超时 ， 返回accessTokenMap");
            return resultMap;
        }

        synchronized (WechatJSAPIUtils.class) {
            if(accessToken != null && time != null && nowDate - Long.parseLong(time) < (1.5*60*60*1000)) {
                log.info("accessToken存在，且没有超时 ， 返回accessTokenMap");
                return resultMap;
            }
            log.info("accessToken 超时 ， 或者不存在 ， 重新获取");
            try {
                String access_token= WechatHelper.getAccessToken();////WeiXinUtils见第三步
                log.info("access_token:"+access_token);
                //"这里是直接调用微信的API去直接获取 accessToken 和Jsapi_t  icket 获取";
                String jsapi_ticket = WechatHelper.getTicket(access_token);
                log.info("jsapi_ticket:"+jsapi_ticket);
                //"获取jsapi_token";
                resultMap.put("time", nowDate + "");
                resultMap.put("access_token", access_token);
                resultMap.put("jsapi_ticket", jsapi_ticket);
                log.info("获取的access_token:"+ resultMap.get("access_token"));
                log.info("获取的jsapi_ticket:"+ resultMap.get("jsapi_ticket"));
            } catch (Exception e) {
                log.error("微信服务器发生错误",e);
            }
        }
    return resultMap;
    }
}
