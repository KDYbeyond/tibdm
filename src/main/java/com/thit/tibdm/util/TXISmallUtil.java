package com.thit.tibdm.util;

import com.xicrm.common.TXISystem;


/**
 * Created by Administrator on 2016/11/7.
 */
public class TXISmallUtil {
    /**
     * @param item_name 名称
     * @param item_value 值
     * @throws Exception 异常
     */
    public static void mustCheck(String item_name, String item_value) throws Exception {
        if (item_value == null || item_value.trim().length() == 0) {
            String errmsg = item_name + " 不能为空！";
            TXISystem.log.error("mustCheck:", errmsg);
            throw new Exception(errmsg);
        }
    }

    /**
     * @param json 数组
     * @param item_name 名称
     * @throws Exception 异常
     */
    public static void checkQueryJsonKeys_one(String json, String item_name) throws Exception {
        if (json.toUpperCase().indexOf(item_name) == -1) {
            String errmsg = "queryjsonkeys中的" + item_name + "不能为空！";
            TXISystem.log.error("queryjsonkeys:", errmsg);
            throw new Exception(errmsg);
        }
    }

    /**
     * @param query_json_keys 查询关键字
     * @param strings 字符串
     * @throws Exception 异常
     */
    public static void checkQueryJsonKeys_all(String query_json_keys, String[] strings) throws Exception {
        for (String s : strings) {

            TXISmallUtil.checkQueryJsonKeys_one(query_json_keys.toUpperCase(), s);
        }
    }

}
