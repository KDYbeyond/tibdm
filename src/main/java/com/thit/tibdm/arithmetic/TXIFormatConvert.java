package com.thit.tibdm.arithmetic;

import com.thit.tibdm.db.bridge.util.TXIBigDBJNDINames;
import com.xicrm.exception.TXIException;
import com.xicrm.model.TXIModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author wanghaoqiang
 */
public class TXIFormatConvert {
    /**
     * @param param               需要计算平均值的字段 例如 arrayParam
     * @param paramValue          需要计算平均值的字段所对应的值 例如JsonParamValue
     * @param newField            最终计算值所要赋值的字段 {F1:”123”,F2:”143.16”,…,….} 例如 newField
     *                            query_json_keys[{KEY:XXX,ATTR_NAME:”F1”,GROUP_ID:” A相电压谐波”, TYPE:”2”},
     *                            {KEY:yyyy, ATTR_NAME:”F2” ,GROUP_ID:” A相电压谐波”, TYPE:”3”}, {KEY:xxx,
     *                            ATTR_NAME:”F3” ,GROUP_ID:” A相电压谐波”, TYPE:”4”}, {KEY:CCC, ATTR_NAME:”F3”,
     *                            ,GROUP_ID:”222”}]
     * @param key                 存放参数字段的，比如：key:"F1"
     * @param attrname            存放参数字段的 ,不为空是使用，为空时使用key，比如：attrname:"F1"
     * @param groupId             分组字段，例如：groupId："A相谐波电压"
     * @param type                次数字段 ， 例如：type："第一次"
     * @param model               ,period_data operator 操作符，sum，min，max，average
     * @param query_json_keys_str 字符串
     * @param tempdata_json_str   暂时
     * @return 返回值
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String XiFormatConvert(String query_json_keys_str, String tempdata_json_str, TXIModel model) throws TXIException {
        JSONObject tempdata_json_obj = JSONObject.fromObject(tempdata_json_str);
//System.out.println("query_json_keys_str="+query_json_keys_str);
        //String query_json_keys = (String) model.getFromView(query_json_keysPN);
        JSONObject query_keys_json_obj = JSONObject.fromObject(query_json_keys_str);
        JSONArray query_keys_json = query_keys_json_obj.getJSONArray(TXIBigDBJNDINames.QUERY_ATTRS);
        JSONObject json_query_obj = null;
        Double value1 = null, value2 = null, temp = null;
        int num = 1;
        //HashMap<String, String> group1_num_map = new HashMap(); //设定序号映射
        HashMap<String, JSONObject> group2_json_map = new HashMap(); //设定json映射
        List<String> list = new ArrayList();
        String group1 = null, group2 = null, value = null;
        //String group1_num = null;
        JSONObject group2_json = null;
        for (int i = 0; i < query_keys_json.size(); i++) {
            json_query_obj = query_keys_json.getJSONObject(i);
            String query_key = TXIAssistUtil.getQueryKey(json_query_obj);
            value = TXIAssistUtil.getJsonValue(tempdata_json_obj, query_key, null);
            group1 = TXIAssistUtil.getJsonValue(json_query_obj, "GROUP1", null);
            group2 = TXIAssistUtil.getJsonValue(json_query_obj, "GROUP2", null);
            if (group1 == null || group2 == null) {
                continue;
            }
            //group1_num = group1_num_map.get(group1);
            group2_json = group2_json_map.get(group2);
            if (group2_json == null) {
                group2_json = new JSONObject();
                group2_json.put("GROUP2", group2);
                group2_json_map.put(group2, group2_json);
                list.add(group2);
            }
            // if (group1_num == null) {
            //     group1_num = group1 + num++;
            //     group1_num_map.put(group1, group1_num);
            // }
            // group2_json.put(group1_num, value);
            group2_json.put(group1, value);
        }
        JSONArray json_array = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            group2 = list.get(i);
            json_array.add(group2_json_map.get(group2));
        }
        return json_array.toString();
    }
}
