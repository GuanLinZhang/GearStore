package com.interconn.demo.Service.Impl;

import com.interconn.demo.Dao.ExpressDao;
import com.interconn.demo.Entity.Express;
import com.interconn.demo.Service.ExpressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ExpressServiceImpl implements ExpressService {
    private final ExpressDao expressDao;

    @Autowired
    public ExpressServiceImpl(ExpressDao expressDao) {
        this.expressDao = expressDao;
    }

    @Override
    public List<Express> findAllExpress(Integer isSelected, String openId) {
        Express express = new Express();
        express.setIs_selected(isSelected);
        express.setOpenId(openId);
        return expressDao.findAllExpress(express);
    }

    @Override
    @Transactional
    public int saveNewExpress(Express express) {
        int result = 0;
        try {
            result = expressDao.saveNewExpress(express);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public int setDefaultExpress(String openId, Integer expressId) {
        int result = 0;
        try {
            expressDao.removeDefaultExpress(openId);

            Express tmp = new Express();
            tmp.setOpenId(openId);
            tmp.setId(expressId);
            result = expressDao.setDefaultExpress(tmp);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public int deleteExpress(Integer expressId) {
        int result = 0;
        try {
            result = expressDao.deleteExpress(expressId);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public int removeDefaultExpress(String openId) {
        int result = 0;
        try {
            result = expressDao.removeDefaultExpress(openId);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public void updateExpress(Express express) {
        try {
            expressDao.updateExpress(express);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
            throw e;
        }
    }
}
