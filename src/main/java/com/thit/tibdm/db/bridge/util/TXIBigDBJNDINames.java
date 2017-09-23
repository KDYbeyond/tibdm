package com.thit.tibdm.db.bridge.util;

/**
 * @author 李丽
 * @version 1.0
 */
public interface TXIBigDBJNDINames {

    /**
     * 获取实时大数据类实现类
     */
    String Tibdm_RealTimeData_ImplClass = "Tibdm_RealTimeData_ImplClass";
    /**
     * 获取分段大数据实现类
     */
    String Tibdm_BigDB_ImplClass = "Tibdm_BigDB_ImplClass";
    /**
     * 计算大数据实现类
     */
    String Tibdm_Calculation_ImplClass = "Tibdm_Calculation_ImplClass";
    /**
     * 时间戳字符串
     */
    String TIMESTEP_FORMAT = "1";
    /**
     * 时间字符串
     */
    String STRING_TIME_FORMAT = "2";
    /**
     * 定义查询属性
     */
    String QUERY_ATTRS = "query_attrs";
    /**
     * 协议解析实现类
     */
    String Tibdm_Protocol_ImplClass = "Tibdm_Protocol_ImplClass";

}
