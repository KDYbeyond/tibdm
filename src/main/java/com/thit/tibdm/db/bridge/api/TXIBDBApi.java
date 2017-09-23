package com.thit.tibdm.db.bridge.api;

import com.thit.tibdm.db.bridge.TXIBigDBFactory;
import com.thit.tibdm.db.bridge.util.CassandraV2Table;
import com.thit.tibdm.db.bridge.util.TXIBigDBJNDINames;
import com.thit.tibdm.util.TXISmallUtil;
import com.xicrm.common.TXISystem;
import com.xicrm.exception.TXIException;

/**
 * @author wanghaoqiang
 */
public class TXIBDBApi {
    public TXIBDBApi() {

    }

    /**
     * 获取时间段大数据
     *
     * @param object_name        对象持久化名，例如：OPC_DATA
     * @param query_json_keys    键值
     * @param start_time         起始时间
     * @param end_time           结束时间
     * @param max_num            最大记录数 -1 不做数据过滤，有多少数据给多少数据, 大于等于0 ,则提取出的数据如果大于最大数,则只给出最大数的个数，
     * @param filter_mode        最大数过滤策略，1 等步长过滤（按照总记录数与最大记录数做等步长映射过滤，例如10000对1000（按照总记录数与最大记录数做映射做过滤，例如10000对1000
     *                           ，每隔10个点做一个提取）
     * @param machine_id       机器ID
     * @param collect_type 收集类型
     * @param query_time_format 查询时间格式
     * @param return_time_format 返回时间格式
     * @return String
     * @throws Exception 异常
     */
    public String XiGetPeriodsData(String object_name, String collect_type, String machine_id, String query_json_keys,
                                   String start_time, String end_time, String max_num,
                                   String filter_mode, String query_time_format,
                                   String return_time_format) throws Exception {

        String impl_classname = TXISystem.config
                .getProperty(TXIBigDBJNDINames.Tibdm_BigDB_ImplClass);
        if (impl_classname != null && impl_classname.trim().length() != 0) {
            return TXIBigDBFactory.getInst(impl_classname)
                    .XiGetPeriodsData(object_name, collect_type, machine_id, query_json_keys, start_time,
                            end_time, max_num, filter_mode, query_time_format,
                            return_time_format);
        } else {
            throw stdException();
        }
    }


    /**
     * 保存数据到数据库(cassandra)
     *
     * @param object_name  对象名
     * @param collect_type 采集时间
     * @param machine_id   机器ID
     * @param json         属性值
     * @throws Exception 异常
     */
    public void save2BigDB(String object_name, String machine_id, String collect_type, String json) throws Exception {
        //检查方法所需参数个数
        TXISmallUtil.mustCheck("表名", object_name);
        TXISmallUtil.mustCheck("机器ID", machine_id);
        TXISmallUtil.mustCheck("采集类型", collect_type);
        TXISmallUtil.mustCheck("需保存数据", json);
        //检查json中的属性的个数
        String[] strings = new String[]{CassandraV2Table.machine_id, CassandraV2Table.collect_type
                , CassandraV2Table.collect_time, CassandraV2Table.receive_time};
        TXISmallUtil.checkQueryJsonKeys_all(json, strings);

        String impl_classname = TXISystem.config
                .getProperty(TXIBigDBJNDINames.Tibdm_BigDB_ImplClass);
        if (impl_classname != null && impl_classname.trim().length() != 0) {
            TXIBigDBFactory.getInst(impl_classname).save2BigDB(object_name, machine_id, collect_type, json);
        } else {
            throw stdException();
        }

    }

    /**
     * 批量保存数据到数据库(cassandra)
     *
     * @param object_name  对象名
     * @param collect_type 采集时间
     * @param machine_id   机器ID
     * @param jsonList     属性值
     * @throws Exception 异常
     */
    public void save2BigDBs(String object_name, String machine_id, String collect_type, String jsonList) throws Exception {

        TXISmallUtil.mustCheck("表名", object_name);
        TXISmallUtil.mustCheck("机器ID", machine_id);
        TXISmallUtil.mustCheck("采集类型", collect_type);


        String impl_classname = TXISystem.config
                .getProperty(TXIBigDBJNDINames.Tibdm_BigDB_ImplClass);
        if (impl_classname != null && impl_classname.trim().length() != 0) {
            TXIBigDBFactory.getInst(impl_classname).save2BigDBs(object_name, machine_id, collect_type, jsonList);
        } else {
            throw stdException();
        }

    }


    /**
     * 保存补录的数据
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param collect_time 采集时间
     * @throws Exception 异常
     */
    public void saveMarkupData(String object_name, String machine_id, String collect_type, String collect_time) throws Exception {
        String impl_classname = TXISystem.config
                .getProperty(TXIBigDBJNDINames.Tibdm_BigDB_ImplClass);
        if (impl_classname != null && impl_classname.trim().length() != 0) {
            TXIBigDBFactory.getInst(impl_classname).saveMakeupData(object_name, machine_id, collect_type, collect_time);
        } else {
            throw stdException();
        }
    }


    /**
     * 获取补录的数据
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @return String
     * @throws Exception 异常
     */
    public String getMarkupData(String object_name, String machine_id, String collect_type) throws Exception {

        TXISmallUtil.mustCheck("表名", object_name);
        TXISmallUtil.mustCheck("机器ID", machine_id);
        TXISmallUtil.mustCheck("采集类型", collect_type);

        String impl_classname = TXISystem.config
                .getProperty(TXIBigDBJNDINames.Tibdm_BigDB_ImplClass);
        if (impl_classname != null && impl_classname.trim().length() != 0) {
            return TXIBigDBFactory.getInst(impl_classname).getMakeupData(object_name, machine_id, collect_type);
        } else {
            throw stdException();
        }
    }

    /**
     * 单条删除补录的数据
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param collect_time 采集时间
     * @throws Exception 异常
     */
    public void delMarkupData(String object_name, String machine_id, String collect_type, String collect_time) throws Exception {

        TXISmallUtil.mustCheck("表名", object_name);
        TXISmallUtil.mustCheck("机器ID", machine_id);
        TXISmallUtil.mustCheck("采集类型", collect_type);
        TXISmallUtil.mustCheck("采集时间", collect_time);

        String impl_classname = TXISystem.config
                .getProperty(TXIBigDBJNDINames.Tibdm_BigDB_ImplClass);
        if (impl_classname != null && impl_classname.trim().length() != 0) {
            TXIBigDBFactory.getInst(impl_classname).delMakeupData(object_name, machine_id, collect_type, collect_time);
        } else {
            throw stdException();
        }
    }

    /**
     * 多条删除补录的数据
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param collect_time 采集时间
     * @throws Exception 异常
     */
    public void delMarkups(String object_name, String machine_id, String collect_type, String collect_time) throws Exception {

        TXISmallUtil.mustCheck("表名", object_name);
        TXISmallUtil.mustCheck("机器ID", machine_id);
        TXISmallUtil.mustCheck("采集类型", collect_type);
        TXISmallUtil.mustCheck("采集时间", collect_time);

        String impl_classname = TXISystem.config
                .getProperty(TXIBigDBJNDINames.Tibdm_BigDB_ImplClass);
        if (impl_classname != null && impl_classname.trim().length() != 0) {
            TXIBigDBFactory.getInst(impl_classname).delMakeups(object_name, machine_id, collect_type, collect_time);
        } else {
            throw stdException();
        }
    }

    /**
     * 抛出异常信息
     *
     * @return Exception 异常
     * @throws TXIException  异常
     */
    public Exception stdException() {
        String errmsg = "获取分段大数据实现类为空，请在xiconfig中配置TiBigDB_XiGetPeriodsBigData_ImplClass参数";
        TXISystem.log.error(this, errmsg);
        return new TXIException(errmsg);
    }

    /**
     * 分段数据多车接口
     *
     * @param object_name 对象名
     * @param collect_type 收集类型
     * @param list               多个车号
     * @param query_json_keys 查询关键字
     * @param start_time 开始时间
     * @param end_time 结束时间
     * @param max_num 最大数
     * @param filter_mode 过滤
     * @param query_time_format 查询时间格式
     * @param return_time_format 返回时间格式
     * @return String
     * @throws Exception 异常
     */

    public String XiGetMutiMachinePeriodsData(String object_name, String collect_type, String list, String query_json_keys, String start_time, String end_time, String max_num, String filter_mode, String query_time_format, String return_time_format) throws Exception {
        String impl_classname = TXISystem.config
                .getProperty(TXIBigDBJNDINames.Tibdm_BigDB_ImplClass);
        if (impl_classname != null && impl_classname.trim().length() != 0) {
            return TXIBigDBFactory.getInst(impl_classname)
                    .XiGetMutiMachinePeriodsData(object_name, collect_type, list, query_json_keys, start_time,
                            end_time, max_num, filter_mode, query_time_format,
                            return_time_format);
        } else {
            throw stdException();
        }

    }

    /**
     * 多线路
     *
     * @param object_name 对象名
     * @param collect_type 收集类型
     * @param list 列表
     * @param keys 关键字
     * @param start_time 开始时间
     * @param end_time 结束时间
     * @param max_num 最大数
     * @param filter_mode 过滤
     * @param query_time_format 查询时间格式
     * @param return_time_format 返回时间格式
     * @return String
     * @throws Exception 异常
     */
    public String XiGetMutiMachineByLineSum(String object_name, String collect_type, String list, String keys, String start_time, String end_time, String max_num, String filter_mode, String query_time_format, String return_time_format) throws Exception {

        String impl_classname = TXISystem.config
                .getProperty(TXIBigDBJNDINames.Tibdm_BigDB_ImplClass);
        if (impl_classname != null && impl_classname.trim().length() != 0) {
            return TXIBigDBFactory.getInst(impl_classname)
                    .XiGetMutiMachineByLineSum(object_name, collect_type, list, keys, start_time,
                            end_time, max_num, filter_mode, query_time_format,
                            return_time_format);
        } else {
            throw stdException();
        }
    }
}
