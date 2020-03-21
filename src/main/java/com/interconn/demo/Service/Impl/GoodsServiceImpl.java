package com.interconn.demo.Service.Impl;

import cn.hutool.core.util.IdUtil;
import com.interconn.demo.Dao.GoodsDao;
import com.interconn.demo.Entity.Goods;
import com.interconn.demo.Service.GoodsService;
import com.interconn.demo.vo.PageObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {
    private final GoodsDao goodsDao;

    @Autowired
    public GoodsServiceImpl(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }


    @Override
    public PageObject<Goods> findAllGoods(Integer pageCurrent, Integer category_id, String fuzzyGoodsName,
                                          Integer status, Integer isRecommended) {
        Integer pageSize = null;
        Integer startIndex = null;
        Integer pageCount = null;
        Integer rowCount = goodsDao.getRowCount(status, isRecommended, fuzzyGoodsName, category_id);
        if (pageCurrent != null) {
            pageSize = 8;//设置单页显示的数据条目数为8.
            startIndex = (pageCurrent - 1) * pageSize;//计算获得startIndex用于sql查询
            /**总记录数不能除尽单页显示条目数，则总页数增加一页，用于显示零头信息*/
            pageCount = rowCount / pageSize; //计算获得总页数
            if (rowCount % pageSize != 0) {
                pageCount++;
            }
        }
        List<Goods> records = goodsDao.findAllObjects(
                startIndex, pageSize, fuzzyGoodsName,
                category_id, status, isRecommended);
        PageObject<Goods> obj = new PageObject<>();//创建PageObject对象用于封装信息
        /**封装信息*/
        obj.setPageCount(pageCount);
        obj.setPageCurrent(pageCurrent);
        obj.setRecords(records);
        obj.setRowCount(rowCount);

        return obj;//返回PageObject对象(到控制层)
    }


    @Override
    @Transactional
    public int saveGoods(Goods entity, MultipartFile file) {
        int result = 0;
        entity.setGoods_id(IdUtil.fastSimpleUUID());
        try {
            String imgUrl = uploadImage(file);
            entity.setCover_img(imgUrl);
            result = goodsDao.saveObject(entity);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public void updateGoodsByGoodsId(Goods entity, MultipartFile coverImg) {
        try {
            if (coverImg != null) {
                String imgUrl = uploadImage(coverImg);
                entity.setCover_img(imgUrl);
            }
            goodsDao.updateObjectByGoodsId(entity);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteGoodsByGoodsId(String id) {
        try {
            goodsDao.deleteObject(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    public String uploadImage(MultipartFile file) {
        String imgURL = null;
        try {
            imgURL = saveSingleFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgURL;
    }

    /***
     * 保存单个文件
     * @param uploadedFile 待上传文件
     * @return 状态码
     */
    private String saveSingleFile(MultipartFile uploadedFile) throws IOException {
        //获得文件完全名
        String fileFullName = uploadedFile.getOriginalFilename();
        String fileName = fileFullName.substring(0, fileFullName.lastIndexOf("."));
        //获得文件扩展名
        String fileExtName = fileFullName.substring(fileFullName.lastIndexOf("."));
        fileExtName = fileExtName.toLowerCase();

        if (!fileExtName.equals(".jpg") && !fileExtName.equals(".jpeg") && !fileExtName.equals(".png")) {
            return null;
        }

        String uuid = UUID.randomUUID().toString();
        //拼接待保存文件路径
        String resourcesPath = "/img/";
        String filePath = resourcesPath + uuid + fileExtName;
        System.out.println("///////////文件保存路径///////////////");
        System.out.println(filePath);
        //保存文件到硬盘
        try {
            FileUtils.copyInputStreamToFile(uploadedFile.getInputStream(), new File(filePath));
        } catch (IOException e) {
            //保存失败
            throw new IOException();
        }

        return filePath;
    }


}
