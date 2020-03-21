package com.interconn.demo.Service;

import com.interconn.demo.Entity.Category;
import com.interconn.demo.vo.PageObject;

import java.util.List;

public interface CategoryService {
    PageObject<Category> findPageCategories(Integer pageCurrent);
    int saveNewCategory(Category entity);
    int updateCategoryById(Category entity);
    int deleteCategory(Integer id);
    List<Category> findAllCategories();
}
