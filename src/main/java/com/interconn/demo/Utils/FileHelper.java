package com.interconn.demo.Utils;

import java.util.ArrayList;
import java.util.List;

/***
 * 用来记录文件上传成功次数和文件名的工具类
 *
 * @author zgl
 */
public class FileHelper {
    //成功上传总数
    private int count;
    //上传成功文件名
    private List<String> filenames;

    public FileHelper() {
        filenames = new ArrayList<>();
    }

    //获得成功保存文件名列表
    public String getFileNames() {
        StringBuilder builder = new StringBuilder();
        for (String index : filenames) {
            builder.append(index).append(" ");
        }
        return builder.toString();
    }

    //添加一个成功新文件
    public void add(String filename) {
        filenames.add(filename);
        count++;
    }

    //获得添加文件总数
    public int getCount() {
        return count;
    }
}
