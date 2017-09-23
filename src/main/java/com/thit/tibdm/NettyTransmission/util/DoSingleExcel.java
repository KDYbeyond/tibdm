package com.thit.tibdm.NettyTransmission.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by dongzhiquan on 2017/4/14.
 */
public enum DoSingleExcel {
    /**
     * 实例
     */
    INSTANCE;

//    private Map instance;
//
//    /**
//     * 依据配置文件读取JSON来实例化
//     * 最后转为MAP
//     * 协议号--协议实体类
//     */
//    DoSingleExcel() {
//        String json = ResourceUtil.getProValueByNameAndKey("rdb", "pro");
//        instance = (Map) JSON.parse(json);
//    }
//
//    public Map getInstance() {
//        return instance;
//    }
    /**
     * 实例
     */
    private Map<String,JSONObject> instance;

    /**
     * 依据配置文件读取JSON来实例化
     * 最后转为MAP
     * 协议号--协议实体类
     */
    DoSingleExcel() {
        String json = ResourceUtil.getProValueByNameAndKey("rdb", "pro");
        instance = (Map<String, JSONObject>) JSON.parse(json);
    }

    public Map<String,JSONObject> getInstance() {
        return instance;
    }
}
