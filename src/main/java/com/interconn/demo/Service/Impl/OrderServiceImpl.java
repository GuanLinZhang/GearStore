package com.interconn.demo.Service.Impl;

import cn.hutool.core.util.IdUtil;
import com.interconn.demo.Dao.*;
import com.interconn.demo.Entity.*;
import com.interconn.demo.Service.OrderService;
import com.interconn.demo.Exception.OrderException;
import com.interconn.demo.vo.PageObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final CartItemDao cartItemDao;
    private final WechatUserDao wechatUserDao;
    private final ExpressDao expressDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao,
                            OrderItemDao orderItemDao,
                            CartItemDao cartItemDao,
                            WechatUserDao wechatUserDao,
                            ExpressDao expressDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.cartItemDao = cartItemDao;
        this.wechatUserDao = wechatUserDao;
        this.expressDao = expressDao;
    }

    @Override
    @Transactional
    public Order createOrder(List<CartItem> itemList, String openId) {
        if (itemList.isEmpty()) {
            return null;
        }
        Order newOrder = new Order();
        try {
            //generate OrderId
            String orderId = IdUtil.fastSimpleUUID();
            newOrder.setOrder_id(orderId);
            //设置openId
            newOrder.setOpenId(openId);

            List<OrderItem> orderItemList = new ArrayList<>();
//            List<Integer> cartItemIDList = new ArrayList<>();
            for (CartItem cartItem : itemList) {
                Integer itemId = cartItem.getId();
                Integer quantity = cartItem.getQuantity();
                Double cartItem_price = cartItem.getGoods().getPrice();
                //设置订单商品实体类
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder_id(orderId);
                orderItem.setQuantity(quantity);
                orderItem.setGoods_id(cartItem.getGoods_id());
                //计算订单商品实体类 数量 * 单价
                Double item_price = quantity * cartItem_price;
                orderItem.setItem_price(item_price);
                //添加到订单商品列表
                orderItemList.add(orderItem);
                //叠加到新订单总价
                Double total_price = newOrder.getTotal_price();
                if (total_price != null) {
                    newOrder.setTotal_price(total_price + item_price);
                } else {
                    newOrder.setTotal_price(item_price);
                }
//                cartItemIDList.add(itemId);
            }
            //序列化
            orderItemDao.saveObjects(orderItemList);
            WechatUser user = wechatUserDao.findObjectByOpenId(openId);
            Express userExp = expressDao.findExpressByOpenId(openId);
            if (userExp != null) {
                newOrder.setAddress(userExp.getAddress());
                newOrder.setTelephone(userExp.getTelephone());
                newOrder.setCreatedUser(userExp.getSender_name());
                newOrder.setContact(userExp.getSender_name());
            } else {
                newOrder.setAddress("");
                newOrder.setTelephone("");
                newOrder.setCreatedUser(user.getNickName());
            }
            orderDao.saveObject(newOrder);
//            cartItemDao.deleteObject(cartItemIDList);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
            return null;
        }
        return newOrder;
    }

    @Override
    public PageObject<Order> findPageOrder(Order order, Integer pageCurrent) {
        Integer pageSize = null;
        Integer startIndex = null;
        Integer pageCount = null;
        Integer rowCount = orderDao.getRowCount(order);
        String createdUser = order.getCreatedUser();
        if (pageCurrent != null) {
            pageSize = 8;//设置单页显示的数据条目数为8.
            startIndex = (pageCurrent - 1) * pageSize;//计算获得startIndex用于sql查询
            /**总记录数不能除尽单页显示条目数，则总页数增加一页，用于显示零头信息*/
            pageCount = rowCount / pageSize; //计算获得总页数
            if (rowCount % pageSize != 0) {
                pageCount++;
            }
        }
        List<Order> records = orderDao.findObject(
                order.getOpenId(),
                order.getOrder_id(),
                order.getIs_paid(),
                order.getStatus(),
                startIndex,
                pageSize,
                createdUser);//获取数据
        PageObject<Order> obj = new PageObject<>();//创建PageObject对象用于封装信息
        /**封装信息*/
        obj.setPageCount(pageCount);
        obj.setPageCurrent(pageCurrent);
        obj.setRecords(records);
        obj.setRowCount(rowCount);

        return obj;//返回PageObject对象(到控制层)
    }

    @Override
    public List<Order> findOrder(Order order) {
        return orderDao.findObject(order.getOpenId(), order.getOrder_id(),
                order.getIs_paid(), order.getStatus(), null, null, order.getCreatedUser());
    }


    @Override
    @Transactional
    public void updateOrder(Order order) {
        try {
            orderDao.updateObject(order);
        } catch (RuntimeException e) {
            log.warn(e.getLocalizedMessage());
            throw e;
        }

    }

    @Override
    public int getRowCount(Order order) {
        return orderDao.getRowCount(order);
    }

    @Override
    @Transactional
    public synchronized void payOrder(Order order) throws OrderException {
        String openId = order.getOpenId();
        try {
            WechatUser user = wechatUserDao.findObjectByOpenId(openId);
            if (user == null) {
                throw new OrderException(OrderException.USER_IS_NOT_EXIST);
            }
            Order toBePaid = orderDao.findObjectById(order.getOrder_id());
            if (toBePaid == null) {
                throw new OrderException(OrderException.ORDER_NOT_FOUND);
            }
            if (toBePaid.getIs_paid() == ORDER_BEEN_PAID) {
                throw new OrderException(OrderException.ORDER_IS_PAID);
            }
            Double userIntegral = user.getIntegral();
            double balance = userIntegral - toBePaid.getTotal_price();
            if (userIntegral - toBePaid.getTotal_price() < 0) {
                throw new OrderException(OrderException.INTEGRAL_IS_NOT_ENOUGH);
            }
            WechatUser tmp = new WechatUser();
            tmp.setOpenId(openId);
            tmp.setIntegral(balance);
            wechatUserDao.updateObject(tmp);
            toBePaid.setIs_paid(ORDER_BEEN_PAID);
            orderDao.updateObject(toBePaid);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    public void deleteOrder(String order_id) {
        try {
            orderDao.deleteObject(order_id);
            orderItemDao.deleteObjects(order_id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void payBackToUser(Double totalPrice, String order_id) {
        try {
            wechatUserDao.updateIntegralByOrderId(totalPrice, order_id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
