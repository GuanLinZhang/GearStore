package com.interconn.demo.Dao;

import com.interconn.demo.Entity.WechatUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Repository
public interface WechatUserDao {

    WechatUser findObjectByOpenId(@Param("openId") String openId);

    String findQRCodeByOpenId(WechatUser user);

    List<WechatUser> findPageObjects(@Param("user") WechatUser user,
                                     @Param("startIndex") Integer startIndex,
                                     @Param("pageSize") Integer pageSize);

    Integer saveObject(WechatUser user);

    Integer deleteObjectsLogicallyById(@Param("ids") List<String> ids);

    Integer updateStatusByOpenid(WechatUser user);

    Integer getRowCount(WechatUser user);

    Double findIntegral(WechatUser user);

    int updateObject(WechatUser user);

    void updateIntegralByOrderId(@Param("payBackIntegral") Double totalPrice, @Param("order_id") String order_id);
}
