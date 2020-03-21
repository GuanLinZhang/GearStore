package com.interconn.demo.Controller;

import com.interconn.demo.Entity.CartItem;
import com.interconn.demo.Entity.CartItemListWithOpenId;
import com.interconn.demo.Entity.Order;
import com.interconn.demo.Exception.OrderException;
import com.interconn.demo.Service.OrderService;
import com.interconn.demo.vo.JSONResponse;
import com.interconn.demo.vo.PageObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("save")
    @ResponseBody
    public JSONResponse createOrder(@RequestBody CartItemListWithOpenId itemWithOpenId) {
        List<CartItem> itemList = itemWithOpenId.getItemList();
        String openId = itemWithOpenId.getOpenId();
        Order newOrder = orderService.createOrder(itemList, openId);
        return newOrder != null ? new JSONResponse(1, "订单创建成功", newOrder) :
                new JSONResponse(0, "订单创建失败");
    }

    @GetMapping("get")
    @ResponseBody
    public JSONResponse getOrder(Order order, @RequestParam(value = "pageCurrent", required = false) Integer pageCurrent) {
        PageObject<Order> pageOrder = orderService.findPageOrder(order, pageCurrent);
        return new JSONResponse(1, "查询成功", pageOrder);
    }

    @PostMapping("pay")
    @ResponseBody
    public JSONResponse payOrder(Order needed2PayOrder) {
//        Order neededToPay = new Order();
//        neededToPay.setOrder_id(order_Id);
//        neededToPay.setOpenId(openId);

        try {
            orderService.payOrder(needed2PayOrder);
        } catch (OrderException e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
            return new JSONResponse(0, e.getLocalizedMessage(), needed2PayOrder);
        }

        return new JSONResponse(1, "支付成功", needed2PayOrder);
    }

    @PutMapping("doPayBack")
    @ResponseBody
    public JSONResponse doPayBack(@RequestParam("order_id") String order_id,
                                  @RequestParam("totalPrice") Double totalPrice) {
        try {
            orderService.payBackToUser(totalPrice, order_id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new JSONResponse(0, e.getLocalizedMessage());
        }
        return new JSONResponse(1, "返还成功");
    }

    @DeleteMapping("delete")
    @ResponseBody
    public JSONResponse deleteOrder(@RequestParam("order_id") String order_id) {
        try {
            orderService.deleteOrder(order_id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
            return new JSONResponse(0, e.getLocalizedMessage());
        }
        return new JSONResponse(1, "删除成功", order_id);
    }

    @PutMapping("update")
    @ResponseBody
    public JSONResponse updateOrder(Order order) {
        try {
            orderService.updateOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONResponse(0, "更新失败", e.getLocalizedMessage());
        }
        return new JSONResponse(1, "更新成功", order);
    }

    @GetMapping("doOrderList")
    public String doOrderUI() {
        return "/admin/view/order_list.html";
    }

    @GetMapping("doOrderToPayUI")
    public String doOrder2PayUI() {
        return "/wechat/order2Paid.html";
    }

}

