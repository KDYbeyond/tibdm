package com.thit.tibdm.calculation;

import com.thit.tibdm.calculation.stru.CollectInfo;
import com.thit.tibdm.calculation.util.DBFactory;
import com.thit.tibdm.calculation.util.SingleConnectOral;
import com.thit.tibdm.util.ResourceUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghaoqiang on 2016/12/13.
 */
public class DBConnection {
    /**
     * 连接
     */
    private Connection connection = SingleConnectOral.INSTANCE.getInstance();

    /**
     *
     * @return 连接
     */
    public Connection getConnection() {
//        String driver=TXISystem.config.getProperty("pool.RDB_TIBDM.driver");
//        String url=TXISystem.config.getProperty("pool.RDB_TIBDM.url");
//        String user=TXISystem.config.getProperty("pool.RDB_TIBDM.user");
//        String passwd=TXISystem.config.getProperty("pool.RDB_TIBDM.password");
        String driver = ResourceUtil.getCassandraProValue("oracle_driver");
        String url = ResourceUtil.getCassandraProValue("oracle_url");
        String user = ResourceUtil.getCassandraProValue("oracle_user");
        String passwd = ResourceUtil.getCassandraProValue("oracle_password");
        connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, passwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn 连接
     *
     */
    public void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                /** 判断当前连接连接对象如果没有被关闭就调用关闭方法*/
                if (!conn.isClosed()) {
                    conn.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


//    public static void main(String[] args) throws SQLException {
//        TXISystem.start();
//        String property = TXISystem.config.getProperty("pool.RDB_TIBDM.driver");
//        System.out.println(property);
//    }


    /**
     * 获取tagnamelist
     *
     * @return 标签
     */
    public static List<String> getTagNameList() {
        List<String> list = new ArrayList<>();
        DBFactory factory = new DBFactory();
        Connection conn = factory.getDBConnectionInstance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT TAGNAME from " + ResourceUtil.getCassandraProValue("oracle_table"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String tagname = rs.getString("TAGNAME");
                list.add(tagname);
            }
            ps.close();
            rs.close();
            factory.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 执行一段sql语句
     *
     * @param sql 语句
     */
    public static void executeSql(String sql) {
        DBFactory factory = new DBFactory();
        Connection conn = factory.getDBConnectionInstance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            factory.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param sql SQL语句
     * @return 返回
     */
    public static List<String> getListBySql(String sql) {
//        DBConnection dbConnection = new DBConnection();
        List<String> list = new ArrayList<>();
//        DBFactory factory = new DBFactory();
        try {
            PreparedStatement ps = SingleConnectOral.INSTANCE.getInstance().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String tagname = rs.getString(1);
//                String tagname = rs.getString("TAGNAME");
                list.add(tagname);
            }
            ps.close();
            rs.close();
//            factory.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取所有的column1
     *
     * @return 列表
     */
    public static List<String> getColumn1List() {
        List<String> list = new ArrayList<>();
        DBFactory factory = new DBFactory();
        Connection conn = factory.getDBConnectionInstance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT COLUMN1 from OPC_TAGINFO");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String column1 = rs.getString("COLUMN1");
                list.add(column1);
            }
            ps.close();
            rs.close();
            factory.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *
     * @param column 列
     * @return 返回
     */
    public static CollectInfo getCollectInfoByColumn1(String column) {
        CollectInfo collectInfo = new CollectInfo();
        DBFactory factory = new DBFactory();
        Connection conn = factory.getDBConnectionInstance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * from OPC_TAGINFO WHERE COLUMN1=" + column);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                collectInfo.setKey(rs.getString("KEY"));
                collectInfo.setColumn1(Long.parseLong(rs.getString("COLUMN1")));
                collectInfo.setValue(rs.getString("VALUE"));
            }
            ps.close();
            rs.close();
            factory.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return collectInfo;
    }


}
