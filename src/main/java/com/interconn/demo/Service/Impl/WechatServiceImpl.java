package com.interconn.demo.Service.Impl;

import com.interconn.demo.Dao.WechatConfigDao;
import com.interconn.demo.Dao.WechatUserDao;
import com.interconn.demo.Entity.WechatConfig;
import com.interconn.demo.Entity.WechatUser;
import com.interconn.demo.Service.WechatService;
import com.interconn.demo.Utils.WechatHelper;
import com.interconn.demo.vo.PageObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WechatServiceImpl implements WechatService {
    private final WechatUserDao wechatUserDao;
    private final WechatConfigDao configDao;
//    private final RedisTemplate<String, Object> template;

    @Autowired
    public WechatServiceImpl(WechatUserDao wechatUserDao, WechatConfigDao configDao) {
        this.wechatUserDao = wechatUserDao;
        this.configDao = configDao;
//        this.template = template;
    }

    @Override
    public WechatConfig getWechatConfig() {
//        if (!template.hasKey("appId") || !template.hasKey("appSecert")) {
//            WechatConfig config = configDao.getWechatConfig();
//            template.opsForValue().set("appId", config.getAppId());
//            template.opsForValue().set("appSecret", config.getAppSecret());
//        }
//        String appId = (String) template.opsForValue().get("appId");
//        String appSecret = (String) template.opsForValue().get("appSecret");
//        return new WechatConfig(appId, appSecret);
        return new WechatConfig("wx9de8d36f3e4d901e", "0849cd5a39afee37fb921c3bb4e3aefc");
    }

    /***
     * 获取微信用户个人基本信息
     * @param code 当用户登录公众号时,微信服务器发送的代码,用于再次与服务器交互获取Access_Token
     *             仅一次有效,且不能被两次请求共同使用
     * @param appId 微信公众号应用ID
     * @param appSecret 微信公众号应用密令
     * @param referrer 推荐人OpenId
     * @return 个人信息结果Bean
     */
    @Override
    public WechatUser getWechatProfile(String code, String appId, String appSecret, String referrer) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            //使用微信工具类从微信服务器中拿到用户个人信息
            resultMap = WechatHelper.getWechatUserInfo(code, appId, appSecret);
        } catch (Exception e) {
            //与微信服务器交互出现错误
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }

        if (resultMap != null) {
            //获得openid
            String openId = resultMap.get("openId");
            //查询数据库,查询用户是否是第一次登录
            WechatUser user = wechatUserDao.findObjectByOpenId(openId);
            //用户是第一次登录,将数据库存到数据库中
            if (user == null) {
                user = saveWechatUser(resultMap, openId);
            }
            return user;
        }
        //查询失败
        return null;
    }

    @Override
    public WechatUser findUserByOpenId(String openId) {
        return wechatUserDao.findObjectByOpenId(openId);
    }

    @Override
    @Transactional
    public WechatUser saveWechatUser(Map<String, String> wechatResultMap, String openId) {
        //设置初始账号信息
        WechatUser user = new WechatUser();
        user.setOpenId(openId);
        user.setCreatedUser("admin");
//                //设置默认积分
//                user.setIntegral(integralDao.selDefaultIntegral());
//
//                if (referrer.equals("null")) {
//                    referrer = "暂无";
//                }
//
//                user.setReferrer(referrer);
//                //设置受奖励推荐人Bean
//                WxUser awardedUser = new WxUser();
//                //查询推荐人推荐一个人获得多少个积分
//                awardedUser.setIntegral(integralDao.selReferIntegral());
//                //设置推荐人的openid
//                awardedUser.setOpenid(referrer);
//                //更新推荐人积分
//                wxUserDao.addIntegralToReferrer(awardedUser);
        // 生成指定url对应的二维码到文件，宽和高都是300像素
        //todo: change qrCodeLInk
//        String qrCodeLink = "http://www.tjshy.net/wxInformation/index.do?referrer=" + openId;
//        String qrCodePath = "/interconn/QRCode/" + IdUtil.fastSimpleUUID() + ".jpg";
//
//        //提取用户头像
//        String headImageURL = wechatResultMap.get("headImg");
//        //从URL拿到用户头像流
//        BufferedImage image;
//        try {
//            //声明URL
//            URL url = new URL(headImageURL);//url 为图片的URL 地址
//            //读取Image流,并写入到对象中
//            image = ImageIO.read(url);
//        } catch (IOException e) {
//            return null;
//        }

        //初始化二维码配置参数
//        QrConfig config = new QrConfig(300, 300);
        //将用户头像添加到二维码中
//        config.setImg(image);
//        //生成用户二维码
//        QrCodeUtil.generate(qrCodeLink, config, FileUtil.file(qrCodePath));
//        //生成的二维码路径写入到结果Bean中
//        user.setQRCode(qrCodePath);
        //用户不是第一次登录,将从微信服务器传来的用户个人信息写入结果Bean
        user.setNickName(wechatResultMap.get("nickName"));
        user.setHeadImg(wechatResultMap.get("headImg"));
        user.setCityName(wechatResultMap.get("cityName"));
        user.setProvince(wechatResultMap.get("provinceName"));
        user.setCountry(wechatResultMap.get("country"));
        user.setSex(wechatResultMap.get("sex"));
        log.debug(user.toString());
        //保存数据库实例
        int saveResult = 0;
        try {
            saveResult = wechatUserDao.saveObject(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return saveResult == 1 ? user : null;
    }

    @Override
    public String findQRCodeByOpenId(WechatUser user) {
        return wechatUserDao.findQRCodeByOpenId(user);
    }

    @Override
    public PageObject<WechatUser> findPageWechatUsers(WechatUser user, Integer pageCurrent) {
        Integer pageSize = null;
        Integer startIndex = null;
        Integer pageCount = null;
        Integer rowCount = wechatUserDao.getRowCount(user);

        if (pageCurrent != null) {
            pageSize = 8;//设置单页显示的数据条目数为8.
            startIndex = (pageCurrent - 1) * pageSize;//计算获得startIndex用于sql查询
            /**总记录数不能除尽单页显示条目数，则总页数增加一页，用于显示零头信息*/
            pageCount = rowCount / pageSize; //计算获得总页数
            if (rowCount % pageSize != 0) {
                pageCount++;
            }
        }
        List<WechatUser> records = wechatUserDao.findPageObjects(user, startIndex, pageSize);
        PageObject<WechatUser> obj = new PageObject<>();//创建PageObject对象用于封装信息
        /**封装信息*/
        obj.setPageCount(pageCount);
        obj.setPageCurrent(pageCurrent);
        obj.setRecords(records);
        obj.setRowCount(rowCount);

        return obj;//返回PageObject对象(到控制层)
    }

    @Override
    @Transactional
    public int batchDeleteWechatUserLogically(List<String> ids) {
        int result = 0;
        try {
            result = wechatUserDao.deleteObjectsLogicallyById(ids);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public void updateWechatUser(WechatUser user) {
        try {
            wechatUserDao.updateObject(user);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    public Double getIntegral(WechatUser user) {
        return wechatUserDao.findIntegral(user);
    }

}
