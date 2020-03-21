package com.interconn.demo.Controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.interconn.demo.Entity.Admin;
import com.interconn.demo.Entity.WechatUser;
import com.interconn.demo.Service.AdminService;
import com.interconn.demo.Service.WechatService;
import com.interconn.demo.vo.JSONResponse;
import com.interconn.demo.vo.PageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final WechatService wechatService;

    @Autowired
    public AdminController(AdminService adminService, WechatService wechatService) {
        this.adminService = adminService;
        this.wechatService = wechatService;
    }

    @GetMapping("doAdminUI")
    public String doAdminUI() {
        return "/admin/view/template.html";
    }

    @GetMapping("doAdminList")
    public String doAdminList() {
        return "/admin/view/list.html";
    }


    @PostMapping("login")
    @ResponseBody
    public JSONResponse login(@RequestParam("username") String username,
                              @RequestParam("password") String password) {

        Admin tmp = new Admin();
        tmp.setUsername(username);
        tmp.setPassword(DigestUtil.md5Hex(username + DigestUtil.md5Hex(password)));

        return adminService.getUserByUsernamePassword(tmp) != null ?
                new JSONResponse(1, "登录成功") :
                new JSONResponse(0, "用户名或密码错误");
    }

    @PostMapping("register")
    @ResponseBody
    public JSONResponse register(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam(value = "createdUser", required = false) String createdUser) {
        Admin newbie = new Admin();
        newbie.setUsername(username);
        newbie.setPassword(DigestUtil.md5Hex(username + DigestUtil.md5Hex(password)));
        newbie.setCreatedUser(createdUser != null ? createdUser : "root");
        return adminService.register(newbie) == 1 ?
                new JSONResponse(1, "注册成功", newbie) :
                new JSONResponse(0, "注册失败");
    }

    @DeleteMapping("deleteAdmin")
    @ResponseBody
    public JSONResponse delete(@RequestParam("ids") List<String> ids) {
        return adminService.batchDeleteAdminLogicallyById(ids) == 1 ?
                new JSONResponse(1, "删除成功", ids) :
                new JSONResponse(0, "删除失败,不存在此用户");
    }

    @GetMapping("getAdmin")
    @ResponseBody
    public JSONResponse getAdminList(@RequestParam("pageCurrent") Integer pageCurrent,
                                     @RequestParam(value = "username", required = false) String username) {
        PageObject<Admin> pageObject = adminService.findPageAdmin(username, pageCurrent);
        return new JSONResponse(1, "查询成功", pageObject);

    }

    @PutMapping("updateAdmin")
    @ResponseBody
    public JSONResponse updateAdmin(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    @RequestParam("id") Integer id) {
        Admin tmp = new Admin();
        tmp.setUsername(username);
        tmp.setId(id);
        tmp.setPassword(DigestUtil.md5Hex(username + DigestUtil.md5Hex(password)));

        return adminService.updateObject(tmp) == 1 ?
                new JSONResponse(1, "更新成功", tmp) :
                new JSONResponse(0, "更新失败");
    }

    @GetMapping("getWxUserByOpenId")
    @ResponseBody
    public JSONResponse getUserByOpenId(@RequestParam("openId") String openId) {
        WechatUser user = wechatService.findUserByOpenId(openId);
        return user != null ? new JSONResponse(1, "查询成功", user) :
                new JSONResponse(0, "无记录");
    }

    @GetMapping("getWechatUsers")
    @ResponseBody
    public JSONResponse getUsers(@RequestParam("pageCurrent") Integer pageCurrent,
                                 @RequestParam(value = "nickName", required = false) String nickName) {
        WechatUser user = new WechatUser();
        user.setNickName(nickName);
        PageObject<WechatUser> userPages = wechatService.findPageWechatUsers(user, pageCurrent);
        return new JSONResponse(1, "查询成功", userPages);
    }


    @GetMapping("getAdminById")
    @ResponseBody
    public JSONResponse getAdminById(@RequestParam("id") Integer id) {
        return new JSONResponse(1, "查询成功", adminService.findObjectById(id));
    }
}
