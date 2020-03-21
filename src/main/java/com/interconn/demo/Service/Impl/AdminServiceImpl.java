package com.interconn.demo.Service.Impl;

import com.interconn.demo.Dao.AdminDao;
import com.interconn.demo.Entity.Admin;
import com.interconn.demo.Service.AdminService;
import com.interconn.demo.vo.PageObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminDao adminDao;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }


    @Override
    public Admin getUserByUsernamePassword(Admin admin) {
        return adminDao.getUserByUsernamePassword(admin);
    }

    @Override
    public PageObject<Admin> findPageAdmin(String username, Integer pageCurrent) {
        int pageSize = 8;//设置单页显示的数据条目数为8.
        int startIndex = (pageCurrent - 1) * pageSize;//计算获得startIndex用于sql查询
        List<Admin> records = adminDao.findPageObjects(username,startIndex, pageSize);//获取数据
        int rowCount = adminDao.getRowCount(username);//获取总记录数
        int pageCount = rowCount / pageSize; //计算获得总页数

        /**总记录数不能除尽单页显示条目数，则总页数增加一页，用于显示零头信息*/
        if(rowCount % pageSize != 0){
            pageCount++;
        }

        PageObject<Admin> obj = new PageObject<>();//创建PageObject对象用于封装信息
        /**封装信息*/
        obj.setPageCount(pageCount);
        obj.setPageCurrent(pageCurrent);
        obj.setRecords(records);
        obj.setRowCount(rowCount);

        return obj;//返回PageObject对象(到控制层)
    }

    @Override
    @Transactional
    public int batchDeleteAdminLogicallyById(List<String> ids) {
        int result = 0;
        try {
            result = adminDao.deleteObjects(ids);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public int updateByUserNameOrID(Admin admin) {
        return adminDao.updateByUserNameOrID(admin);
    }

    @Override
    @Transactional
    public int register(Admin admin) {
        int result = 0;
        try {
            result = adminDao.saveObject(admin);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public int updateObject(Admin admin) {
        return adminDao.updateObject(admin);
    }

    @Override
    public Admin findObjectById(int id) {
        return adminDao.findObjectById(id);
    }

}
