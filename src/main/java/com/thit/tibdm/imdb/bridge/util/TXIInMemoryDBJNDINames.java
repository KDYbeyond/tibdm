package com.thit.tibdm.imdb.bridge.util;

/**
 * @author 李丽
 * @version 1.0
 */
public interface TXIInMemoryDBJNDINames {
    /**
     * 获取实时大数据类实现类
     */
    String Tibdm_RealTimeData_ImplClass = "Tibdm_RealTimeData_ImplClass";
    /**
     * 获取分段大数据实现类
     */
    String Tibdm_RealTimeData_ImplClass_Default = "com.thit.tibdm.imdb.bridge.impl.TXIRedisImpl";
    /**
     * 获取实时大数据类实现类
     */
    String Tibdm_RealTimeDataBatch_ImplClass = "Tibdm_RealTimeData_ImplClass";
    /**
     * 获取分段大数据实现类
     */
    String Tibdm_RealTimeDataBatch_ImplClass_Default = "com.thit.tibdm.imdb.bridge.impl.TXIRedisImpl";
}
