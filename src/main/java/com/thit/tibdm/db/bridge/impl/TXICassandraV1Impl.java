package com.thit.tibdm.db.bridge.impl;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.thit.tibdm.db.bridge.TXIBigDB;
import com.thit.tibdm.db.bridge.util.CassandraSingleConnect;
import com.thit.tibdm.db.bridge.util.TXIBigDBJNDINames;
import com.thit.tibdm.util.TXIFilterStepsUtil;
import com.thit.tibdm.util.TXIJsonUtil;
import com.thit.tibdm.util.TXIPoolJNDINames;
import com.xicrm.common.TXISystem;
import com.xicrm.exception.TXIException;
import com.xicrm.util.TXIDateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author wanghaoqiang
 */
public class TXICassandraV1Impl extends TXIBigDBBaseImpl implements TXIBigDB {
    /**
     *
     */
    static final String ORA_POOLNAME = TXIPoolJNDINames.DB_TIBDM_RDB_POOLNAME;
    /**
     *
     */
    static final String CASSANDRA_POOLNAME = TXIPoolJNDINames.DB_TIBDM_CASSANDRA_POOLNAME;
    /**
     *
     */
    static final String TAGNAME = "TAGNAME";
    /**
     *
     */
    static final String FIELD = "ATTR_NAME";
    /**
     *
     */
    static final String TIME = "COLLECT_TIME";

    /**
     * 读分段数据
     *
     * @author： 李丽
     * @version 1.0
     */
    @Override
    public String XiGetPeriodsData(String object_name, String collect_type,
                                   String machine_id, String query_json_keys, String start_time,
                                   String end_time, String max_num, String filter_mode,
                                   String query_time_format, String return_time_format)
            throws Exception {
        JSONArray jsonArrayResult = new JSONArray();
        JSONArray jsonArray = null;
        JSONObject jsonObj = null;

        try {
            TXISystem.log.debug(this, "11111 json_keys,max_num="
                    + query_json_keys + "," + query_json_keys);
            JSONObject queryJsonKeysJsonObj = JSONObject
                    .fromObject(query_json_keys);
            // json_array = JSONArray.fromObject(query_json_keys);
            jsonArray = queryJsonKeysJsonObj
                    .getJSONArray(TXIBigDBJNDINames.QUERY_ATTRS);
            if (jsonArray.size() == 0) {
                if (query_json_keys != null) {
                    String result = "{total:'0',rows='[]'}";
                    return result;
                }
            }

        } catch (Exception ex) {
            String errmsg = "传进来的json格式有错误:" + ex.getMessage() + " json变量="
                    + query_json_keys;
            TXISystem.log.error(this, errmsg, ex);
            ex.printStackTrace();
            throw new TXIException(errmsg);
        }

        try {
            String startTime1 = start_time;
            String endTime1 = end_time;
            if (query_time_format != null
                    && !query_time_format
                    .equals(TXIBigDBJNDINames.TIMESTEP_FORMAT)) {
                startTime1 = TXIDateUtil.convert2long(start_time,
                        query_time_format) + "";
                endTime1 = TXIDateUtil.convert2long(end_time,
                        query_time_format) + "";

            }

            HashMap<String, JSONObject> map = new HashMap();
            ArrayList list = new ArrayList();
            // long start = new Date().getTime() - 10000;
            // long end = new Date().getTime();
            // start = 1375632064;
            // end = 1375812064;

            JSONObject json_obj_result = null;
            String tagname = null;
            JSONArray json_array_temp = null;
            JSONObject json_obj_temp = null;
            String time = null;
            String field = null;

            // 提取数据
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObj = jsonArray.getJSONObject(i);
                TXISystem.log.info(this,
                        "query_json_keys=" + jsonObj.toString());
                // 取出要提取的TAGNAME和提取数据后要给的字段名
                tagname = jsonObj.getString(TAGNAME);
                field = TXIJsonUtil.getJsonValue(jsonObj, FIELD, tagname);
                json_array_temp = readData(tagname, field, startTime1,
                        endTime1, object_name);
                for (int j = 0; j < json_array_temp.size(); j++) {
                    json_obj_temp = json_array_temp.getJSONObject(j);
                    time = json_obj_temp.getString(TIME);
                    // colvalue = json_obj_temp.getString(VALUE);
                    // 放入格式类似："TIME":"一月","currentA":"20","currentB":"30","currentC":"40","vboltageA":"45","vboltageB":"15","vboltageC":"23","activePowerA":"10","activePowerB":"20","activePowerC":"23"

                    if (i == 0) {
                        json_obj_result = json_obj_temp;
                        map.put(time, json_obj_result);
                        list.add(time);
                    } else {
                        json_obj_result = map.get(time);
                        // 如果没有对应的值，则丢弃掉该值
                        if (json_obj_result == null) {
                            continue;
                        }
                    }
                    json_obj_result.put(field, json_obj_temp.get(field));
                }
            }

            // 处理最终结果
            List toList = TXIFilterStepsUtil.getFilterStepsList(list, max_num,
                    filter_mode);
            String return_time = null;
            jsonObj = null;
            long l_return_time;

            for (int i = 0; i < toList.size(); i++) {
                // json_obj =
                // map.get(list.get(Integer.parseInt(toList.get(i).toString())));
                // TXISystem.log.debug(this, "toList.get(i).toString()=" +
                // toList.get(i).toString());
                jsonObj = map.get(toList.get(i).toString());
                // TXISystem.log.debug(this, "json_obj=" + json_obj.toString());
                return_time = jsonObj.getString(TIME);
                // TXISystem.log.debug(this,"i,toList.size(),list.size() return_time="
                // + i + ","+ toList.size() + "," + list.size() + ","+
                // return_time);

                if (return_time_format != null
                        && return_time_format.equals("1")) {
                    jsonObj.put(TIME, return_time);
                } else {
                    l_return_time = Long.parseLong(return_time);
                    jsonObj.put(TIME, TXIDateUtil.convert2String(
                            l_return_time, return_time_format));
                }

                jsonArrayResult.add(jsonObj);
            }
            ;

            jsonObj = new JSONObject();
            jsonObj.put("total", list.size());
            jsonObj.put("rows", jsonArrayResult);

        } catch (Exception ex) {
            ex.printStackTrace();
            String errmsg = "执行获取分段数据时出错，";
            TXISystem.log.error(this, errmsg + ex.getLocalizedMessage(), ex);
            throw new TXIException(errmsg + ex.getMessage());
        }

        return jsonObj.toString();
    }

    @Override
    public String XiGetMutiMachinePeriodsData(String object_name, String collect_type, String machine_id, String query_json_keys, String start_time, String end_time, String max_num, String filter_mode, String query_time_format, String return_time_format) {
        return null;
    }

    // 从大数据库提取数据

    /**
     * @param tagname 名字
     * @param field id
     * @param start_time 开始时间
     * @param end_time 结束时间
     * @param object_name 名称
     * @return JSONArray 列表
     * @throws Exception 异常
     */
    private JSONArray readData(String tagname, String field, String start_time,
                               String end_time, String object_name) throws Exception {
        JSONObject json_obj = null;
        String cql = null;
        Statement state = null;

        try {

            makeConnection(CASSANDRA_POOLNAME);
            state = jdbcConn.createStatement();
            // time = new Date().getTime() / 10000 * 10000; // ???
            // 指定keyspace
            state.execute("use thss_thit_crc");

            // String tagname = "AKM.AKM.[AREA06].F90_snwd.PV";
            // String start_time_temp = "1465961700000";
            // String end_time_temp = "1465961760000";

            long starttime = Long.parseLong(start_time);
            long endtime = Long.parseLong(end_time);

            // cql = "SELECT * FROM opc_data where KEY = '" + tagname + "'";
            // objectPN = "opc_data";
//			cql = "SELECT " + starttime + " .. " + endtime + " FROM "
//					+ object_name + " where KEY = '" + tagname + "' limit 1000000";

            JSONArray json_array = new JSONArray();
            cql = "SELECT * FROM " + object_name + " where KEY = '" + tagname + "' and column1>" + start_time + " and column1<" + end_time;


            System.out.println("cql=" + cql);
            ResultSet rs = CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
            Iterator<Row> iterator = rs.iterator();
            while (iterator.hasNext()) {
                json_obj = new JSONObject();
                Row row = iterator.next();
                json_obj.put(TIME, row.getLong("column1"));
                json_obj.put(field, row.getString("value"));
                json_array.add(json_obj);
            }
            // System.out.println("----Start");
//			ResultSet rs = state.executeQuery(cql.toString());
            /*
             * while (rs.next()) { for (int i = 0; i <
			 * rs.getMetaData().getColumnCount(); i++) { System.out.println("i="
			 * + i + "---" + rs.getObject(i + 1)); } }
			 */
//			int i = 0;


//			while (rs.next()) {
//				json_obj = new JSONObject();
//				TXISystem.log.debug(this, "-----行号，列数=" + ++i + ","
//						+ rs.getMetaData().getColumnCount());
//				// TXISystem.log.debug(this, "colKey="
//				// + rs.getMetaData().getColumnName(1));
//				if (rs.getMetaData().getColumnCount() >= 2)
//					for (int j = 2; j < rs.getMetaData().getColumnCount() + 1; j++) {
//						String colName = rs.getMetaData().getColumnName(j);
//						// TXISystem.log.debug(this, "colName=" + colName);
//						// TXISystem.log.debug(this,"Long.parseLong(colName)="+
//						// Long.parseLong(colName));
//						// TXISystem.log.debug(this,"rs.getString(colName)=" +
//						// rs.getString(colName));
//						json_obj.put(TIME, colName);
//						json_obj.put(field, rs.getString(colName));
//						json_array.add(json_obj);
//					}
//			}

            // System.out.println("----读cql成功:" + new Date().getTime()
            // + " start,end=" + starttime + "," + endtime);
            return json_array;

        } catch (Exception e) {
            String errMsg = "读cql时出错！";
            error(errMsg + e.getMessage() + "cql=" + cql, e);
            e.printStackTrace();
            throw new Exception(errMsg);
        } finally {
            try {
                closeStatement(state);
            } catch (Exception e) {
            }
            dropConnection();
        }

    }

    // http://192.168.8.132:8060/tiems_carry/txieasyui?taskFramePN=GetBigData&command=GBD_GetPeriodsData&colname=json_ajax&colname1={dataform:%22eui_form_data%22}&object_name=opc_data&query_json_keys=[{"FIELD":"L0","TAGNAME":"TY_JC_DM.RCLJ_TDM26.SW_ET113_9_Ia"},{"FIELD":"L1","TAGNAME":"TY_JC_DM.RCLJ_TDM26.SW_ET113_9_Ib"},{"FIELD":"L2","TAGNAME":"TY_JC_DM.RCLJ_TDM26.SW_ET113_9_Ic"}]&start_time=1473841667000&end_time=1473845207000&max_num=100&filter_mode=1&query_time_format=1&return_time_format=1&jsonpCallback=callback

    /**
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main(String[] args) throws Exception {
        TXISystem.start();

        // String start_time =
        // TXIDateUtil.convert2long("2016/09/05 01:02:03.001","yyyy/MM/dd hh:mm:ss.ms")+"";
        // String start_time =
        // TXIDateUtil.convert2long("2016-09-05 01:02:03.001","yyyy-mm-dd hh:mm:ss")+"";

        // System.out.println("start_time="+start_time);
        // String end_time =
        // TXIDateUtil.convert2long("2016-09-05","yyyy-MM-dd")+"";
        // System.out.println("end_time="+end_time);
        TXICassandraV1Impl test = new TXICassandraV1Impl();
        String objectPN = "opc_data";

        // String start_time = "2016-06-05 01:02:03.001";
        // String end_time = "2016-09-05 01:02:03.001";
        // json_obj1.put("TAGNAME", "AKM.AKM.[AREA06].F90_snwd.PV");
        // json_obj2.put("TAGNAME", "TY_JC_DM.RCLJ_TDM26.SW_ET113_5_Q");

		/*
         * String return_time_format = "yyyy-MM-dd HH:mm:ss.ms"; String
		 * query_time_format = "yyyy-MM-dd HH:mm:ss.ms"; String start_time =
		 * "2016-09-05 01:02:03.001"; String end_time =
		 * "2016-09-06 01:02:03.001"; long l_start_time =
		 * TXIDateUtil.convert2long(start_time, query_time_format); long
		 * l_end_time = TXIDateUtil.convert2long(end_time, query_time_format);
		 * System
		 * .out.println("l_start_time="+l_start_time+" l_end_time="+l_end_time);
		 */

        String return_time_format = "1";
        String query_time_format = "1";
        String start_time = "1473008401000";
        String end_time = "1473094801000";
        String query_json_keys = "{query_attrs:[{TAGNAME:'TY_JC_DM.BYQ_TDM17.SW_ET105_6_ED',ATTR_NAME:'F1'},"
                + "{TAGNAME:'TY_BGQ_DM.ZP_TDM16.SW_DL9104_EH',ATTR_NAME:'F2'}]}";

        String max_num = "3";
        String filter_mode = "1";
        String collect_type = "";
        String machine_id = "";
        // String json_string = test.XiGetPeriodsData(objectPN,
        // json_array.toString(), start_time, end_time, max_num,
        // filter_mode, query_time_format, return_time_format);
        String json_string = test.XiGetPeriodsData(objectPN, collect_type,
                machine_id, query_json_keys, start_time, end_time, max_num,
                filter_mode, query_time_format, return_time_format);
        System.out.println("json_string=" + json_string);

    }

    /**
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main_test1(String[] args) throws Exception {
        TXISystem.start();

        // String start_time =
        // TXIDateUtil.convert2long("2016/09/05 01:02:03.001","yyyy/MM/dd hh:mm:ss.ms")+"";
        // String start_time =
        // TXIDateUtil.convert2long("2016-09-05 01:02:03.001","yyyy-mm-dd hh:mm:ss")+"";

        // System.out.println("start_time="+start_time);
        // String end_time =
        // TXIDateUtil.convert2long("2016-09-05","yyyy-MM-dd")+"";
        // System.out.println("end_time="+end_time);
        TXICassandraV1Impl test = new TXICassandraV1Impl();
        String objectPN = "opc_data";
        // JSONArray json_array = new JSONArray();

        // String start_time = "2016-06-05 01:02:03.001";
        // String end_time = "2016-09-05 01:02:03.001";
        // json_obj1.put("TAGNAME", "AKM.AKM.[AREA06].F90_snwd.PV");
        // json_obj2.put("TAGNAME", "TY_JC_DM.RCLJ_TDM26.SW_ET113_5_Q");

        String return_time_format = "1";
        String query_time_format = "1";
        String start_time = "1473008401000";
        String end_time = "1473094801000";
        String json_array_string = "[{TAGNAME:’TY_CL_DM.XZGJG_TDM5.SW_ET116_5_EH’,FIELD:'F1'},{TAGNAME:’TY_BGQ_DM.ZP_TDM16.SW_DL9104_EH’,FIELD:'F2'}]]";

        String max_num = "3";
        String filter_mode = "1";
        String collect_type = "";
        String machine_id = "";
        String json_string = test.XiGetPeriodsData(objectPN, collect_type,
                machine_id, json_array_string, start_time, end_time, max_num,
                filter_mode, query_time_format, return_time_format);
        System.out.println("json_string=" + json_string);

    }

    /**
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main_test(String[] args) throws Exception {
        TXISystem.start();
        // JSONObject json_test=new JSONObject();
        // json_test.put("aaa:bbb", "test:1122");
        // json_test.put("aaa:bbb", "http://localhost:8888/sss?aaa=1&bbb=3");
        // System.out.println("json_test="+json_test.toString());
        /*
		 * long start = (long)(startTime - utc).TotalMilliseconds; long end =
		 * (long)(endTime - utc).TotalMilliseconds; static DateTime utc = new
		 * DateTime(1970, 1, 1).AddHours(8); String cql =
		 * String.Format("SELECT {0} .. {1} FROM OPC_DATA where KEY = '{2}'",
		 * start, end, child);
		 */
        // String start_time = "1465961700000";
        // String end_time = "1465961760000";
        // long kkk_l = Long.parseLong("1465961700051");
        // String kkk = TXIDateUtil.convert2String((kkk_l),return_time_format);
        // System.out.println("kkk="+kkk);
        // kkk_l = Long.parseLong("1495961700001");
        // kkk = TXIDateUtil.convert2String((kkk_l),"yyyy-mm-dd hh:mm:ss.ms");
        // System.out.println("kkk="+kkk);

    }

}
