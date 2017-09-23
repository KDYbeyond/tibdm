package com.thit.tibdm.db.bridge;

import com.thit.tibdm.base.InterfaceBase;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author： 李丽
 */
public interface TXIBigDB extends InterfaceBase {

    /**
     * 获取时间段大数据接口
     *
     * @param object_name        对象持久化名，例如：opc_data
     * @param query_json_keys    键值
     * @param start_time         起始时间
     * @param end_time           结束时间
     * @param max_num            最大记录数 -1 不做数据过滤，有多少数据给多少数据, 大于等于0 ,则提取出的数据如果大于最大数,则只给出最大数的个数，
     * @param filter_mode        最大数过滤策略，1 等步长过滤，例如100条记录，取10条记录，则，每隔10个点取1个）
     * @param query_time_format  1 时间戳字符串格式 ,其它按照用户定义，例如：yyyy-MM-dd HH:mm:ss.ms
     * @param return_time_format 同上
     * @param collect_type 收集类型
     * @param machine_id 机器ID
     * @return String 字符串
     * @throws Exception 异常
     */

    String XiGetPeriodsData(String object_name, String collect_type, String machine_id, String query_json_keys,
                            String start_time, String end_time, String max_num,
                            String filter_mode, String query_time_format,
                            String return_time_format) throws Exception;

    /**
     * 接收外部信息存储到cassandra
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param json         其他属性
     */
    void save2BigDB(String object_name, String machine_id, String collect_type, String json);


    /**
     * 接收外部信息存储到cassandra
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param json         其他属性
     */
    void save2BigDBs(String object_name, String machine_id, String collect_type, String json);


    /**
     * 保存补录的数据
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param collect_time 采集时间
     */
    void saveMakeupData(String object_name, String machine_id, String collect_type, String collect_time);


    /**
     * 批量保存补录数据
     *
     * @param list 补录数据列表
     */
    void saveMakeupDataBatch(List<Map> list);

    /**
     * 获取补录的数据依据对象名,采集类型,机器
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   采集时间
     * @return String
     */
    String getMakeupData(String object_name, String machine_id, String collect_type);

    /**
     * 单条删除补录的数据
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param collect_time 采集时间
     */
    void delMakeupData(String object_name, String machine_id, String collect_type, String collect_time);

    /**
     * 多条删除补录的数据
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param timeList     时间数组
     */
    void delMakeups(String object_name, String machine_id, String collect_type, String timeList);

    /**
     * @param object_name 对象名
     * @param collect_type 收集类型
     * @param machine_id 机器ID
     * @param query_json_keys 关键字
     * @param start_time  开始时间
     * @param end_time 结束时间
     * @param max_num 最大数
     * @param filter_mode 过滤
     * @param query_time_format 查询时间格式
     * @param return_time_format 返回时间格式
     * @return String
     */
    String XiGetMutiMachinePeriodsData(String object_name, String collect_type, String machine_id, String query_json_keys, String start_time, String end_time, String max_num, String filter_mode, String query_time_format, String return_time_format);

    /**
     * @param object_name        对象名
     * @param collect_type       收集类型
     * @param list               列表
     * @param keys               关键字
     * @param start_time         开始时间
     * @param end_time           结束时间
     * @param max_num            最大数
     * @param filter_mode        过滤
     * @param query_time_format  查询时间格式
     * @param return_time_format 返回时间格式
     * @return String
     */
    String XiGetMutiMachineByLineSum(String object_name, String collect_type, String list, String keys, String start_time, String end_time, String max_num, String filter_mode, String query_time_format, String return_time_format);

}
