package com.thit.tibdm.util;

import net.sf.json.JSONObject;

/**
 * @author wanghaoqiang
 */
public class TXIJsonUtil {
    /**
     *
     * @param jsonObject json
     * @param key 关键字
     * @param defaultvalue 默认值
     * @return 返回值
     */
    public static String getJsonValue(JSONObject jsonObject, String key, String defaultvalue) {
        return jsonObject.containsKey(key) ? jsonObject.getString(key) : defaultvalue;
    }

}
