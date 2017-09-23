package com.thit.tibdm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置中心
 * Created by wanghaoqiang on 2017/2/21.
 * <p>
 * 加载所有的配置给程序使用
 */
public class Config {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    /**
     * 配置中心
     */
    public static Map config = new HashMap();

    /**
     * 初始化文件
     *
     * @param path 路径
     * @return 返回
     */
    public static void init(String path) {
        Config cfg = new Config();
        //初始化配置文件
        //读取传入的路径,获取路径下面所有的文件名
        File file = new File(path);
        /**
         * 配置文件
         */
        config = cfg.getConfig(file);
    }

    /**
     * 初始化
     */
    public static void init() {
        Config cfg = new Config();
        //初始化配置文件
        //读取传入的路径,获取路径下面所有的文件名
        String path = Config.class.getClassLoader().getResource("").getPath();
        LOGGER.info("配置的路径为：" + path);
        File file = new File(path);
        config = cfg.getConfig(file);
        if (config.size() == 0) {
            file = new File(path + "/conf");
            config = cfg.getConfig(file);
        }
        LOGGER.info(config.toString());
    }


    /**
     * 读取配置文件,并且将配置文件改为map
     *
     * @param file 文件
     * @param name 名字
     * @return 集合
     */
    private Map readPro(File file, String name) {
        Map map = new HashMap<>();
        Properties props = new Properties();
        try {
            InputStream in = new FileInputStream(file);
            props.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        props.forEach((k, v) -> map.put(k, v));
        return map;
    }

    /**
     * 递归的获取目标目录下面所有的配置文件并转成map
     *
     * @param file 文件
     * @return Map 集合
     */
    private Map getConfig(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            Arrays.stream(files).forEach((k) -> {
                if (k.isFile()) {
                    String name = k.getName();
                    if (name.endsWith(".properties")) {
                        Map map = readPro(k, name);
                        String[] split = name.split("\\.");
                        if (!config.containsKey(split[0])) {
                            config.put(split[0], map);
                        } else {
                            LOGGER.warn("存在多份配置文件,请检查");
                        }
                    }
                } else {
                    getConfig(k);
                }
            });
        }
        return config;
    }

    /**
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        init();
        System.out.println(config.toString());
    }


}
