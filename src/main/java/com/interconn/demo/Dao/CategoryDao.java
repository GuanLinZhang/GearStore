package com.interconn.demo.Dao;

import com.interconn.demo.Entity.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao {

    List<Category> findPageObjects(@Param("startIndex") Integer startIndex,
                                   @Param("pageSize") Integer pageSize);

    int saveObject(Category entity);

    int updateObjectById(Category entity);

    int deleteObject(Integer id);

    int getRowCount();

    List<Category> findAllObjects();
}
