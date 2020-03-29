package com.interconn.demo.Service;


import com.interconn.demo.Entity.Goods;
import com.interconn.demo.vo.PageObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface GoodsService {
    PageObject<Goods> findAllGoods(Integer pageCurrent,Integer category_id, String fuzzyGoodsName, Integer status, Integer isRecommended);

    void saveGoods(Goods entity, MultipartFile file) throws IOException, Exception;

    void updateGoodsByGoodsId(Goods entity, MultipartFile coverImg);

    void deleteGoodsByGoodsId(String id);

    String uploadImage(MultipartFile file);

}

