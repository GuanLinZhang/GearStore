package com.interconn.demo.Controller;

import com.interconn.demo.Entity.WechatConfig;
import com.interconn.demo.Service.WechatConfigService;
import com.interconn.demo.vo.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wechatConfig")
public class WechatConfigController {
    private final WechatConfigService wechatConfigService;

    @Autowired
    public WechatConfigController(WechatConfigService wechatConfigService) {
        this.wechatConfigService = wechatConfigService;
    }

    @GetMapping("get")
    @ResponseBody
    public JSONResponse getWechatConfig() {
        return new JSONResponse(1,wechatConfigService.getWechatConfig());
    }

    @PutMapping("set")
    @ResponseBody
    public JSONResponse setWechatConfig(WechatConfig config) {
        int result = wechatConfigService.setWechatConfig(config);
        return new JSONResponse(1,"设置成功", config);
    }

    @GetMapping("doConfigUI")
    public String doConfigUI() {
        return "/admin/view/config.html";
    }

}
