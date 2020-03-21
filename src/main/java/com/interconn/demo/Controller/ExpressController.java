package com.interconn.demo.Controller;

import com.interconn.demo.Entity.Express;
import com.interconn.demo.Service.ExpressService;
import com.interconn.demo.vo.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/express")
public class ExpressController {
    private final ExpressService expressService;

    @Autowired
    public ExpressController(ExpressService expressService) {
        this.expressService = expressService;
    }

    @GetMapping("get")
    @ResponseBody
    public JSONResponse findAllExpress(@RequestParam("openId") String openId,
                                       @RequestParam(value = "is_selected", required = false) Integer isSelected) {
        List<Express> exList = expressService.findAllExpress(isSelected, openId);
        return new JSONResponse(1, "查询成功", exList);
    }

    @PostMapping("save")
    @ResponseBody
    public JSONResponse saveNewExpress(Express express) {

        int result = expressService.saveNewExpress(express);
        return result == 1 ? new JSONResponse(1, "保存成功", express) :
                new JSONResponse(0, "保存失败");
    }

    @PutMapping("setDefault")
    @ResponseBody
    public JSONResponse setDefault(@RequestParam("openId") String openId,
                                   @RequestParam("express_id") Integer express_id) {
        int result = expressService.setDefaultExpress(openId, express_id);
        return result == 1 ? new JSONResponse(1, "设置成功") :
                new JSONResponse(0, "设置失败");
    }

    @DeleteMapping("delete")
    @ResponseBody
    public JSONResponse deleteExpress(@RequestParam("expressId") Integer expressId) {
        int result = expressService.deleteExpress(expressId);
        return result == 1 ? new JSONResponse(1, "删除成功") :
                new JSONResponse(0, "删除失败");
    }

    @PutMapping("update")
    @ResponseBody
    public JSONResponse updateExpress(Express express) {
        try {

            List<Express> allExpress = expressService.findAllExpress(express.getIs_selected(), express.getOpenId());
            if (allExpress.size() == 0) {
                expressService.saveNewExpress(express);
            }
            expressService.updateExpress(express);
        } catch (RuntimeException e) {
            return new JSONResponse(0, "更新失败", e.getLocalizedMessage());
        }
        return new JSONResponse(1, "更新成功", express);
    }

    @GetMapping("doExpressUI")
    public String doExpressUI() {
        return "/wechat/express.html";
    }
}
