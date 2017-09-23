package com.thit.tibdm.calculation.util;

import com.thit.tibdm.calculation.DBConnection;

import java.sql.Connection;

/**
 * @author wanghaoqiang
 */
public class DBFactory {
     
    /** 数据库连接对象*/ 
    private DBConnection dbConn = new DBConnection();
     
    /** 
     * 获取数据库连接对象实例 
     *  
     * @return dbConn
     */ 
    public DBConnection getDBConnectionInstance(){ 
        /** 如果为null就创建一个新的实例化对象且返回*/ 
        if(dbConn==null){ 
            dbConn = new DBConnection(); 
            return dbConn; 
        } 
        /** 如果不为null就直接返回当前的实例化对象*/ 
        else{ 
            return dbConn; 
        } 
    } 
     
    /** 关闭数据库连接
     * @param conn 连接
     * */
    public void closeConnection(Connection conn){
        /** 如果为null就创建一个新的实例化对象*/ 
        if(dbConn==null){ 
            dbConn = new DBConnection(); 
        } 
        dbConn.closeConnection(conn);/** 调用关闭连接的方法*/ 
    } 
     
}