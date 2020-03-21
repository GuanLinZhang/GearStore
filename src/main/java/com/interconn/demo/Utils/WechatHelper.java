package com.interconn.demo.Utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.interconn.demo.Exception.AesException;
import com.interconn.demo.vo.WechatMsgEntity;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/***
 * 此工具类是封装与微信公众号的方法
 *
 * 参考:
 *  https://segmentfault.com/a/1190000013392838
 *
 * @author zgl
 */
@Slf4j
public class WechatHelper {
        /** 微信支付回调支付成功，返回成功结果 */
        public static final String WX_PAY_SUCCESS = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[ok]]></return_msg></xml>";
        /** 微信支付回调支付失败，返回失败结果 */
        public static final String WX_PAY_FAIL = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[result_code is FAIL]]></return_msg></xml>";
        /** 微信支付回调支付sign验证失败，返回sign验证失败结果 */
        public static final String WX_PAY_SIGN_FAIL = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[check signature FAIL]]></return_msg></xml>";
        /** 微信支付回调地址路径 */
        public static final String NOTIFY_URL = "/wxInformation/notifyCallback.do";
        /** 微信获取微信用户授权后用户信息 地址路径 */
        public static final String OAUTH2_USERINFO_URL = "/wxInformation/auth.do";
        /** 微信官方api接口 */
        public static final String URL_OAUTH2 = "https://open.weixin.qq.com/connect/oauth2/authorize?";
        /** 获取微信用户信息 */
        public static final String SCOPE = "snsapi_userinfo";
        /** 交易类型 ：jsapi代表微信公众号支付 */
        public static final String TRADE_TYPE = "JSAPI";

    /** 获取微信openId URL  */
    public String getWxOpenIdUrl(String toUrl){
        ConstantUtils instance = ConstantUtils.getInstance();
        return URL_OAUTH2 +
                "appid=" + instance.getPropertyValue("WX_APPID") +
                "&redirect_uri=" + instance.getPropertyValue("WX_WEB_URL") + OAUTH2_USERINFO_URL +
                "&response_type=code" +
                "&scope=" + SCOPE +
                "&state=" + toUrl +
                "#wechat_redirect";
    }

        /**
         * 获得微信支付随机码
         * @return
         * Date:2017年12月4日上午9:50:48
         * @author 吉文剑
         */
        public static String getNonceStr(){
            return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        }



    /**
     * 获取微信用户信息
     *
     * 基本流程:
     *      1) 根据生成公众号唯一指定的appId和secret,和当用户使用微信客户端登录时微信发送给本服务器的code
     *         去请求微信服务端拿到此用户的openId来作为唯一用户标识(微信端和本服务器端)
     *      2) 拿到openId后,再次向微信服务端发送请求,获得网页授权access_token,默认过期时间7200ms,可调用api刷新
     *      3) 最后拿到access_token和openId再次向服务器请求获得用户个人信息
     *
     * @param code 微信产生的code,只当前访问有效,且不能被同时两个请求一起使用
     * @return 用户数据集: 包含昵称,头像url,性别,地点,openid
     */
    public static Map<String,String> getWechatUserInfo(String code, String appId, String secret) throws Exception {
        //判断服务器传来的code是否为空
        if (StrUtil.isNotBlank(code)){
            JSONObject resultObj = getWebAuthInfo(appId,secret,code);
            //向微信服务器发起请求,获取OpenID
            String openid = resultObj.get("openid").toString();
            //如果成功拿到了openid
            if (StrUtil.isNotBlank(openid)) {
                String access_token = resultObj.get("access_token").toString();
                JSONObject validObj = checkAccessToken(access_token, openid);
                log.info("validObj={}",validObj);
                if ((Integer)validObj.get("errcode") != 0) {
                     JSONObject refreshObj = getAccessTokenByRefreshToken(openid, getRefreshToken(resultObj));
                     log.info("refreshObj={}", refreshObj);
                     access_token = refreshObj.get("access_token").toString();
                }
                log.info("access_token={}", access_token);
                //拼接能够获取微信用户基本信息的url
                String resultUrl2 = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token +"&openid=" + openid +"&lang=zh_CN";
                //向url发送get请求,获得含有用户信息的json格式数据
                String json2 = HttpUtil.get(resultUrl2);
                log.info("wxUserStr={}", json2);
                //实例化用户信息对象
                JSONObject jsonObject2 = JSONUtil.toBean(json2, JSONObject.class);
                log.info("wxUserObj={}",jsonObject2);
                //取得用户信息
                String nickname = jsonObject2.get("nickname").toString();
                String headimgurl = jsonObject2.get("headimgurl").toString();
                //用户没有关注,拒绝访问
                if (nickname == null || headimgurl == null){
                    throw new Exception("未关注,请关注后重试!");
                }
                String sex = jsonObject2.get("sex").toString();
                String country = jsonObject2.get("country").toString();
                String province = jsonObject2.get("province").toString();
                String city = jsonObject2.get("city").toString();
                //声明返回结果Map
                HashMap<String,String> map = new HashMap<>();
                //给结果集赋值,并返回
                map.put("nickName",nickname);
                map.put("country",country);
                map.put("provinceName",province);
                map.put("cityName",city);
                map.put("headImg",headimgurl);
                map.put("openId",openid);
                map.put("sex",sex);
                return map;
            }
        }
        return null;
    }

    /***
     * 获得网页授权Access_Token和openid
     * @param appId appid
     * @param secret 秘钥
     * @param code 用户每次登陆时,微信发送过来的code
     * @return access_token
     */
    public static JSONObject getWebAuthInfo(String appId, String secret, String code) {
        String resultUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ appId +"&secret="+ secret +"&code="+ code +"&grant_type=authorization_code";
        //向url发送get请求,获得含有access_token的json格式数据
        String json = HttpUtil.get(resultUrl);
        log.info("json={}",json);
        //实例化json对象
        return JSONUtil.toBean(json, JSONObject.class);
    }

    /**
     * 检查 AccessToken 是否过期
     * @param access_token
     * @param openid
     * @return
     * @throws Exception
     */
    public static JSONObject checkAccessToken(String access_token, String openid) throws Exception {
        String chickUrl="https://api.weixin.qq.com/sns/auth?access_token="+access_token+"&openid="+openid;
        String jsonString = HttpUtil.get(chickUrl);
        return JSONUtil.toBean(jsonString, JSONObject.class);
    }

    /**
     * 使用Refresh Token来获取Access Token
     * @param openid
     * @param refresh_token
     * @return
     */
    public static JSONObject getAccessTokenByRefreshToken(String openid, String refresh_token) {
        String refreshTokenUrl="https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+openid+"&grant_type=refresh_token&refresh_token="+refresh_token;
        String jsonString = HttpUtil.get(refreshTokenUrl);
        return JSONUtil.toBean(jsonString, JSONObject.class);
    }

    /***
     * 获得refresh_token
     * @param object 传入jsonObject
     * @return refresh_token 字符串
     */
    public static String getRefreshToken(JSONObject object) {
        return object.get("refresh_token").toString();
    }

    /**
     * 验证Token
     * @param msgSignature 签名串，对应URL参数的signature
     * @param timeStamp 时间戳，对应URL参数的timestamp
     * @param nonce 随机串，对应URL参数的nonce
     *
     * @return 是否为安全签名
     * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
     */
    public static boolean verifyUrl(String msgSignature, String timeStamp, String nonce)
            throws AesException {
        // 这里的 WXPublicConstants.TOKEN 填写你自己设置的Token就可以了
        String signature = SHA1.getSHA1(WXPublicConstants.TOKEN, timeStamp, nonce);
        if (!signature.equals(msgSignature)) {
            throw new AesException(AesException.ValidateSignatureError);
        }
        return true;
    }

    public static WechatMsgEntity getWechatMessage(String url) {
        WechatMsgEntity wx = new WechatMsgEntity();
//        String access_token = cache.get("access_token");
        Map<String,String> map = WechatJSAPIUtils.getAccessTokenAndJSTicket();

        String ticket = map.get("jsapi_ticket");
        Map<String, String> ret = Sign.sign(ticket, url);
        wx.setTicket(ret.get("jsapi_ticket"));
        wx.setSignature(ret.get("signature"));
        wx.setNoncestr(ret.get("nonceStr"));
        wx.setTimestamp(ret.get("timestamp"));
        log.info("wx_share_info={}", wx);
        return wx;
    }


    //获取token
    public static String getAccessToken() {
//        String access_token = cache.get("access_token");
//        if (access_token != null) {
//            return access_token;
//        }
        String access_token = "";
        String grant_type = "client_credential";//获取access_token填写client_credential
        //todo: 设置appid 和 secret
        String AppId="wx9de8d36f3e4d901e";//第三方用户唯一凭证
        String secret="0849cd5a39afee37fb921c3bb4e3aefc";//第三方用户唯一凭证密钥，即appsecret
        //这个url链接地址和参数皆不能变
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+AppId+"&secret="+secret;  //访问链接

        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            /*System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒 */
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, StandardCharsets.UTF_8);
            JSONObject demoJson = JSONUtil.toBean(message, JSONObject.class);
            log.info("demoJson={}", demoJson);
            access_token = (String) demoJson.get("access_token");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }



    //获取jsapi_ticket
    public static String getTicket(String access_token) {
//        String ticket = cache.get("jsapi_ticket");
////        if (ticket != null) {
////            return ticket;
////        }
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ access_token +"&type=jsapi";//这个url链接和参数不能变
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, StandardCharsets.UTF_8);
            JSONObject demoJson = JSONUtil.toBean(message,JSONObject.class);
            log.info("wx_jsticket={}", demoJson);
            ticket = (String) demoJson.get("ticket");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }

}
