package com.thit.tibdm.NettyTransmission.util;

import com.thit.tibdm.util.Config;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by rmbp13 on 16/9/21.
 */
public class ResourceUtil {
    /**
     * @param key 关键字
     * @return String
     */
    public static String getRedisProValue(String key) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("redis-db");
        String type = resourceBundle.getString(key);
        return type;
    }

    /**
     * @param key 关键字
     * @return String
     */
    public static String getCassandraProValue(String key) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("cassandra-db");
        String type = resourceBundle.getString(key);
        return type;
    }

    /**
     * @param args 参数
     */
    public static void main(String[] args) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(args[0]);
        String type = resourceBundle.getString(args[1]);
        System.out.println("取得的名字是" + type);
    }

    /**
     * 获取配置文件中的信息
     *
     * @param fileName 文件名
     * @param key 关键字
     * @return String
     */
    public static String getProValueByNameAndKey(String fileName, String key) {
//        ResourceBundle resourceBundle = ResourceBundle.getBundle(fileName);
//        String type = resourceBundle.getString(key);
        Map config = Config.config;
        //判断是否配置文件已经初始化,如果未初始化,那么就去classpath下面去找配置文件进行初始化
        synchronized (com.thit.tibdm.util.ResourceUtil.class) {
            if (config.isEmpty()) {
                Config.init();
            }
        }
        Map<String, String> o = (Map) Config.config.get(fileName);
        return o.get(key);
    }

}