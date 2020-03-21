package com.interconn.demo.Service;


import com.interconn.demo.Entity.Express;

import java.util.List;

public interface ExpressService {
    List<Express> findAllExpress(Integer isSelected, String openId);

    int saveNewExpress(Express express);

    int setDefaultExpress(String openId, Integer expressId);

    int deleteExpress(Integer expressId);

    int removeDefaultExpress(String openId);

    void updateExpress(Express express);
}
