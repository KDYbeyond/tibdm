package com.thit.tibdm.arithmetic;

import com.xicrm.model.TXIModel;
import net.sf.json.JSONObject;

import java.util.Iterator;

/**
 * @author wanghaoqiang
 */
public class TXIAssistUtil {
    /**
     *
     * @param json_obj_str json_obj_str
     * @param model 模型
     * @return TXIModel
     */
    public static TXIModel insertJSONObjToModel(String json_obj_str,
                                                TXIModel model) {
        JSONObject json_obj = JSONObject.fromObject(json_obj_str);
        String key = null;
        for (Iterator it = json_obj.keys(); it.hasNext(); ) {
            key = (String) it.next();
            model.setView(key, getJsonValue(json_obj, key, ""));
        }
        return model;
    }

    /**
     *
     * @param jsonObject  jsonObject
     * @param key key
     * @param defaultvalue 默认值
     * @return  String
     */
    public static String getJsonValue(JSONObject jsonObject, String key,
                                      String defaultvalue) {
        return jsonObject.containsKey(key) ? jsonObject.getString(key)
                : defaultvalue;
    }

    /**
     *
     * @param json_query_obj json_query_obj
     * @return  String
     */
    public static String getQueryKey(JSONObject json_query_obj) {
        String query_key = getJsonValue(json_query_obj, "ATTR_NAME", null);
        if (query_key == null || query_key.trim().length() == 0) {
            query_key = json_query_obj.getString("TAGNAME");
        }
        return query_key;
    }
}
