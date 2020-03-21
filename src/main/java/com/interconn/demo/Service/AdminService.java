package com.interconn.demo.Service;


import com.interconn.demo.Entity.Admin;
import com.interconn.demo.vo.PageObject;

import java.util.List;

public interface AdminService {
    Admin getUserByUsernamePassword(Admin admin);

    PageObject<Admin> findPageAdmin(String username, Integer pageCurrent);

    int batchDeleteAdminLogicallyById(List<String> ids);

    int updateByUserNameOrID(Admin admin);

    int register(Admin admin);

    int updateObject(Admin admin);

    Admin findObjectById(int id);

}
