package com.interconn.demo.Service.Impl;

import com.interconn.demo.Dao.CategoryDao;
import com.interconn.demo.Entity.Category;
import com.interconn.demo.Service.CategoryService;
import com.interconn.demo.vo.PageObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }


    @Override
    public PageObject<Category> findPageCategories(Integer pageCurrent) {
        int pageSize = 8;//设置单页显示的数据条目数为9.
        int startIndex = (pageCurrent - 1) * pageSize;//计算获得startIndex用于sql查询
        List<Category> records = categoryDao.findPageObjects(startIndex, pageSize);//获取数据
        int rowCount = categoryDao.getRowCount();//获取总记录数
        int pageCount = rowCount / pageSize; //计算获得总页数

        /**总记录数不能除尽单页显示条目数，则总页数增加一页，用于显示零头信息*/
        if (rowCount % pageSize != 0) {
            pageCount++;
        }

        PageObject<Category> obj = new PageObject<>();//创建PageObject对象用于封装信息
        /**封装信息*/
        obj.setPageCount(pageCount);
        obj.setPageCurrent(pageCurrent);
        obj.setRecords(records);
        obj.setRowCount(rowCount);

        return obj;//返回PageObject对象(到控制层)
    }

    @Override
    @Transactional
    public int saveNewCategory(Category entity) {
        int result = 0;
        try {
            result = categoryDao.saveObject(entity);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public int updateCategoryById(Category entity) {
        int result = 0;
        try {
            result = categoryDao.updateObjectById(entity);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public int deleteCategory(Integer id) {
        int result = 0;
        try {
            result = categoryDao.deleteObject(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryDao.findAllObjects();
    }
}
