package com.interconn.demo.Controller;

import com.interconn.demo.Entity.CartItem;
import com.interconn.demo.Service.CartService;
import com.interconn.demo.vo.JSONResponse;
import com.interconn.demo.vo.PageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("get")
    @ResponseBody
    public JSONResponse getCartItemByOpenId(@RequestParam("openId") String openId,
                                            @RequestParam(value = "pageCurrent", required = false) Integer pageCurrent) {
        PageObject<CartItem> cartItemList = cartService.getCartItem(openId, pageCurrent);
        return new JSONResponse(1, "查询成功", cartItemList);
    }


    @PostMapping("add")
    @ResponseBody
    public JSONResponse addToCart(CartItem newItem) {
        try {
            cartService.addToCart(newItem);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new JSONResponse(0, e.getLocalizedMessage(), newItem);
        }
        return new JSONResponse(1, "添加成功", newItem);

    }

    @DeleteMapping("delete")
    @ResponseBody
    public JSONResponse deleteFromCart(@RequestParam("idList[]") List<Integer> idList) {
        try {
            cartService.deleteFromCart(idList);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONResponse(0, "删除失败,未知错误");
        }
        return new JSONResponse(1, "删除成功", idList);
    }

    @DeleteMapping("batchDelete")
    @ResponseBody
    public JSONResponse batchDelete() {
        return null;
    }

    @PutMapping("update")
    @ResponseBody
    public JSONResponse updateCartItem(CartItem targetItem) {
        int result = cartService.updateCartItem(targetItem);
        return result == 1 ? new JSONResponse(1, "更新成功", targetItem) :
                new JSONResponse(0, "更新失败,未知错误");
    }
}
