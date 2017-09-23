package com.thit.tibdm.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;


/**
 * 配置中心
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-05-31 11:25
 **/
public interface CC {
    /**
     * 配置
     */
    Config cfg = load();

    /**
     * @return 返回值
     */
    static Config load() {
        Config config = ConfigFactory.load();//扫描加载所有可用的配置文件
        String custom_conf = "thit.conf";//加载自定义配置, 值来自jvm启动参数指定-Dmp.conf
        if (config.hasPath(custom_conf)) {
            File file = new File(config.getString(custom_conf));
            if (file.exists()) {
                Config custom = ConfigFactory.parseFile(file);
                config = custom.withFallback(config);
            }
        }
        return config;
    }

    /**
     * 接口
     */
    interface tibdm {
        /**
         * 配置
         */
        Config cfg = CC.cfg.getObject("tibdm").toConfig();

        /**
         * cassandra接口
         */
        interface cassandra {
            /**
             * 配置
             */
            Config cfg = tibdm.cfg.getObject("cassandra").toConfig();
            /**
             * ip
             */
            int ip = cfg.getInt("ip");
            /***
             * 端口
             */
            int port = cfg.getInt("port");
            /**
             * 密钥
             */
            String keySpace = cfg.getString("keySpaceName");
            /**
             * 类型
             */
            String type = cfg.getString("type");
        }

        /**
         *接口
         */
        interface kafka {
            /**
             * 配置
             */
            Config cfg = tibdm.cfg.getObject("kafka").toConfig();
//            String keyspace = cfg.getList("kafka_ip");
        }
    }
}
