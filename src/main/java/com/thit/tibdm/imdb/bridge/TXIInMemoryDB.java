package com.thit.tibdm.imdb.bridge;


import com.thit.tibdm.base.InterfaceBase;


/**
 * Created by rmbp13 on 16/9/20.
 */
public interface TXIInMemoryDB extends InterfaceBase {
    /**
     * 根据机器ID和采集类型来获取最新的数据
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @return String
     * @throws Exception 异常
     */
    String getRealTimeData(String object_name, String machine_id, String collect_type) throws Exception;

    /**
     * 批量获取实时数据多条
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machineIds   机器ID数组
     * @return String
     */
    String getRealTimeDataBatch(String object_name, String collect_type, String[] machineIds);

    /**
     * 分别保存数据到内存型数据库和cassandra数据库
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param json         其他属性
     * @return String 字符串
     * @throws Exception 异常
     */
    String save2InMemoryDB(String object_name, String machine_id, String collect_type, String json) throws Exception;

    /**
     * 保存数据到内存型数据库批量
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param jsonList     其他属性
     * @return String 字符串
     * @throws Exception 异常
     */
    String save2InMemoryDBBatch(String object_name, String collect_type, String machine_id, String jsonList) throws Exception;
}
