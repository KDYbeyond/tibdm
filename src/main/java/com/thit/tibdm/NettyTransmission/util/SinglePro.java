package com.thit.tibdm.NettyTransmission.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thit.tibdm.bom.entity.MessageProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * 协议单例
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-06-17 19:41
 **/
public enum SinglePro {
    /**
     * 实例
     */
    instance;
    /**
     * 集合
     */
    private Map<String, MessageProtocol> pro=new HashMap<>();

    SinglePro() {
        String json = ResourceUtil.getProValueByNameAndKey("rdb", "pro");
        Map<String, JSONObject> parse = (Map<String, JSONObject>) JSON.parse(json);
        parse.forEach((k, v) -> {
            if (v.size() != 0) {
                pro.put(k, v.toJavaObject(MessageProtocol.class));
            }
        });
    }

    public Map<String, MessageProtocol> getInstance() {
        return pro;
    }
}
