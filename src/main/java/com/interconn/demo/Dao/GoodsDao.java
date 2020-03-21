package com.interconn.demo.Dao;

import com.interconn.demo.Entity.Goods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsDao {
    List<Goods> findAllObjects(@Param("startIndex") Integer startIndex,
                               @Param("pageSize") Integer pageSize,
                               @Param("goods_name") String goods_name,
                               @Param("category_id") Integer category_id,
                               @Param("status") Integer status,
                               @Param("isRecommended") Integer isRecommended);

    int saveObject(Goods entity);

    int updateObjectByGoodsId(Goods entity);

    int deleteObject(@Param("goods_id") String id);

    int getRowCount(@Param("status") Integer status,
                    @Param("isRecommended") Integer isRecommended,
                    @Param("goods_name") String fuzzyGoodsName,
                    @Param("category_id") Integer categoryId);

    Goods getObjectsByCategoryId(@Param("category_id") Integer category_id);
}
