package com.interconn.demo.Dao;

import com.interconn.demo.Entity.Express;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpressDao {
    List<Express> findAllExpress(Express express);

    int saveNewExpress(Express express);

    int setDefaultExpress(Express express);

    int deleteExpress(@Param("id") Integer id);

    int removeDefaultExpress(@Param("openId") String openId);

    Express findExpressByOpenId(@Param("openId") String openId);

    void updateExpress(Express express);
}
