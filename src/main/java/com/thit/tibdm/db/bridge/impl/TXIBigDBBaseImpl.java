package com.thit.tibdm.db.bridge.impl;


import net.sf.json.JSONObject;

import com.thit.tibdm.db.bridge.TXIBigDB;
import com.xicrm.common.TXISystem;
import com.xicrm.dao.DAOImpl;

import java.util.List;
import java.util.Map;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**
 * @author wanghaoqiang
 */
public class TXIBigDBBaseImpl extends DAOImpl implements TXIBigDB {

    /**
     * 读分段数据
     *
     * @param return_time_format 时间格式
     * @param collect_type 收集类型
     * @param query_time_format 查询时间格式
     * @param machine_id 机器ID
     * @param end_time 结束时间
     * @param filter_mode 过滤
     * @param max_num 最大数
     * @param object_name 对象名
     * @param query_json_keys 查询关键字
     * @param start_time 开始时间
     * @return string 字符串
     * @throws Exception 异常
     * @author： 李丽
     * @version 1.0
     */
    public String XiGetPeriodsData(String object_name, String collect_type, String machine_id, String query_json_keys,
                                   String start_time, String end_time, String max_num,
                                   String filter_mode, String query_time_format,
                                   String return_time_format) throws Exception {

        JSONObject json_obj = new JSONObject();
        return json_obj.toString();
    }

    /**
     * 接收外部信息粗存接口(对外)
     *
     * @param json json
     * @param object_name 对象名
     * @param machine_id 机器ID
     * @param collect_type 采集类型
     * @return 返回值
     */
    public void save2BigDB(String object_name, String collect_type, String machine_id, String json) {
    }

    /**
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param json         其他属性
     */
    @Override
    public void save2BigDBs(String object_name, String collect_type, String machine_id, String json) {

    }

    @Override
    public void saveMakeupData(String object_name, String collect_type, String machine_id, String collect_time) {
        //的保存补录数据实现
    }

    @Override
    public void saveMakeupDataBatch(List<Map> list) {

    }

    @Override
    public String getMakeupData(String object_name, String collect_type, String machine_id) {
        return null;
    }

    @Override
    public void delMakeupData(String object_name, String collect_type, String machine_id, String collect_time) {

    }

    @Override
    public void delMakeups(String object_name, String collect_type, String machine_id, String timeList) {

    }

    @Override
    public String XiGetMutiMachinePeriodsData(String object_name, String collect_type, String machine_id, String query_json_keys, String start_time, String end_time, String max_num, String filter_mode, String query_time_format, String return_time_format) {
        return null;
    }

    @Override
    public String XiGetMutiMachineByLineSum(String object_name, String collect_type, String list, String keys, String start_time, String end_time, String max_num, String filter_mode, String query_time_format, String return_time_format) {
        return null;
    }


    // http://192.168.8.132:8060/tiems_carry/txieasyui?taskFramePN=GetBigData&command=GBD_GetPeriodsData&colname=json_ajax&colname1={dataform:%22eui_form_data%22}&object_name=opc_data&query_json_keys=[{"FIELD":"L0","TAGNAME":"TY_JC_DM.RCLJ_TDM26.SW_ET113_9_Ia"},{"FIELD":"L1","TAGNAME":"TY_JC_DM.RCLJ_TDM26.SW_ET113_9_Ib"},{"FIELD":"L2","TAGNAME":"TY_JC_DM.RCLJ_TDM26.SW_ET113_9_Ic"}]&start_time=1473841667000&end_time=1473845207000&max_num=100&filter_mode=1&query_time_format=1&return_time_format=1&jsonpCallback=callback
    public static void main(String[] args) throws Exception {
        TXISystem.start();

    }

}
