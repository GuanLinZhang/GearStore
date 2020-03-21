package com.interconn.demo.Utils;

import java.util.Arrays;

/***
 * WangEditor只支持特定json
 * 此类专用于封装上传文件后,图片在服务器的资源路径
 */
public class WangEditor {
    private Integer errno; //错误代码，0 表示没有错误。
    private String[] data; //已上传的图片路径

    public WangEditor() {
        super();
    }
    public WangEditor(String[] data) {
        super();
        this.errno = 0;
        this.data = data;
    }
    public Integer getErrno() {
        return errno;
    }
    public void setErrno(Integer errno) {
        this.errno = errno;
    }
    public String[] getData() {
        return data;
    }
    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WangEditor [errno=" + errno + ", data=" + Arrays.toString(data)
                + "]";
    }
}
