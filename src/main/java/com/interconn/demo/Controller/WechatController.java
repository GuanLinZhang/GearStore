package com.interconn.demo.Controller;

import com.interconn.demo.Entity.WechatConfig;
import com.interconn.demo.Entity.WechatUser;
import com.interconn.demo.Exception.AesException;
import com.interconn.demo.Service.WechatService;
import com.interconn.demo.Utils.WechatHelper;
import com.interconn.demo.vo.JSONResponse;
import com.interconn.demo.vo.PageObject;
import com.interconn.demo.vo.WechatMsgEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/")
public class WechatController {
    private final WechatService wechatService;

    @Autowired
    public WechatController(WechatService wechatService) {
        this.wechatService = wechatService;
    }


    /***
     * 微信JSAPI验证,根据access_token换取的jsapi_ticket,
     * 及用其获取到的noncestr,timestamp生成特定String后SHA1()后
     * 生成的signature是否与微信服务器生成的相同,如果相同,则表明验证通过
     *
     * 注意: 其中access_token有效期为20min,每日获取上线2k次,jsapi_ticket同理,
     *       则有必要通过缓存机制进行存储
     *       目前采用的是hashmap机制存储
     * @param request 传来的HttpRequest
     * @return 生成的noncestr, timestamp, jsapi_ticket
     */
    @GetMapping("share")
    @ResponseBody
    public JSONResponse share(@RequestParam("url") String strURL, HttpServletRequest request) {
        WechatMsgEntity wxEntity = WechatHelper.getWechatMessage(strURL);
        return new JSONResponse(1, wxEntity);
    }


    /***
     * 微信用户授权,并取得微信用户信息
     * @param code 微信服务器发来的code,用与和服务器换取access_token,
     *             拿到access_token后再次请求获得用户个 人信息
     * @param state 状态码 微信API标定的参数,暂时没有用到
     * @param referrer 推荐人 微信推荐人OpenID
     */
    @GetMapping("auth")
    @ResponseBody
    public JSONResponse auth(@RequestParam("code") String code,
                             @RequestParam("state") String state,
                             @RequestParam(value = "referrer", required = false) String referrer) {
        WechatConfig config = wechatService.getWechatConfig();
        WechatUser user = wechatService.getWechatProfile(code, config.getAppId(), config.getAppSecret(), referrer);
        return user != null ? new JSONResponse(1, "查询成功", user) :
                new JSONResponse(0, "微信用户授权失败");
    }

    @GetMapping("get")
    @ResponseBody
    public JSONResponse getWechatUser(WechatUser user,
                                      @RequestParam(value = "pageCurrent", required = false) Integer pageCurrent) {
        PageObject<WechatUser> userPageObject = wechatService.findPageWechatUsers(user, pageCurrent);
        return new JSONResponse(1, "查询成功", userPageObject);
    }

    @GetMapping("getByOpenId")
    @ResponseBody
    public JSONResponse getUserByOpenId(@RequestParam("openId") String openId) {
        return new JSONResponse(1, "查询成功", wechatService.findUserByOpenId(openId));
    }

    @PutMapping("update")
    @ResponseBody
    public JSONResponse updateWechatUser(WechatUser user) {
        try {
            wechatService.updateWechatUser(user);
        } catch (RuntimeException e) {
            return new JSONResponse(0, "更新失败", user);
        }
        return new JSONResponse(1, "更新成功", user);
    }

    @DeleteMapping("deleteWechatUser")
    @ResponseBody
    public JSONResponse deleteWechatUser(@RequestParam("ids") List<String> ids) {
        int result = wechatService.batchDeleteWechatUserLogically(ids);
        return result == 1 ? new JSONResponse(1, "删除成功", ids) :
                new JSONResponse(0, "删除失败", ids);
    }

    /***
     * 用户微信服务器配置验证
     * @param request http请求,用于封装微信传来的请求信息
     * @return 判断echostr是否与之前的相同, 如果相同, 则验证通过
     * @throws AesException
     */
    @GetMapping("verify_wx_token")
    @ResponseBody
    public String verifyWXToken(HttpServletRequest request) throws AesException {
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (WechatHelper.verifyUrl(msgSignature, msgTimestamp, msgNonce)) {
            return echostr;
        }
        return null;
    }


    @GetMapping("doWechatUserList")
    public String doWechatUserList() {
        return "/admin/view/wxuser_list.html";
    }

    @GetMapping("index")
    public String index() {
        return "wechat/index.html";
    }

    @GetMapping("doCartUI")
    public String doCartUI() {
        return "wechat/cart.html";
    }

    @GetMapping("doGoodsUI")
    public String doGoodsUI() {
        return "wechat/goods.html";
    }

    @GetMapping("doOrderUI")
    public String doOrderUI() {
        return "wechat/order.html";
    }

    @GetMapping("doMyUI")
    public String doMyUI() {
        return "wechat/my.html";
    }

    @GetMapping("doTransDetail")
    public String doTransDetail() {
        return "wechat/statement.html";
    }

    @GetMapping("doExpressUI")
    public String doExpressUI() {
        return "wechat/express.html";
    }

    @GetMapping("doPayOrderUI")
    public String doPayOrderUI() {
        return "wechat/order2Paid.html";
    }


}
