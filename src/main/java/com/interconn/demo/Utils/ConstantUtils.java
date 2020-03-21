package com.interconn.demo.Utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName: ConstantUtils
 * @Description: (读取配置文件的信息，一些公共的属性，参数配置在)
 * @author zgl
 */
public class ConstantUtils {
    private static final String FILEPATH = "src/main/resources/wechat-config.properties";

    private static ConstantUtils instance;

    private ConstantUtils(){
    }

    public static ConstantUtils getInstance(){
        if(null == instance ){
            instance  = new ConstantUtils();
        }
        return instance;
    }

    /**
     * @Description: (读取文件信息)
     * @author JiWenJian
     * @date 2012-11-22 下午01:42:08
     * @param key
     * @return
     */
    public String getPropertyValue(String key) {
        Properties props = new Properties();
        try {
            InputStream in = ConstantUtils.class.getResourceAsStream(FILEPATH);
            if (in != null) {
                props.load(in);
            } else {
                System.out.println("fuck");
            }
            return props.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description: (读取配置Integer数值信息)
     * @author JiWenJian
     * @date 2012-11-22 下午01:42:08
     * @param key
     * @return
     */
    public Integer getPropertyIntegerValue(String key) {
        Properties props = new Properties();
        try {
            InputStream in = getClass().getResourceAsStream(FILEPATH);
            props.load(in);
            return Integer.parseInt(props.getProperty(key));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
