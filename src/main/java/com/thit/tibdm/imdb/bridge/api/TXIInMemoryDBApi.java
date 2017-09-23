package com.thit.tibdm.imdb.bridge.api;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.imdb.bridge.TXIInMemoryDBFactory;
import com.thit.tibdm.imdb.bridge.util.TXIInMemoryDBJNDINames;
import com.xicrm.common.TXISystem;

import java.util.Arrays;

/**
 * @author wanghaoqiang
 */
public class TXIInMemoryDBApi {
    public TXIInMemoryDBApi() {

    }

    /**
     * 根据机器ID和采集类型来获取最新的数据
     *
     * @param object_name 名字
     * @param collect_type 类型
     * @param machine_id id
     * @return String 字符串
     * @throws Exception 异常
     */
    public String getRealTimeData(String object_name, String machine_id, String collect_type) throws Exception {
        String result;

        try {

            String impl_classname = TXISystem.config
                    .getProperty(
                            TXIInMemoryDBJNDINames.Tibdm_RealTimeData_ImplClass,
                            TXIInMemoryDBJNDINames.Tibdm_RealTimeData_ImplClass_Default);
            result = TXIInMemoryDBFactory.getInst(impl_classname)
                    .getRealTimeData(object_name, machine_id, collect_type);
            return result;
        } catch (Exception e) {
            throw stdException(e);
        }
    }

    /**
     *
     * @param object_name 名字
     * @param machine_id id
     * @param collect_type 类型
     * @param json json
     * @return 返回值
     * @throws Exception 异常
     */
    public boolean save2InMemoryDB(String object_name, String machine_id, String collect_type, String json) throws Exception {
        // 需要向redis和cassandra里面写入信息
        // redis
        String redisResult;
        try {
            String impl_classname = TXISystem.config.getProperty(
                    TXIInMemoryDBJNDINames.Tibdm_RealTimeData_ImplClass,
                    TXIInMemoryDBJNDINames.Tibdm_RealTimeData_ImplClass_Default);
            redisResult = TXIInMemoryDBFactory.getInst(impl_classname)
                    .save2InMemoryDB(object_name, machine_id, collect_type, json);
        } catch (Exception e) {
            throw stdException(e);
        }

        if (redisResult.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param e 异常
     * @return 返回值
     */
    private Exception stdException(Exception e) {
        String errmsg = "实时数据接口出错！" + e.toString();
        TXISystem.log.error(this, errmsg, e);
        return e;
    }

    /**
     *
     * @param object_name 名字
     * @param collect_type 类型
     * @param list 列表
     * @return 返回值
     * @throws Exception 异常
     */
    public String getRealTimeDataBatch(String object_name, String collect_type, String list) throws Exception {
        String result;
        String[] strs = Arrays.asList(JSON.parseArray(list).toArray()).toArray(new String[0]);
        System.out.println(object_name + collect_type + list + "=======================");
        String impl_classname = TXISystem.config.getProperty(
                TXIInMemoryDBJNDINames.Tibdm_RealTimeDataBatch_ImplClass,
                TXIInMemoryDBJNDINames.Tibdm_RealTimeDataBatch_ImplClass_Default);

        try {
            result = TXIInMemoryDBFactory.getInst(impl_classname)
                    .getRealTimeDataBatch(object_name, collect_type, strs);
        } catch (Exception e) {
            throw stdException(e);
        }
        return result;
    }

    /**
     *
     * @param object_name 名字
     * @param machine_id id
     * @param collect_type 类型
     * @param jsonList 列表
     * @return 返回
     * @throws Exception 异常
     */
    public boolean save2InMemoryDBBatch(String object_name, String machine_id, String collect_type, String jsonList) throws Exception {
        // 需要向redis和cassandra里面写入信息
        // redis
        String redisResult;
        String impl_classname = TXISystem.config.getProperty(
                TXIInMemoryDBJNDINames.Tibdm_RealTimeData_ImplClass,
                TXIInMemoryDBJNDINames.Tibdm_RealTimeData_ImplClass_Default);
        try {
            redisResult = TXIInMemoryDBFactory.getInst(impl_classname)
                    .save2InMemoryDBBatch(object_name, machine_id, collect_type, jsonList);
        } catch (Exception e) {
            throw stdException(e);
        }
        if (redisResult.equals("1")) {
            return true;
        } else {
            return false;
        }
    }
}
