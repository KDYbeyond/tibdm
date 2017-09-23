package com.thit.tibdm.db.bridge.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datastax.driver.core.*;
import com.thit.tibdm.db.bridge.TXIBigDB;
import com.thit.tibdm.db.bridge.stru.CassandraV2CollectInfo;
import com.thit.tibdm.db.bridge.stru.CassandraV2Markup;
import com.thit.tibdm.db.bridge.stru.QueryedCassandraV2CollectInfo;
import com.thit.tibdm.db.bridge.util.*;
import com.thit.tibdm.util.DateUtil;
import com.thit.tibdm.util.ResourceUtil;
import com.thit.tibdm.util.TXIFilterStepsUtil;
import com.thit.tibdm.util.TXISmallUtil;
import com.xicrm.common.TXISystem;
import com.xicrm.exception.TXIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by rmbp13 on 16/9/20.
 * <p>
 * closeconn方法
 *
 * @Override问题
 */
public class TXICassandraV2Impl extends TXIBigDBBaseImpl implements TXIBigDB {
    /**
     * 日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(TXICassandraV2Impl.class);
    /**
     * 关键字
     */
    private static String KEYSPACENAME = "keySpaceName";
    /**
     * 数据
     */
    private static String MAKEUP_DATA = "makeup_table";

    public static String XiGetGroupPeriodsData(String object_name, String collect_type, String machine_id, String queryJsonKeys,
                                               String startTime, String endTime, String maxNum, String filterMode,
                                               String queryTimeFormat, String returnTimeFormat, String time) throws Exception {

        //检查参数的个数
        TXISmallUtil.mustCheck("表名", object_name);
        TXISmallUtil.mustCheck("采集类型", collect_type);
        TXISmallUtil.mustCheck("机器ID", machine_id);
        TXISmallUtil.mustCheck("查询条件", queryJsonKeys);
        TXISmallUtil.mustCheck("起始时间", startTime);
        TXISmallUtil.mustCheck("结束时间", endTime);
        //解析查询条件json字符串
        JSONArray ks = JSON.parseArray(queryJsonKeys);
        List<String> listForRow = CassandraV2Util.getTongjiList(queryJsonKeys);

        ArrayList list = new ArrayList();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String keys = CassandraV2Util.getCql(queryJsonKeys);//查询条件拼接 得到ZT20,ZT1,ZT2,ZT30,ZT50
        //根据时间段获取分组
        long start_time = 0l;
        long end_time = 0l;

        start_time = getLongTime(queryTimeFormat, startTime);
        end_time = getLongTime(queryTimeFormat, endTime);

        long time1 = end_time - start_time;
        int group_Num = (int) (time1 / (Integer.parseInt(time) * 1000));
        Map<Integer, Map> map = new HashMap<>();
        for (int i = 0; i < group_Num; i++) {
            Map map1 = new HashMap<>();
            map.put(i, map1);
        }

        String cql = "";
        cql = "select " + keys + " from " + ResourceUtil.getCassandraProValue(KEYSPACENAME) + "." + object_name + " where " + CassandraV2Table.machine_id + " = " + "'" + machine_id + "'"
                + " and " + CassandraV2Table.collect_date + " in " + CassandraV2Util.getCqlDate(start_time, end_time) + " and " + CassandraV2Table.collect_type + "=" + "'" + collect_type + "'"
                + " and " + CassandraV2Table.collect_time + " > " + start_time + " and " + CassandraV2Table.collect_time + " < " + end_time + ";";

        LOGGER.debug("查询cql===" + cql);

        ResultSet rs = CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
        Iterator<Row> iterator = rs.iterator();
        Row row = null;

        Map<String, String> mapFilter = new HashMap<>();
        Map<Integer, Map> map1 = new HashMap<>();
        int g = 0;
        int group = 0;
        String aLong = "";
        long rowcollecttime = 0l;
        while (iterator.hasNext()) {
            row = iterator.next();
            //根据查询属性从row中取出结果
            rowcollecttime = row.getLong(CassandraV2Table.collect_time);
            aLong = rowcollecttime + "";

            g = Integer.parseInt(time) * 1000;
            group = (int) ((rowcollecttime - start_time) / g);

            if (!map1.containsKey(group)) {
                Map mapAll = new HashMap<>();
                if (returnTimeFormat != null && returnTimeFormat.equals(TXIBigDBJNDINames.TIMESTEP_FORMAT)) {
                    //返回时间戳格式
                    mapAll.put(CassandraV2Table.collect_time, aLong);
                } else if (returnTimeFormat != null && returnTimeFormat.equals(TXIBigDBJNDINames.STRING_TIME_FORMAT)) {
                    //返回yyyy/MM/dd HH:mm:ss SSS格式
                    mapAll.put(CassandraV2Table.collect_time, CassandraV2Util.getStrTime(aLong));
                }
                mapAll = CassandraV2Util.getAttrMap(row, mapAll, listForRow);
                map1.put(group, mapAll);
            }
        }


        for (Map.Entry<Integer, Map> entry : map1.entrySet()) {
            int key = entry.getKey();
            Map value = entry.getValue();
            map.put(key, value);
        }

        for (Map.Entry<Integer, Map> entry : map.entrySet()) {
            Map value = entry.getValue();
            list.add(value);
        }

//        List toList = TXIFilterStepsUtil.getFilterStepsList(list, maxNum, filterMode);
        Map mapAfterMaxNum;
        for (int i = 0; i < list.size(); i++) {
            mapAfterMaxNum = (Map) list.get(i);
            mapFilter = TXIFilterStepsUtil.filterMap(mapAfterMaxNum, ks);//对list过滤完的map进行累加
            //为mapfilter加上collecttime
            mapFilter.put(CassandraV2Table.collect_time, (String) mapAfterMaxNum.get(CassandraV2Table.collect_time));
            jsonArray.add(mapFilter);
        }
        jsonObject.put("total", list.size());
        //处理jsonarray
        jsonObject.put("rows", jsonArray);
        return jsonObject.toJSONString();
    }

    public static long getLongTime(String timeFormat, String time) {

        long time1 = 0l;
        if (timeFormat != null && timeFormat.equals(TXIBigDBJNDINames.TIMESTEP_FORMAT)) {
            //查询格式为时间戳格式
            time1 = Long.parseLong(time);

        } else if (timeFormat != null && timeFormat.equals(TXIBigDBJNDINames.STRING_TIME_FORMAT)) {
            //查询格式为时间字符串式
            time1 = DateUtil.getTime(time);
        }

        return time1;
    }

    /**
     * 多线程版本
     *
     * @param object_name      名字
     * @param collect_type     类型
     * @param machine_ids      id
     * @param queryJsonKeys    关键字
     * @param startTime        开始时间
     * @param endTime          结束时间
     * @param maxNum           最大值
     * @param filterMode       过滤
     * @param queryTimeFormat  查询
     * @param returnTimeFormat 返回时间
     * @return 返回值 XiGetGroupPeriodsData
     */
    public static String XiGetMutiMachineGroupPeriodsData(String object_name, String collect_type, String machine_ids, String queryJsonKeys,
                                                          String startTime, String endTime, String maxNum, String filterMode,
                                                          String queryTimeFormat, String returnTimeFormat, String time) {

        ExecutorService exec = Executors.newFixedThreadPool(60);
        JSONArray machine_idArray = JSON.parseArray(machine_ids);
        Map map = new ConcurrentHashMap();
        //多线程去执行查询
//        LOGGER.error("开始执行多线程查询");
//        LOGGER.error("查询的车号为：" + machine_ids);
//        LOGGER.error("queryJsonKeys为：" + queryJsonKeys);
//        LOGGER.error("object_name为：" + object_name+"  collect_type为：" + collect_type+"  machine_ids为：" + machine_ids+"  startTime为：" + startTime+"  endTime为："+endTime+"  maxNum为："+maxNum+"//"+filterMode+"//"+queryTimeFormat+"//"+returnTimeFormat);

        long s = System.currentTimeMillis();
        machine_idArray.forEach(machine_id -> exec.execute(() -> {
            String json;
            try {
                json = XiGetGroupPeriodsData(object_name, collect_type, machine_id.toString(), queryJsonKeys, startTime, endTime, maxNum, filterMode, queryTimeFormat, returnTimeFormat, time);
//               LOGGER.error("json==="+json);
                Map map1 = JSON.parseObject(json);
                map.put(machine_id, map1);
            } catch (Exception e) {
                LOGGER.error(e.toString());
                e.printStackTrace();
            }
        }));
        exec.shutdown();
        //等待所有的线程执行完毕再返回数据
        while (true) {
            if (exec.isTerminated()) {
                System.out.println("结束了！");
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long e = System.currentTimeMillis();
        String jsonRe = JSON.toJSONString(map);
//        LOGGER.error("返回的结果为：" + jsonRe);
        LOGGER.debug("消耗的时间为：" + (e - s));
        return jsonRe;
    }

    /**
     * 分组多线路
     **/

    public static String XiGetGroupPeriodsByLineSum(String object_name, String collect_type, String lineList, String keys, String start_time, String end_time, String max_num, String filter_mode, String
            query_time_format, String return_time_format, String time) {
        //因为machine_id里的值是线路l01,l02所以转换为查多车 每一条线路就是一列车
        String lineJson = "";
        //查出多线路
        lineJson = XiGetMutiMachineGroupPeriodsData(object_name, collect_type, lineList, keys, start_time, end_time, max_num, filter_mode, query_time_format, return_time_format, time);

        return lineJson;
    }

    /**
     * @param object_name  对象名
     * @param machine_id   机器ID
     * @param collect_type 采集类型
     * @param json         其他属性
     */
    public void save2BigDB(String object_name, String machine_id, String collect_type, String json) {
        //json字符串转java对象
        Map map = (Map) JSON.parseObject(json);
        CassandraV2CollectInfo ci = CassandraV2Util.mapToCollectInfo(map);
        String cql = "INSERT INTO " + ResourceUtil.getCassandraProValue(KEYSPACENAME) + "."
                + object_name + " (" + CassandraV2Table.machine_id + ","
                + CassandraV2Table.collect_type + "," + CassandraV2Table.collect_time + ","
                + CassandraV2Table.attr + "," + CassandraV2Table.receive_time + ","
                + CassandraV2Table.save_time + ") values" + " (?,?,?,?,?,?);";
//        TXISystem.log.info(this, "保存数据save2BigDB()执行了cql语句" + cql);
        String saveTime = System.currentTimeMillis() + "";
        //不使用批处理
        PreparedStatement prepared = CassandraSingleConnect.INSTANCE.getInstance().prepare(cql);
        BoundStatement bound = prepared.bind(machine_id, collect_type, ci.getCollectTime(),
                ci.getAttr(), ci.getReceiveTime(), saveTime);
//        CassandraSingleConnect.INSTANCE.getInstance().execute(bound);
        CassandraSingleConnect.INSTANCE.getInstance().execute(bound);
    }

    /**
     * 多车接口
     *
     * @param object_name      表名
     * @param collect_type     采集类型
     * @param machine_ids      车号
     * @param queryJsonKeys    查询条件
     * @param startTime        起始时间
     * @param endTime          结束时间
     * @param maxNum           最大记录数
     * @param filterMode       过滤策略
     * @param queryTimeFormat  查询时间格式
     * @param returnTimeFormat 返回时间格式
     * @return 返回字符串
     * @throws Exception
     */

    public String XiGetMutiMachinePeriodsDataFa(String object_name, String collect_type, String machine_ids, String queryJsonKeys,
                                                String startTime, String endTime, String maxNum, String filterMode,
                                                String queryTimeFormat, String returnTimeFormat) {

        JSONArray machine_idArray = JSON.parseArray(machine_ids);
        Map map = new HashMap<>();
        machine_idArray.forEach(machine_id -> {
            try {
                String json = XiGetPeriodsData(object_name, collect_type, machine_id.toString(), queryJsonKeys, startTime, endTime, maxNum, filterMode, queryTimeFormat, returnTimeFormat);
                Map map1 = JSON.parseObject(json);
                map.put(machine_id, map1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        String jsonRe = JSON.toJSONString(map);
        return jsonRe;
    }

    /**
     * 多线程版本
     *
     * @param object_name      名字
     * @param collect_type     类型
     * @param machine_ids      id
     * @param queryJsonKeys    关键字
     * @param startTime        开始时间
     * @param endTime          结束时间
     * @param maxNum           最大值
     * @param filterMode       过滤
     * @param queryTimeFormat  查询
     * @param returnTimeFormat 返回时间
     * @return 返回值
     */
    public String XiGetMutiMachinePeriodsData(String object_name, String collect_type, String machine_ids, String queryJsonKeys,
                                              String startTime, String endTime, String maxNum, String filterMode,
                                              String queryTimeFormat, String returnTimeFormat) {

        ExecutorService exec = Executors.newFixedThreadPool(60);
        JSONArray machine_idArray = JSON.parseArray(machine_ids);
        Map map = new ConcurrentHashMap();
        //多线程去执行查询
//        LOGGER.error("开始执行多线程查询");
//        LOGGER.error("查询的车号为：" + machine_ids);
//        LOGGER.error("queryJsonKeys为：" + queryJsonKeys);
//        LOGGER.error("object_name为：" + object_name+"  collect_type为：" + collect_type+"  machine_ids为：" + machine_ids+"  startTime为：" + startTime+"  endTime为："+endTime+"  maxNum为："+maxNum+"//"+filterMode+"//"+queryTimeFormat+"//"+returnTimeFormat);

        long s = System.currentTimeMillis();
        machine_idArray.forEach(machine_id -> exec.execute(() -> {
            String json;
            try {
                json = XiGetPeriodsData(object_name, collect_type, machine_id.toString(), queryJsonKeys, startTime, endTime, maxNum, filterMode, queryTimeFormat, returnTimeFormat);
//               LOGGER.error("json==="+json);
                Map map1 = JSON.parseObject(json);
                map.put(machine_id, map1);
            } catch (Exception e) {
                LOGGER.error(e.toString());
                e.printStackTrace();
            }
        }));
        exec.shutdown();
        //等待所有的线程执行完毕再返回数据
        while (true) {
            if (exec.isTerminated()) {
                System.out.println("结束了！");
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long e = System.currentTimeMillis();
        String jsonRe = JSON.toJSONString(map);
//        LOGGER.error("返回的结果为：" + jsonRe);
        LOGGER.debug("消耗的时间为：" + (e - s));
        return jsonRe;
    }

    /**
     * 多线路
     **/

    @Override
    public String XiGetMutiMachineByLineSum(String object_name, String collect_type, String lineList, String keys, String start_time, String end_time, String max_num, String filter_mode, String
            query_time_format, String return_time_format) {
        //因为machine_id里的值是线路l01,l02所以转换为查多车 每一条线路就是一列车
        String lineJson = "";
        //查出多线路
        lineJson = XiGetMutiMachinePeriodsData(object_name, collect_type, lineList, keys, start_time, end_time, max_num, filter_mode, query_time_format, return_time_format);

        return lineJson;
    }

    /**
     * @param object_name      对象持久化名，例如：opc_data
     * @param collect_type     采集类型
     * @param machine_id       车号
     * @param queryJsonKeys    查询条件
     * @param startTime        起始时间
     * @param endTime          结束时间
     * @param maxNum           最大记录数
     * @param filterMode       过滤策略
     * @param queryTimeFormat  查询时间格式
     * @param returnTimeFormat 返回时间格式
     * @return 字符串
     * @throws Exception 异常
     */
    public String XiGetPeriodsData(String object_name, String collect_type, String machine_id, String queryJsonKeys,
                                   String startTime, String endTime, String maxNum, String filterMode,
                                   String queryTimeFormat, String returnTimeFormat) throws Exception {
        //解析查询条件json字符串
        JSONArray ks = JSON.parseArray(queryJsonKeys);
        List<String> listForRow = CassandraV2Util.getTongjiList(queryJsonKeys);
//        listForRow.add("COLLECT_TIME");

        //检查参数的个数
        TXISmallUtil.mustCheck("表名", object_name);
        TXISmallUtil.mustCheck("采集类型", collect_type);
        TXISmallUtil.mustCheck("机器ID", machine_id);
        TXISmallUtil.mustCheck("查询条件", queryJsonKeys);
        TXISmallUtil.mustCheck("起始时间", startTime);
        TXISmallUtil.mustCheck("结束时间", endTime);

        ArrayList list = new ArrayList();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String keys = CassandraV2Util.getCql(queryJsonKeys);//查询条件拼接 得到ZT20,ZT1,ZT2,ZT30,ZT50
        long start_time = 0l;
        long end_time = 0l;
        if (queryTimeFormat != null && queryTimeFormat.equals(TXIBigDBJNDINames.TIMESTEP_FORMAT)) {
            //查询格式为时间戳格式
            start_time = Long.parseLong(startTime);
            end_time = Long.parseLong(endTime);

        } else if (queryTimeFormat != null && queryTimeFormat.equals(TXIBigDBJNDINames.STRING_TIME_FORMAT)) {
            //查询格式为时间字符串式
            start_time = DateUtil.getTime(startTime);
            end_time = DateUtil.getTime(endTime);
        }
        String cql = "";
        cql = "select " + keys + " from " + ResourceUtil.getCassandraProValue(KEYSPACENAME) + "." + object_name + " where " + CassandraV2Table.machine_id + " = " + "'" + machine_id + "'"
                + " and " + CassandraV2Table.collect_date + " = " + "'" + CassandraV2Util.getNowDate() + "'" + " and " + CassandraV2Table.collect_type + "=" + "'" + collect_type + "'"
                + " and " + CassandraV2Table.collect_time + " > " + start_time + " and " + CassandraV2Table.collect_time + " < " + end_time + ";";
        LOGGER.debug("查询cql===" + cql);

        ResultSet rs = CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
        Iterator<Row> iterator = rs.iterator();
        Row row = null;

        Map<String, String> mapFilter = new HashMap<>();

        while (iterator.hasNext()) {
            row = iterator.next();
            //根据查询属性从row中取出结果
            Map mapAll = new HashMap<>();
            mapAll = CassandraV2Util.getAttrMap(row, mapAll, listForRow);

            String aLong = row.getLong(CassandraV2Table.collect_time) + "";
            if (returnTimeFormat != null && returnTimeFormat.equals(TXIBigDBJNDINames.TIMESTEP_FORMAT)) {
                //返回时间戳格式
                mapAll.put(CassandraV2Table.collect_time, aLong);
            } else if (returnTimeFormat != null && returnTimeFormat.equals(TXIBigDBJNDINames.STRING_TIME_FORMAT)) {
                //返回yyyy/MM/dd HH:mm:ss SSS格式
                mapAll.put(CassandraV2Table.collect_time, CassandraV2Util.getStrTime(aLong));
            }
            list.add(mapAll);

        }
//        map=TXIFilterStepsUtil.getFilterStepsMap(map,maxNum,filterMode);
        List toList = TXIFilterStepsUtil.getFilterStepsList(list, maxNum, filterMode);
        Map mapAfterMaxNum;
        for (int i = 0; i < toList.size(); i++) {
            mapAfterMaxNum = (Map) toList.get(i);
            mapFilter = TXIFilterStepsUtil.filterMap(mapAfterMaxNum, ks);//对list过滤完的map进行累加
            //为mapfilter加上collecttime
            mapFilter.put(CassandraV2Table.collect_time, (String) mapAfterMaxNum.get(CassandraV2Table.collect_time));
            jsonArray.add(mapFilter);
        }
        jsonObject.put("total", toList.size());
        //处理jsonarray
        jsonObject.put("rows", jsonArray);
        return jsonObject.toJSONString();
    }

    public String XiGetPeriodsDataold(String object_name, String collect_type, String machine_id, String queryJsonKeys,
                                      String startTime, String endTime, String maxNum, String filterMode,
                                      String queryTimeFormat, String returnTimeFormat) throws Exception {
        //解析查询条件json字符串
        JSONArray ks = JSON.parseArray(queryJsonKeys);
        List<String> attrList = CassandraV2Util.getTongjiList(queryJsonKeys);
        List<String> listForRow = attrList;
//        listForRow.add("COLLECT_TIME");

        //检查参数的个数
        TXISmallUtil.mustCheck("表名", object_name);
        TXISmallUtil.mustCheck("采集类型", collect_type);
        TXISmallUtil.mustCheck("机器ID", machine_id);
        TXISmallUtil.mustCheck("查询条件", queryJsonKeys);
        TXISmallUtil.mustCheck("起始时间", startTime);
        TXISmallUtil.mustCheck("结束时间", endTime);

        CassandraV2CollectInfo returnCi = new CassandraV2CollectInfo();
        ArrayList list = new ArrayList();
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String cql = CassandraV2Util.getCql(queryJsonKeys);//查询条件拼接 得到ZT20,ZT1,ZT2,ZT30,ZT50
        if (queryTimeFormat != null && queryTimeFormat.equals(TXIBigDBJNDINames.TIMESTEP_FORMAT)) {

            //查询格式为时间戳格式

            cql = "select " + cql + " from " + ResourceUtil.getCassandraProValue(KEYSPACENAME) + "." + object_name + " where " + CassandraV2Table.machine_id + "=" + "'" + machine_id + "'"
                    + " and " + CassandraV2Table.collect_type + "=" + "'" + collect_type + "'"
                    + " and " + CassandraV2Table.collect_time + " >" + Long.parseLong(startTime) + " and " + CassandraV2Table.collect_time + "<" + Long.parseLong(endTime) + ";";
            LOGGER.error("查询cql===" + cql);
//            System.out.println(cql);

        } else if (queryTimeFormat != null && queryTimeFormat.equals(TXIBigDBJNDINames.STRING_TIME_FORMAT)) {
            //查询格式为时间字符串式

            cql = "select " + cql + " from " + ResourceUtil.getCassandraProValue(KEYSPACENAME) + "." + object_name + " where " + CassandraV2Table.machine_id + "=" + "'" + machine_id + "'"
                    + " and " + CassandraV2Table.collect_type + "=" + "'" + collect_type + "'"
                    + " and " + CassandraV2Table.collect_time + " >" + DateUtil.getTime(startTime) + " and " + CassandraV2Table.collect_time + "<" + DateUtil.getTime(endTime) + ";";
            LOGGER.error("查询cql===" + cql);
//            System.out.println(cql);
        }

        ResultSet rs = CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
        Iterator<Row> iterator = rs.iterator();
        Row row = null;
        CassandraV2CollectInfo queryedCi = new CassandraV2CollectInfo();
        String json = "";
        Map mapAll = new HashMap<>();
        Map<String, String> mapFilter = new HashMap<>();
        Map map = new HashMap<>();
        String json1 = "";
        QueryedCassandraV2CollectInfo queryedCassandraV2CollectInfo = new QueryedCassandraV2CollectInfo();

        while (iterator.hasNext()) {
            row = iterator.next();

            if (returnTimeFormat != null && returnTimeFormat.equals(TXIBigDBJNDINames.TIMESTEP_FORMAT)) {
                //返回时间戳格式
                //拿出collecttime
                queryedCi.setCollectTime(row.getLong(CassandraV2Table.collect_time));
                //根据查询属性从row中取出结果
                mapAll = CassandraV2Util.getAttrMap(row, mapAll, listForRow);
                //将map中的属性与collect_time提取出来(而不是从mapfilter中拿)
                map = CassandraV2Util.queryedCollectTimeLongMap(queryedCi, mapAll);
                json1 = JSON.toJSONString(map);
                list.add(json1);
            } else if (returnTimeFormat != null && returnTimeFormat.equals(TXIBigDBJNDINames.STRING_TIME_FORMAT)) {
                //返回yyyy/MM/dd HH:mm:ss SSS格式
                queryedCassandraV2CollectInfo.setCollectTime(CassandraV2Util.getStrTime(row.getLong(CassandraV2Table.collect_time) + ""));
                //根据查询属性从row中取出结果
                mapAll = CassandraV2Util.getAttrMap(row, mapAll, listForRow);
                //将map中的属性与collect_time提取出来
                map = CassandraV2Util.queryedCollectTimeStringMap(queryedCassandraV2CollectInfo, mapAll);
                json1 = JSON.toJSONString(map);
                list.add(json1);
            }
        }

        List toList = TXIFilterStepsUtil.getFilterStepsList(list, maxNum, filterMode);
        Map mapAfterMaxNum;
        for (int i = 0; i < toList.size(); i++) {
            mapAfterMaxNum = JSONObject.parseObject(toList.get(i).toString());//把想要拿的几条数据转为map
            mapFilter = TXIFilterStepsUtil.filterMap(mapAfterMaxNum, ks);//对list过滤完的map进行累加
            //为mapfilter加上collecttime
            mapFilter = CassandraV2Util.getFilterMapCollectTimeString(returnTimeFormat, mapAfterMaxNum, mapFilter);
            jsonArray.add(mapFilter);
        }
        jsonObject.put("total", toList.size());
        //处理jsonarray
        jsonObject.put("rows", jsonArray);
        return jsonObject.toJSONString();
    }

    /**
     * 批量保存接口
     *
     * @param object_name  对象名
     * @param machine_id   机器ID
     * @param collect_type 采集类型
     * @param jsonList
     */
    @Override
    public void save2BigDBs(String object_name, String machine_id, String collect_type, String jsonList) {

        List<String> list = JSON.parseArray(jsonList, String.class);
        BatchStatement batch = new BatchStatement();
        BoundStatement bindStatement;
        batch = new BatchStatement();
        String saveTime = "";
        String cql = "INSERT INTO " + ResourceUtil.getCassandraProValue(KEYSPACENAME) + "."
                + object_name + " (" + CassandraV2Table.machine_id + ","
                + CassandraV2Table.collect_type + "," + CassandraV2Table.collect_time + ","
                + CassandraV2Table.attr + "," + CassandraV2Table.receive_time + ","
                + CassandraV2Table.save_time + ") values" + " (?,?,?,?,?,?);";
        PreparedStatement ps = CassandraSingleConnect.INSTANCE.getInstance().prepare(cql);
        for (String s : list) {
            Map map = JSON.parseObject(s);
            CassandraV2CollectInfo ci = CassandraV2Util.mapToCollectInfo(map);
            saveTime = System.currentTimeMillis() + "";
            bindStatement = ps.bind(machine_id, collect_type, ci.getCollectTime(),
                    ci.getAttr(), ci.getReceiveTime(), saveTime);
            batch.add(bindStatement);
        }
        TXISystem.log.info(this, "保存数据save2BigDB()执行了cql语句" + cql);
        System.out.println("cql====" + cql);
//      System.out.println("ss===="+ci.getMap().toString());
        CassandraSingleConnect.INSTANCE.getInstance().execute(batch);
        batch.clear();
    }

    /**
     * 保存补录数据
     *
     * @param object_name  对象名
     * @param machine_id   机器ID
     * @param collect_type 采集类型
     * @param collect_time 采集时间
     */
    @Override
    public void saveMakeupData(String object_name, String machine_id, String collect_type, String collect_time) {
        //实现保存补录数据的接口

        String cql = "INSERT INTO " + ResourceUtil.getCassandraProValue(KEYSPACENAME) + "."
                + CassandraV2Table.makeup_data + " (" + CassandraV2Table.object_name + "," + CassandraV2Table.machine_id + ","
                + CassandraV2Table.collect_type + "," + CassandraV2Table.collect_time
                + ") values" + " (" + "'" + object_name + "'" + "," + "'" + machine_id + "'" + "," + "'" + collect_type + "'" + "," + Long.parseLong(collect_time) + ");";

        TXISystem.log.info(this, "saveMakeupData()执行了cql:" + cql);
        System.out.println(cql);
        CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
    }

    /**
     * @param list 补录数据列表
     */
    @Override
    public void saveMakeupDataBatch(List<Map> list) {
        BatchStatement batch;
        BoundStatement bindStatement;
        batch = new BatchStatement();

        String cql = "INSERT INTO " + ResourceUtil.getCassandraProValue(KEYSPACENAME) + "."
                + CassandraV2Table.makeup_data + " (" + CassandraV2Table.object_name + "," + CassandraV2Table.collect_type + ","
                + CassandraV2Table.machine_id + "," + CassandraV2Table.collect_time
                + ") values" + " (?,?,?,?)";
        PreparedStatement ps = CassandraSingleConnect.INSTANCE.getInstance().prepare(cql);

        for (Map map : list) {
            bindStatement = ps.bind(map.get(CassandraV2Table.object_name.toUpperCase()), map.get(CassandraV2Table.collect_type.toUpperCase()),
                    map.get(CassandraV2Table.machine_id.toUpperCase()), Long.parseLong(map.get(CassandraV2Table.collect_time.toUpperCase()).toString()));
            batch.add(bindStatement);
        }
        System.out.println("cql====" + cql);
        CassandraSingleConnect.INSTANCE.getInstance().execute(batch);
        batch.clear();
    }

    /**
     * 获取补录数据
     *
     * @param object_name  对象名
     * @param machine_id   采集时间
     * @param collect_type 采集类型
     * @return
     */
    @Override
    public String getMakeupData(String object_name, String machine_id, String collect_type) {
        //实现获取补录数据的接口

        String cql = "select * from " + ResourceUtil.getCassandraProValue(KEYSPACENAME) + "."
                + ResourceUtil.getCassandraProValue(MAKEUP_DATA) + " where " + CassandraV2Table.object_name + "= '"
                + object_name + "' and " + CassandraV2Table.machine_id + "= '" + machine_id + "' "
                + " and " + CassandraV2Table.collect_type + "=" + "'" + collect_type + "';";
        System.out.println(cql);
        String json = "";
        ResultSet rs = CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
        Iterator<Row> iterator = rs.iterator();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            System.out.println("colltime===" + row.getLong(CassandraV2Table.collect_time));
            CassandraV2Markup cassandraV2Markup = new CassandraV2Markup();
            cassandraV2Markup.setMachine_id(row.getString(CassandraV2Table.machine_id));
            cassandraV2Markup.setCollect_time(row.getLong(CassandraV2Table.collect_time));
            cassandraV2Markup.setObject_name(row.getString(CassandraV2Table.object_name));
            cassandraV2Markup.setCollect_type(row.getString(CassandraV2Table.collect_type));
            json = JSON.toJSONString(cassandraV2Markup);
            System.out.println("json=====" + json);
            TXISystem.log.info(this, "补录数据getMakeupData()查询结果为：" + json);
        }

        return json;
    }

    /**
     * 删除补录数据
     *
     * @param object_name  对象名
     * @param machine_id   机器ID
     * @param collect_type 采集类型
     * @param collect_time 采集时间
     */
    @Override
    public void delMakeupData(String object_name, String machine_id, String collect_type, String collect_time) {
        //实现删除单条的数据接口
        String cql = "delete  from " + ResourceUtil.getCassandraProValue(KEYSPACENAME) + "." + ResourceUtil.getCassandraProValue(MAKEUP_DATA)
                + " where " + CassandraV2Table.object_name + "=" + "'" + object_name + "'"
                + "and " + CassandraV2Table.machine_id + "= '" + machine_id + "'"
                + " and " + CassandraV2Table.collect_type + "=" + "'" + collect_type + "'"
                + "and " + CassandraV2Table.collect_time + "= " + Long.parseLong(collect_time)
                + ";";
//        System.out.println(cql);
        TXISystem.log.info(this, "delMakeupData()执行了cql语句:" + cql);
        CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
    }

    /**
     * 批量删除补录数据
     *
     * @param object_name  对象名
     * @param machine_id   机器ID
     * @param collect_type 采集类型
     * @param timeList     时间数组
     */
    @Override
    public void delMakeups(String object_name, String machine_id, String collect_type, String timeList) {
        //实现删除多条补录数据的接口

        List<String> list = JSON.parseArray(timeList, String.class);
        for (int i = 0; i < list.size(); i++) {
            delMakeupData(object_name, machine_id, collect_type, Long.parseLong(list.get(i)) + "");
        }
    }

    /**
     * 抛出异常
     *
     * @return 异常
     */

    public Exception inputException() {
        String errmsg = "传入的collect_type或machine_id与json里面不一致";
        TXISystem.log.error(this, errmsg);
        return new TXIException(errmsg);
    }

    /**
     * 检查参数
     *
     * @param machine_id   车号
     * @param collect_type 采集类型
     * @param json         查询条件
     * @throws Exception 参数异常
     */
    public void checkItems(String machine_id, String collect_type, String json) throws Exception {

        CassandraV2CollectInfo queryJsonKeysCi = CassandraV2Util.getQueryJsonKeys(json);

        if (!collect_type.equals(queryJsonKeysCi.getCollectType()) || !machine_id.equals(queryJsonKeysCi.getMachineId())) {
            throw inputException();
        }
    }

}
