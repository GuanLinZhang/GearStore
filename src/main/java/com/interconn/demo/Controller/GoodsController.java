package com.interconn.demo.Controller;

import com.interconn.demo.Entity.Goods;
import com.interconn.demo.Service.GoodsService;
import com.interconn.demo.vo.JSONResponse;
import com.interconn.demo.vo.PageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("get")
    @ResponseBody
    public JSONResponse getAllGoodsByPages(@RequestParam(value = "pageCurrent", required = false) Integer pageCurrent,
                                           @RequestParam(value = "goodsName", required = false) String goodsName,
                                           @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                           @RequestParam(value = "status", required = false) Integer status,
                                           @RequestParam(value = "isRecommended", required = false) Integer isRecommended) {
        PageObject<Goods> resultPage = goodsService.findAllGoods(pageCurrent, categoryId, goodsName, status, isRecommended);
        return new JSONResponse(1, "查询成功", resultPage);
    }

    @PostMapping("save")
    @ResponseBody
    public JSONResponse saveGoods(Goods goods,
                                  @RequestParam("coverImg") MultipartFile coverImg) {
        int result = goodsService.saveGoods(goods, coverImg);
        return result == 1 ? new JSONResponse(1, "保存成功", goods) :
                new JSONResponse(0, "保存失败", goods);
    }

    @PutMapping("update")
    @ResponseBody
    public JSONResponse updateGoods(Goods goods, @RequestParam(value = "coverImg", required = false) MultipartFile coverImg) {
        try {
            goodsService.updateGoodsByGoodsId(goods, coverImg);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONResponse(0, "更新失败,id重复", goods);
        }
        return new JSONResponse(1, "更新成功", goods);
    }

    @DeleteMapping("delete")
    @ResponseBody
    public JSONResponse deleteGoodsByGoodsId(Goods goods) {
        try {
            goodsService.deleteGoodsByGoodsId(goods.getGoods_id());
        } catch (Exception e) {
            new JSONResponse(0, "删除失败", goods);
        }
        return new JSONResponse(1, "删除成功", goods);
    }

    @GetMapping("doGoodsList")
    public String doGoodsUI() {
        return "/admin/view/goods_list.html";
    }
}
