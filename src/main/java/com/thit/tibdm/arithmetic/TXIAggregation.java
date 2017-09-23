package com.thit.tibdm.arithmetic;

import com.xicrm.business.TXIBizPage;
import com.xicrm.business.common.TXIBizCommonJSONString;
import com.xicrm.common.TXISystem;
import com.xicrm.control.util.TXIControlUtil;
import com.xicrm.exception.TXIException;
import com.xicrm.model.TXIModel;
import com.xicrm.util.TXIDateUtil;
import com.xicrm.util.TXIUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.thit.tibdm.api.TXIBDMApi;
import com.thit.tibdm.db.bridge.util.TXIBigDBJNDINames;

/**
 * @author wanghaoqiang
 */
public class TXIAggregation {
    /**
     * 平均信号
     */
    private static final String AVERAGE_SIGN = "average";
    /**
     * 总数信号
     */
    private static final String SUM_SIGN = "sum";
    /**
     * 最小信号量
     */
    private static final String MIN_SIGN = "min";
    /**
     * 最大信号量
     */
    private static final String MAX_SIGN = "max";
    /**
     * 平均
     */
    private static final int AVERAGE = 1;
    /**
     * 总数
     */
    private static final int SUM = 2;
    /**
     * 最小
     */
    private static final int MIN = 3;
    /**
     * 最大
     */
    private static final int MAX = 4;


    /**
     * 得到时间段大数据聚合后的数据
     *
     * @param object_name         大数据中的业务对象名,例如：opc_data
     * @param assist_config_param 配置参数，可以对应业务系统的表名，用于提取关系表中对应的sql语句
     * @param query_json_keys     查询的json关键字串，里面的字段与上面配置参数对应的sql语句中的变量按照变量名对应
     * @param start_time          到毫秒的起始时间 ，可以为长整型字符串，
     * @param end_time            到靠秒的结束时间。可以为长整型字符串，
     * @param query_time_format   查询时间格式,如果上面时间为长整型串，则给1，否则为对应到毫秒级的格式
     * @param operate             操作符，sum，min，max，average。例如：average
     * @param precision           聚合预算采用的小数点后的精度，例如：2
     * @param format_y_entend_by  数据纵向拉伸的依据，GROUP2 按照小类拉伸，
     * @param model               模型
     * @param collect_type        收集类型
     * @param machine_id          机器ID
     * @return 返回值
     * @throws Exception 异常
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String XiAggregation(String object_name, String collect_type, String machine_id, String assist_config_param,
                                String query_json_keys, String start_time, String end_time,
                                String query_time_format, String operate, String precision,
                                String format_y_entend_by, TXIModel model) throws Exception {
        TXIUtil.showModel("-model--", model);
        // 得到配置信息
        String sql = TXiAssitConfig.getString(assist_config_param
                + TXIJNDINames.SQL);

        // sql="select TAGNAME AS TAGNAME, PARAMETER_TYPE AS GROUP1, PARAMETER_ORDER AS GROUP2 from  ems_powerquality_tags t1,ems_powerquality_dict t2 where t2.parameter_name=t1.parameter_type and t2.general_name='电压谐波频谱' and t1.jlyb_no='ET209_1_ZL' order by PARAMETER_TYPE, PARAMETER_ORDER";
        if (query_json_keys == null || query_json_keys.trim().length() == 0) {
            String errmsg = "查询json串等于空，请赋值";
            TXISystem.log.error(this, errmsg);
            throw new TXIException(errmsg);
        }

        if (sql == null || sql.trim().length() == 0) {
            String errmsg = "assist_config_param对应的sql语句为空，请检查tidbm_assist.properties 文件";
            TXISystem.log.error(this, errmsg);
            throw new TXIException(errmsg);

        }

        // 处理sql字符串，并执行sql语句
        TXIModel tempModel = new TXIModel(model);
        tempModel = TXIAssistUtil.insertJSONObjToModel(query_json_keys,
                tempModel);
        TXIUtil.showModel("--tempModel--", tempModel);
        sql = TXIControlUtil.getBizFieldValue(sql, tempModel);
        TXIBizPage.XiSelfDefSelectListAll(sql, tempModel);
        TXIBizCommonJSONString bizclass = new TXIBizCommonJSONString();
        tempModel = bizclass.XiConvertModelDetailToJSONArrayString("detail0",
                "TAGNAME;ATTR_NAME;GROUP1;GROUP2",
                "TAGNAME;ATTR_NAME;GROUP1;GROUP2", "insert_mater_fieldPN",
                tempModel);
        String insert_mater_value = (String) tempModel.getFromView("insert_mater_fieldPN");
        JSONObject json_obj = new JSONObject();
        json_obj.put(TXIBigDBJNDINames.QUERY_ATTRS, insert_mater_value);
        String new_query_json_keys = json_obj.toString();
        //String new_query_json_keys ="{"+TXIBigDBJNDINames.QUERY_ATTRS+":"+insert_mater_value+"}";

        // 提取分段大数据数据
        TXIBDMApi db_api = new TXIBDMApi();
        String period_data_json = db_api.XiGetPeriodsData(object_name, collect_type, machine_id,
                new_query_json_keys, start_time, end_time, "-1", "1",
                query_time_format, "1", model);
        // 聚合运算
        String tempdata_json_str = aggregation(new_query_json_keys,
                period_data_json, operate, precision, model);

        // 格式转换
        String result_value = TXIFormatConvert.XiFormatConvert(
                new_query_json_keys, tempdata_json_str, model);
        return result_value;
    }

    /**
     * @param param                需要计算平均值的字段 例如 arrayParam
     * @param paramValue           需要计算平均值的字段所对应的值 例如JsonParamValue
     * @param newField             最终计算值所要赋值的字段 {F1:”123”,F2:”143.16”,…,….} 例如 newField
     *                             query_json_keys[{KEY:XXX,ATTR_NAME:”F1”,GROUP_ID:” A相电压谐波”,
     *                             TYPE:”2”}, {KEY:yyyy, ATTR_NAME:”F2” ,GROUP_ID:” A相电压谐波”,
     *                             TYPE:”3”}, {KEY:xxx, ATTR_NAME:”F3” ,GROUP_ID:” A相电压谐波”,
     *                             TYPE:”4”}, {KEY:CCC, ATTR_NAME:”F3”, ,GROUP_ID:”222”}]
     * @param key                  存放参数字段的，比如：key:"F1"
     * @param attrname             存放参数字段的 ,不为空是使用，为空时使用key，比如：attrname:"F1"
     * @param groupId              分组字段，例如：groupId："A相谐波电压"
     * @param type                 次数字段 ， 例如：type："第一次"
     * @param query_json_keys      查询关键字
     * @param operator             操作
     * @param period_data_json_str 时期
     * @param precision            精度
     * @param model                ,period_data operator 操作符，sum，min，max，average
     * @return 返回
     * @throws Exception 异常
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String aggregation(String query_json_keys,
                                      String period_data_json_str, String operator, String precision,
                                      TXIModel model) throws TXIException {

        int i_operator = getOperator(operator);
        if (i_operator == -1) {
            String errmsg = "聚合预算符为不正确! operator=" + operator;
            TXISystem.log.error(TXIAggregation.class.getName(),
                    "XiAggregation:" + errmsg);
            throw new TXIException(errmsg);
        }

        JSONObject period_data_json = JSONObject
                .fromObject(period_data_json_str);
        if (TXISystem.config.getTestSwitch()) {
            TXISystem.log.info("aggregation:", period_data_json.toString());
            System.out.println(period_data_json.toString());
        }
        String totals = period_data_json.getString("total");
        if (totals != null && totals.trim().equals("0")) {
            return "{}";
        }

        JSONArray period_data_array_json = period_data_json
                .getJSONArray("rows");
        // String query_json_keys = (String)
        // model.getFromView(query_json_keysPN);
        JSONObject query_keys_json = JSONObject.fromObject(query_json_keys);
        JSONArray query_keys_json_attrs = query_keys_json.getJSONArray(TXIBigDBJNDINames.QUERY_ATTRS);

        HashMap<String, TXITempStru> hm = new HashMap();
        JSONObject json_periaddata_obj = null;
        JSONObject json_query_obj = null;
        // Double value1 = null, value2 = null, temp = null;
        // 做运算
        int i_operator_temp = i_operator;
        if (i_operator_temp == AVERAGE) {
            i_operator_temp = SUM;
        }
        TXITempStru temp_stru = null;
        String value2 = null;
        for (int i = 0; i < period_data_array_json.size(); i++) {
            json_periaddata_obj = period_data_array_json.getJSONObject(i);
            for (int j = 0; j < query_keys_json_attrs.size(); j++) {
                json_query_obj = query_keys_json_attrs.getJSONObject(j);
                String query_key = TXIAssistUtil.getQueryKey(json_query_obj);
                if (!json_periaddata_obj.containsKey(query_key)) {
                    continue;
                }

                if (hm.get(query_key) == null) {
                    temp_stru = new TXITempStru();
                    temp_stru.setValue(0);
                    temp_stru.setNumber(0);
                } else {
                    temp_stru = hm.get(query_key);
                }

                value2 = json_periaddata_obj.getString(query_key);
                if (value2 != null && !value2.equals("*")) {
                    temp_stru.setValue(operator(temp_stru.getValue(),
                            Double.valueOf(value2), i_operator_temp));
                    temp_stru.setNumber(temp_stru.getNumber() + 1);
                }

                hm.put(query_key, temp_stru);
            }
        }

        // 对结果求平均
        JSONObject result_json = new JSONObject();
        String key = "";
        Double value = null;
        for (Iterator it = hm.keySet().iterator(); it.hasNext(); ) {
            key = (String) it.next();
            temp_stru = hm.get(key);
            if (i_operator == AVERAGE) {
                value = temp_stru.getValue() / temp_stru.getNumber();
            } else {
                value = temp_stru.getValue();
            }
            BigDecimal bd = new BigDecimal(value + "");
            if (precision != null && precision.trim().length() != 0) {
                bd = bd.setScale(Integer.parseInt(precision),
                        BigDecimal.ROUND_HALF_UP);
            }
            result_json.put(key, bd.toString());
        }

        return result_json.toString();
    }

    /**
     *
     * @param value1 数值
     * @param value2 数值
     * @param i_operator 操作
     * @return Double
     */
    private static Double operator(Double value1, Double value2, int i_operator) {
        Double d_temp = null;

        switch (i_operator) {
            case SUM:
                d_temp = value1.doubleValue() + value2.doubleValue();
                break;
            case MIN:
                d_temp = value1.doubleValue() < value2.doubleValue() ? value1
                        .doubleValue() : value2.doubleValue();
                break;
            case MAX:
                d_temp = value1.doubleValue() > value2.doubleValue() ? value1
                        .doubleValue() : value2.doubleValue();
                break;
            default:
                break;
        }
        return d_temp;
    }

    private static int getOperator(String operator) {
        int i_operator = -1;
        if (operator != null && operator.trim().length() != 0) {
            operator = operator.toLowerCase();
            if (operator.equals(AVERAGE_SIGN)) {
                i_operator = AVERAGE;
            } else if (operator.equals(SUM_SIGN)) {
                i_operator = SUM;
            } else if (operator.equals(MIN_SIGN)) {
                i_operator = MIN;
            } else if (operator.equals(MAX_SIGN)) {
                i_operator = MAX;
            }
        }
        return i_operator;
    }

    /**
     *
     * @param str 参数
     * @throws Exception 异常
     */
    public static void main(String[] str) throws Exception {
        TXIModel model = new TXIModel(null);

        TXISystem.start();

        // String start_time =
        // TXIDateUtil.convert2long("2016/09/05 01:02:03.001","yyyy/MM/dd hh:mm:ss.ms")+"";
        // String start_time =
        // TXIDateUtil.convert2long("2016-09-05 01:02:03.001","yyyy-mm-dd hh:mm:ss")+"";

        // System.out.println("start_time="+start_time);
        // String end_time =
        // TXIDateUtil.convert2long("2016-09-05","yyyy-MM-dd")+"";
        // System.out.println("end_time="+end_time);
        TXIAggregation test = new TXIAggregation();
        String object_name = "opc_data";

        String start_time = TXIDateUtil.convert2long("2016/09/26 18:04:41.001",
                "yyyy/MM/dd hh:mm:ss.ms") + "";
        String end_time = TXIDateUtil.convert2long("2016/09/26 20:54:41.001",
                "yyyy/MM/dd hh:mm:ss.ms") + "";
        System.out.println("start_time,end_time=" + start_time + "," + end_time);

        String query_time_format = "1";
        // String start_time = "1473008401000";
        // String end_time = "1473094801000";

        String query_json_keys = "{JLYB_NAME:'ET209_1_ZL',OPC_TYPE:'dyxbpp'}";
        String assist_config_param = "ems_powerquality_tags";
        String operate = "average";
        String precision = "2";
        String collect_type = "";
        String machine_id = "";
        String format_y_entend_by = "GROUP2";

        System.out.println("---------11111=");
        String out = test.XiAggregation(object_name, collect_type, machine_id, assist_config_param,
                query_json_keys, start_time, end_time, query_time_format,
                operate, precision, format_y_entend_by, model);
    }

    /**
     *@param str 字符串数组
     *           @throws Exception 异常
     */
    public static void main3(String[] str) throws Exception {
        TXIModel model = new TXIModel(null);
        TXIAggregation test = new TXIAggregation();

        TXISystem.start();
        String query_json_keysPN = "";
        String query_json_keys = "{query_attrs:["
                + "{TAGNAME:'F1',GROUP1:'A电压谐波频谱',GROUP2:'1'},"
                + "{TAGNAME:'F2',GROUP1:'A电压谐波频谱',GROUP2:'2'},"
                + "{TAGNAME:'F3',GROUP1:'A电压谐波频谱',GROUP2:'3'},"
                + "{TAGNAME:'F4',GROUP1:'B电压谐波频谱',GROUP2:'1'},"
                + "{TAGNAME:'F5',GROUP1:'B电压谐波频谱',GROUP2:'2'},"
                + "{TAGNAME:'F6',GROUP1:'B电压谐波频谱',GROUP2:'3'},"
                + "{TAGNAME:'F7',GROUP1:'C电压谐波频谱',GROUP2:'1'},"
                + "{TAGNAME:'F8',GROUP1:'C电压谐波频谱',GROUP2:'2'},"
                + "{TAGNAME:'F9',GROUP1:'C电压谐波频谱',GROUP2:'3'}" + "]}";

        String period_data_json_str = "{total:'300',rows:["
                + "{F1:'1',F2:'2',F3:'3',F4:'1',F5:'2',F6:'5',F7:'1',F8:'2',F9:'7'},"
                + "{F1:'1',F2:'2',F3:'3',F4:'1',F5:'2',F6:'5',F7:'1',F8:'2',F9:'7'},"
                + "{F1:'1',F2:'2',F3:'3',F4:'1',F5:'2',F6:'5',F7:'1',F8:'2',F9:'7'},"
                + "{F1:'1',F2:'2',F3:'3',F4:'1',F5:'2',F6:'5',F7:'1',F8:'2',F9:'7'},"
                + "{F1:'1',F2:'2',F3:'3',F4:'1',F5:'2',F6:'5',F7:'1',F8:'2',F9:'7'}"
                + "]}";
        String operator = "average";
        String precision = "2";

        String tempdata_json_str = test.aggregation(query_json_keys,
                period_data_json_str, operator, precision, model);
        String result = TXIFormatConvert.XiFormatConvert(query_json_keys,
                tempdata_json_str, model);

        System.out.println("out=" + result);
    }

}
