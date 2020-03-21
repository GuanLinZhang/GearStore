package com.interconn.demo.Service.Impl;

import com.interconn.demo.Dao.CartDao;
import com.interconn.demo.Dao.CartItemDao;
import com.interconn.demo.Entity.CartItem;
import com.interconn.demo.Service.CartService;
import com.interconn.demo.vo.PageObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartDao cartDao;
    private final CartItemDao cartItemDao;

    @Autowired
    public CartServiceImpl(CartDao cartDao, CartItemDao cartItemDao) {
        this.cartDao = cartDao;
        this.cartItemDao = cartItemDao;
    }


    @Override
    public PageObject<CartItem> getCartItem(String openId, Integer pageCurrent) {
        Integer pageSize = null;
        Integer startIndex = null;
        Integer pageCount = null;
        Integer rowCount = cartItemDao.getRowCount(openId);

        if (pageCurrent != null) {
            pageSize = 8;//设置单页显示的数据条目数为8.
            startIndex = (pageCurrent - 1) * pageSize;//计算获得startIndex用于sql查询
            /**总记录数不能除尽单页显示条目数，则总页数增加一页，用于显示零头信息*/
            pageCount = rowCount / pageSize; //计算获得总页数
            if (rowCount % pageSize != 0) {
                pageCount++;
            }
        }
        List<CartItem> records = cartItemDao.findPageObjects(openId, startIndex, pageSize);
        PageObject<CartItem> obj = new PageObject<>();//创建PageObject对象用于封装信息
        /**封装信息*/
        obj.setPageCount(pageCount);
        obj.setPageCurrent(pageCurrent);
        obj.setRecords(records);
        obj.setRowCount(rowCount);

        return obj;//返回PageObject对象(到控制层)
    }

    @Override
    @Transactional
    public void addToCart(CartItem newItem) {
        try {
            CartItem cartItem = cartItemDao.findObjectByOpenIdAndGoodsId(newItem.getOpenId(), newItem.getGoods_id());
            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItemDao.updateObject(cartItem);
            } else {
                cartItemDao.saveObject(newItem);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteFromCart(List<Integer> idList) {
        try {
            cartItemDao.deleteObject(idList);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public int updateCartItem(CartItem targetItem) {
        int result = 0;
        try {
            result = cartItemDao.updateObject(targetItem);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

}
