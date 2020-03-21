package com.interconn.demo.Dao;

import com.interconn.demo.Entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDao {

    Admin getUserByUsernamePassword(Admin admin);

    List<Admin> findPageObjects(@Param("username") String username,
                                @Param("startIndex") Integer startIndex,
                                @Param("pageSize") Integer pageSize);
    int getRowCount(@Param("username") String username);

    int deleteObjects(@Param("ids") List<String> ids);

    int deleteObjectsLogicallyById(@Param("ids") List<String> ids);

    int updateByUserNameOrID(Admin admin);

    int saveObject(Admin admin);

    int updateObject(Admin admin);

    Admin findObjectById(int id);

}
