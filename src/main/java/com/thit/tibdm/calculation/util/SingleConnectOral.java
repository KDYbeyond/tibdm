package com.thit.tibdm.calculation.util;

import com.thit.tibdm.util.ResourceUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ORAL的单例连接
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-06-05 15:47
 **/
public enum SingleConnectOral {
    /**
     * 实例
     */
    INSTANCE;
    /**
     * 连接
     */
    private Connection instance;

    SingleConnectOral() {
        String driver = ResourceUtil.getCassandraProValue("oracle_driver");
        String url = ResourceUtil.getCassandraProValue("oracle_url");
        String user = ResourceUtil.getCassandraProValue("oracle_user");
        String passwd = ResourceUtil.getCassandraProValue("oracle_password");
        instance = null;
        try {
            Class.forName(driver);
            instance = DriverManager.getConnection(url, user, passwd);
            instance.setAutoCommit(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getInstance() {
        return instance;
    }
}
