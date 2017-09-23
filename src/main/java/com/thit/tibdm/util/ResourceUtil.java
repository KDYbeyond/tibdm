package com.thit.tibdm.util;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by rmbp13 on 16/9/21.
 */
public class ResourceUtil {
    /**
     * @param key 关键字
     * @return  RedisProValue
     */
    public static String getRedisProValue(String key) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("redis-db");
        String type = resourceBundle.getString(key);
        return type;
    }

    /**
     * @param key 关键字
     * @return CassandraProValue
     */
    public static String getCassandraProValue(String key) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("cassandra-db");
        String type = resourceBundle.getString(key);
        return type;
    }


    /**
     * 获取配置文件中的信息
     *
     * @param fileName 文件名
     * @param key 关键字
     * @return ProValueByNameAndKey
     */
    public static String getProValueByNameAndKey(String fileName, String key) {
//        ResourceBundle resourceBundle = ResourceBundle.getBundle(fileName);
//        String type = resourceBundle.getString(key);
        Map config = Config.config;
        //判断是否配置文件已经初始化,如果未初始化,那么就去classpath下面去找配置文件进行初始化
        synchronized (ResourceUtil.class) {
            if (config.isEmpty()) {
                Config.init();
            }
        }
        Map<String, String> o = (Map) Config.config.get(fileName);
        return o.get(key);
    }
}