package com.thit.tibdm.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.thit.tibdm.NettyTransmission.util.ResourceUtil;
import com.thit.tibdm.arithmetic.TXIAggregation;
import com.thit.tibdm.calculation.DBConnection;
import com.thit.tibdm.calculation.TXICalcula;
import com.thit.tibdm.db.bridge.api.TXIBDBApi;
import com.thit.tibdm.db.bridge.impl.TXICassandraV2Impl;
import com.thit.tibdm.imdb.bridge.api.TXIInMemoryDBApi;
import com.thit.tibdm.imdb.bridge.util.RedisUtil;
import com.thit.tibdm.util.Constants;
import com.thit.tibdm.util.JexlUtil;
import com.thit.tibdm.util.ProtocolConstants;
import com.thit.tibdm.util.TXIFilterStepsUtil;
import com.xicrm.model.TXIModel;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 外层API
 *
 * @author wanghaoqiang
 */
public class TXIBDMApi {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TXIBDMApi.class);

    /**
     * flag表
     */
    private static String FLAG = "flag";

    public TXIBDMApi() {

    }

    /**
     * 获取时间段大数据
     *
     * @param object_name        对象持久化名，例如：OPC_DATA
     * @param query_json_keys    键值
     * @param start_time         起始时间
     * @param end_time           结束时间
     * @param max_num            最大记录数 -1 不做数据过滤，有多少数据给多少数据, 大于等于0 ,则提取出的数据如果大于最大数,则只给出最大数的个数，
     * @param filter_mode        最大数过滤策略，1
     *                           自动调整（按照总记录数与最大记录数做映射做过滤，例如10000对1000
     *                           ，每隔10个点做一个提取）
     * @param query_time_format  查询时间的格式   1.时间戳格式 2.字符串格式 yyyy/MM/dd HH:mm格式
     * @param return_time_format 返回时间格式  1.时间戳格式 2.字符串格式 yyyy/MM/dd HH:mm格式
     * @param collect_type       采集类型
     * @param machine_id         机器ID
     * @param model              平台
     * @return result
     * @throws Exception 自定义异常
     */
    public static String XiGetPeriodsData(String object_name, String collect_type, String machine_id, String query_json_keys,
                                          String start_time, String end_time, String max_num,
                                          String filter_mode, String query_time_format,
                                          String return_time_format, TXIModel model) throws Exception {
        TXIBDBApi bigDBApi = new TXIBDBApi();
        return bigDBApi.XiGetPeriodsData(object_name, collect_type, machine_id, query_json_keys,
                start_time, end_time, max_num, filter_mode, query_time_format,
                return_time_format);

    }

    /**
     * 多车
     *
     * @param object_name        对象名
     * @param collect_type       采集类型
     * @param machine_id         机器ID
     * @param query_json_keys    查询keys
     * @param start_time         开始时间
     * @param end_time           结束时间
     * @param max_num            最大数量
     * @param filter_mode        过滤规则
     * @param query_time_format  过滤时间类型
     * @param return_time_format 返回时间类型
     * @param model              平台model
     * @return result
     * @throws Exception 自定义异常
     */
    public static String XiGetMutiMachinePeriodsData(String object_name, String collect_type, String machine_id, String query_json_keys,
                                                     String start_time, String end_time, String max_num,
                                                     String filter_mode, String query_time_format,
                                                     String return_time_format, TXIModel model) throws Exception {
        TXIBDBApi bigDBApi = new TXIBDBApi();
        return bigDBApi.XiGetMutiMachinePeriodsData(object_name, collect_type, machine_id, query_json_keys,
                start_time, end_time, max_num, filter_mode, query_time_format,
                return_time_format);

    }


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
     * @param collect_type        采集类型
     * @param machine_id          机器ID
     * @param model               平台model
     * @return result
     * @throws Exception 自定义注解
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String XiAggregation(String object_name, String collect_type, String machine_id, String assist_config_param,
                                String query_json_keys, String start_time, String end_time,
                                String query_time_format, String operate, String precision,
                                String format_y_entend_by, TXIModel model) throws Exception {
        TXIAggregation aggregation = new TXIAggregation();
        return aggregation.XiAggregation(object_name, collect_type, machine_id, assist_config_param,
                query_json_keys, start_time, end_time, query_time_format,
                operate, precision, format_y_entend_by, model);

    }


    /**
     * 获取实时数据单条
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param model        平台Model
     * @return result
     */
    public static String XiGetRealTimeData(String object_name, String machine_id, String collect_type, TXIModel model) {
        String result = null;
//        System.out.println("XiGetRealTimeData===" + object_name + "====" + machine_id + "=====" + collect_type);
        TXIInMemoryDBApi inMemoryDBApi = new TXIInMemoryDBApi();
        try {
//            long startTime = System.currentTimeMillis();//获取当前时间
            result = inMemoryDBApi.getRealTimeData(object_name, machine_id, collect_type);
//            long endTime = System.currentTimeMillis();
//            System.out.println("读取程序运行时间：" + (endTime - startTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(result);
        return result;
    }

    /**
     * 获取特定车辆的
     *
     * @param object_name  对象名
     * @param machine_ids  机器ID列表
     * @param collect_type 采集类型
     * @param keys         keys列表
     * @param model        平台
     * @return result
     * @throws UnsupportedEncodingException 不支持的编码类型
     */
    public static String XiGetRealTimeDataByKey(String object_name, String machine_ids, String collect_type, String keys, TXIModel model)
            throws UnsupportedEncodingException {
//        keys = URLDecoder.decode(keys, "UTF-8");
//        System.out.println("转义后的字符为：" + keys);
        JSONArray ms = JSON.parseArray(machine_ids);
        JSONArray ks = JSON.parseArray(keys);
//        System.out.println(ms.toString());
//        System.out.println(ks.toString());
        String timeEx = ResourceUtil.getProValueByNameAndKey("redis-db", "timeEx");
        String table = ResourceUtil.getProValueByNameAndKey("redis-db", "tablerealtime");
        String tableEx = ResourceUtil.getProValueByNameAndKey("redis-db", "tableEx");


        Map<String, Map<String, String>> result = new HashMap<>();
        ms.forEach(machine_id -> {
            long l = System.currentTimeMillis();
            String s = XiGetRealTimeData(table, machine_id.toString(), collect_type, model);
            if (s == null) {
                return;
            }
            Map<String, Object> parse = (Map<String, Object>) JSON.parse(s);
            if (parse == null) {
                return;
            } else {
                if (!parse.isEmpty()) {
                    Map<String, String> json = (Map<String, String>) parse.get(machine_id.toString());
                    //如果是过滤的话会执行下面的代码
                    if (tableEx.equals(object_name)) {
                        //时间大于约定的时间
                        if (Math.abs(l - Long.parseLong(json.get("COLLECT_TIME"))) > Long.parseLong(timeEx)) {
                            return;
                        }
                    }
                    result.put(machine_id.toString(), TXIFilterStepsUtil.filterMap(json, ks));
                }

            }
        });
        //获取特定的属性
        String s = JSON.toJSONString(result);
//        System.out.println("最终结果===" + s);
        return s;
    }

    /**
     * 分别保存数据到内存型数据库和cassandra数据库
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param json         其他属性
     * @param model        平台
     * @throws Exception 自定义异常
     */
    public static void XiSave2Db(String object_name, String machine_id, String collect_type, String json, TXIModel model) throws Exception {
//        System.out.println("机器ID=====" + machine_id);
        // 需要向redis和cassandra里面写入信息
        // cassandra
        if (collect_type == null || collect_type.equals("")) {
            collect_type = Constants.DEFAULT_COLLECTTYPE;
        }
        long startTime = System.currentTimeMillis();//获取当前时间
        TXIBDBApi bigDBApi = new TXIBDBApi();
        bigDBApi.save2BigDB(object_name, machine_id, collect_type, json);

        long endTime = System.currentTimeMillis();
        System.out.println("cassandra插入程序运行时间：" + (endTime - startTime) + "ms");
        // redis
        startTime = System.currentTimeMillis();//获取当前时间
        TXIInMemoryDBApi inMemoryDBApi = new TXIInMemoryDBApi();
        inMemoryDBApi.save2InMemoryDB(object_name, machine_id, collect_type, json);

        endTime = System.currentTimeMillis();
        System.out.println("redis插入程序运行时间：" + (endTime - startTime) + "ms");
    }

    /**
     * 批量获取实时数据多条
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param list         机器ID数组
     * @param model        平台
     * @return result
     * @throws Exception 自定义异常
     */
    public static String XiGetRealTimes(String object_name, String collect_type, String list, TXIModel model) throws Exception {
        System.out.println(object_name + collect_type + list + "=======================");
        TXIInMemoryDBApi inMemoryDBApi = new TXIInMemoryDBApi();
        String result = inMemoryDBApi.getRealTimeDataBatch(object_name, collect_type, list);
        return result;
    }

    /**
     * 通过线路来获取车辆然后将属性累加
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param lineList     线路列表
     * @param keys         属性列表
     * @param model        平台model
     * @return result
     * @throws Exception 自定义异常
     */
    public static String XiGetRealTimeByLineSum(String object_name, String collect_type, String lineList, String keys, TXIModel model)
            throws Exception {
//        keys = URLDecoder.decode(keys, "UTF-8");
//        System.out.println("转义后的字符为：" + keys);
//        System.out.println("线路：" + lineList);
//        System.out.println("keys：" + keys);
        JSONArray ks = JSON.parseArray(keys);
        JSONArray ll = JSON.parseArray(lineList);
        String timeEx = ResourceUtil.getProValueByNameAndKey("redis-db", "timeEx");
        String table = ResourceUtil.getProValueByNameAndKey("redis-db", "tablerealtime");
        String tableEx = ResourceUtil.getProValueByNameAndKey("redis-db", "tableEx");

        //结果map结构：{"l1":{"ZT1":90,"ZT2+ZT3":900},"l2":""}
        Map<String, Map<String, String>> result = new HashMap<>();
        ll.forEach(line -> {
            List<String> ids =
                    DBConnection.getListBySql(ResourceUtil.getProValueByNameAndKey("cassandra-db", "ch_ids_sql")
                            .replace("linenum", line.toString()));
            if (ids != null && ids.size() != 0) {
                //获取线路所有的车辆结果
                String map = null;
                try {
                    map = XiGetRealTimes(table, collect_type, JSON.toJSONString(ids), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //车号-属性
                Map<String, Map<String, String>> parse = (Map<String, Map<String, String>>) JSON.parse(map);
                //将所有的车遍历计算
                //获取现在的服务器时间
                long l = System.currentTimeMillis();

                parse.forEach((k, v) -> {

                    //如果是过滤的话会执行下面的代码
                    if (tableEx.equals(object_name)) {
                        //时间大于约定的时间
                        if (Math.abs(l - Long.parseLong(v.get("COLLECT_TIME"))) > Long.parseLong(timeEx)) {
                            return;
                        }
                    }
                    if (!parse.isEmpty()) {
                        //这里计算的是车辆的结果需要将所有的相加才可以 属性：值
                        Map<String, String> chMap = TXIFilterStepsUtil.filterMap(v, ks);
                        chMap.forEach((ck, cv) -> {
                            Map<String, String> lineMap = result.get(line.toString());
                            if (lineMap != null) {
                                String value = lineMap.get(ck);
                                double lastV = 0;
                                if (value != null) {
                                    lastV = Double.parseDouble(value);
                                }
                                double v1 = Double.parseDouble(cv);
                                lastV += v1;
                                lineMap.put(ck, lastV + "");
                            } else {
                                lineMap = new HashMap<>();
                                double v1 = Double.parseDouble(cv);
                                lineMap.put(ck, v1 + "");
                            }
                            result.put(line.toString(), lineMap);
                        });
                    }
                });

            }
        });
        return JSON.toJSONString(result);
    }

    /**
     * 多线路
     *
     * @param object_name        对象名
     * @param collect_type       采集类型
     * @param lineList           线路列表
     * @param keys               属性列表
     * @param start_time         开始时间
     * @param end_time           结束时间
     * @param max_num            最大数量
     * @param filter_mode        过滤模式
     * @param query_time_format  查询时间格式
     * @param return_time_format 返回时间格式
     * @param model              平台Model
     * @return result
     * @throws Exception 自定义异常
     */

    public static String XiGetMutiMachineByLineSum(String object_name, String collect_type, String lineList, String keys,
                                                   String start_time, String end_time, String max_num,
                                                   String filter_mode, String query_time_format,
                                                   String return_time_format, TXIModel model) throws Exception {
        TXIBDBApi bigDBApi = new TXIBDBApi();
        return bigDBApi.XiGetMutiMachineByLineSum(object_name, collect_type, lineList, keys,
                start_time, end_time, max_num, filter_mode, query_time_format,
                return_time_format);
    }


    /**
     * 批量保存数据到redis 和cassandra
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param jsonList     属性数组
     * @throws Exception
     */
//    public static void XiSave2Dbs(String object_name, String machine_id, String collect_type, String jsonList, TXIModel model) throws Exception {
//        if (collect_type == null || collect_type.equals("")) {
//            collect_type = Constants.DEFAULT_COLLECTTYPE;
//        }
//        //redis 批量插入
//        TXIInMemoryDBApi inMemoryDBApi = new TXIInMemoryDBApi();
//        inMemoryDBApi.save2InMemoryDBBatch(object_name, machine_id, collect_type, jsonList);
//
//        //cassandra 插入
//        TXIBDBApi txibdbApi = new TXIBDBApi();
//        txibdbApi.save2BigDBs(object_name, machine_id, collect_type, jsonList);
//    }

    /**
     * 获取补录数据
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param model        平台
     * @return result
     * @throws Exception 自定义异常
     */
    public static String XiGetMakeupData(String object_name, String machine_id, String collect_type, TXIModel model) throws Exception {
        TXIBDBApi txibdbApi = new TXIBDBApi();
        String markupData = txibdbApi.getMarkupData(object_name, machine_id, collect_type);
        return markupData;
    }

    /**
     * 删除补录数据单条
     *
     * @param object_name  对象命
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param collect_time 采集时间
     * @param model        平台
     * @throws Exception 自定义异常
     */
    public static void XiDelMakeupData(String object_name, String machine_id, String collect_type, String collect_time, TXIModel model) throws Exception {
        TXIBDBApi txibdbApi = new TXIBDBApi();
        txibdbApi.delMarkupData(object_name, machine_id, collect_type, collect_time);
    }

    /**
     * 批量删除补录数据
     *
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param timeList     采集世纪数组
     * @param model        平台
     * @throws Exception 自定义异常
     */
    public static void XiDelMakeups(String object_name, String machine_id, String collect_type, String timeList, TXIModel model) throws Exception {
        TXIBDBApi txibdbApi = new TXIBDBApi();
        txibdbApi.delMarkups(object_name, machine_id, collect_type, timeList);
    }


    /**
     * 获取某一个采集点得聚合数据
     *
     * @param object_name 数据库名字
     * @param tagNameList 采集点的名字
     * @param time        统计的时间
     * @param valueType   需要统计的类型(年月日)
     * @param model       平台
     * @return result
     * @throws Exception 自定义异常
     */
    public static String XiCalculation(String object_name, String tagNameList, String time, String valueType, TXIModel model) throws Exception {
        TXICalcula txiCalcula = new TXICalcula();
        String s = txiCalcula.XIGetCalculationResult(object_name, tagNameList, time, valueType);
        return s;
    }


    /**
     * 更新协议，主要策略为更新redis里面的参数，然后在spark里面去自动更新
     *
     * @param object_name 对象名
     * @param proId       协议ID
     * @param model       平台
     */
    public static void XiUpdatePro(String object_name, String proId, TXIModel model) {

        //处理&符号
        if (proId.indexOf("%26") != -1) {
            proId = proId.replace("%26", "&");
        }

        Map<String, String> mapStatus = new HashMap<>();
        mapStatus = RedisUtil.getHgetAllCluster("protocol");
        mapStatus.forEach((k,v)->{
            RedisUtil.hset(object_name,k,"0");
        });

        RedisUtil.hset(object_name, proId, "1");
        RedisUtil.hset(object_name, "change", "1");

        //发布订阅使用
        LOGGER.error("开始推送：" + proId);
        RedisUtil.pub("update_status", proId);
    }


    /**
     * 计算表达式结果
     *
     * @param jexl    表达式
     * @param para    属性名
     * @param decimal 保留几位小数
     * @param model   平台model
     * @return result 运算结果
     */
    public static String XiJexl(String jexl, String para, String decimal, TXIModel model) {
        return JexlUtil.execJexl(jexl, JSON.parseObject(para, Map.class), Integer.parseInt(decimal));
    }

    /**
     * 获取时间段大数据
     *
     * @param object_name        对象持久化名，例如：OPC_DATA
     * @param query_json_keys    键值
     * @param start_time         起始时间
     * @param end_time           结束时间
     * @param max_num            最大记录数 -1 不做数据过滤，有多少数据给多少数据, 大于等于0 ,则提取出的数据如果大于最大数,则只给出最大数的个数，
     * @param filter_mode        最大数过滤策略，1
     *                           自动调整（按照总记录数与最大记录数做映射做过滤，例如10000对1000
     *                           ，每隔10个点做一个提取）
     * @param query_time_format  查询时间的格式   1.时间戳格式 2.字符串格式 yyyy/MM/dd HH:mm格式
     * @param return_time_format 返回时间格式  1.时间戳格式 2.字符串格式 yyyy/MM/dd HH:mm格式
     * @param collect_type       采集类型
     * @param machine_id         机器ID
     * @param  time              时间间隔
     * @param model              平台
     * @return result
     * @throws Exception 自定义异常
     */
    public static String XiGetGroupPeriodsData(String object_name, String collect_type, String machine_id, String query_json_keys,
                                          String start_time, String end_time, String max_num,
                                          String filter_mode, String query_time_format,
                                          String return_time_format,String time, TXIModel model) throws Exception {
        String json= TXICassandraV2Impl.XiGetGroupPeriodsData(object_name, collect_type, machine_id, query_json_keys,
                start_time, end_time, max_num, filter_mode, query_time_format,
                return_time_format,time);
        return json;

    }
}
