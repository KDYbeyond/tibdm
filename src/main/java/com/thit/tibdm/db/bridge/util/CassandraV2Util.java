package com.thit.tibdm.db.bridge.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.datastax.driver.core.Row;
import com.thit.tibdm.db.bridge.stru.CassandraV2Attr;
import com.thit.tibdm.db.bridge.stru.CassandraV2CollectInfo;
import com.thit.tibdm.db.bridge.stru.QueryedCassandraV2CollectInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on 16/9/21.
 *
 * @author wanghaoqiang
 */
public class CassandraV2Util {

    /**
     * 将字符串转为时间戳传入参数 "yyyy/MM/dd HH:mm:ss"格式的时间转为时间戳
     *
     * @param strTime 开始时间
     * @return String 返回
     * @throws ParseException 异常
     */
    public static String getTimeStamp(String strTime) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = simpleDateFormat.parse(strTime);
        String timeStemp = date.getTime() + "";
        return timeStemp;

    }

    /**
     * 将时间戳转为字符串
     *
     * @param cc_time 时间
     * @return String
     */
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss SSS");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time));

        return re_StrTime;
    }

    /**
     * 将map转为实体类
     *
     * @param map 集合
     * @return CassandraV2CollectInfo
     */
    public static CassandraV2CollectInfo mapToCollectInfo(Map map) {
        CassandraV2CollectInfo ci = new CassandraV2CollectInfo();
        Map map1 = new HashMap();
        for (Object o : map.entrySet()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) o;
            String key = entry.getKey().toUpperCase();
            String value = entry.getValue();
            if (key.equals(CassandraV2Table.collect_type)) {
                ci.setCollectType(value);
            } else if (key.equals(CassandraV2Table.collect_time)) {
                ci.setCollectTime(Long.parseLong(value));
            } else if (key.equals(CassandraV2Table.receive_time)) {
                ci.setReceiveTime(value + "");
            } else if (key.equals(CassandraV2Table.machine_id)) {
                ci.setMachineId(value);
            } else if (!entry.getKey().equals(CassandraV2Table.collect_type) && !entry.getKey().equals(CassandraV2Table.collect_time) && !entry.getKey().equals(CassandraV2Table.receive_time) && !entry.getKey().equals(CassandraV2Table.machine_id)) {
                map1.put(key, value);
                ci.setAttr(map1);
            }

        }

        return ci;
    }

    //过滤不需要查询的属性，若查询条件不包含属性则全部返回


//    //过滤不需要查询的属性，若查询条件不包含属性则全部返回
//    public static CassandraV2CollectInfo filterMap(CassandraV2CollectInfo queryJsonKeysCi, CassandraV2CollectInfo ci) {
//
//        Map map = getCassandraV2CollectInfoMap(queryJsonKeysCi, ci.getAttr());
//        ci.setAttr(map);
//        return ci;
//
//    }

    //返回查询结果   只把需要查询的属性放到map里
//    public static Map<String, String> getCassandraV2CollectInfoMap(CassandraV2CollectInfo queryJsonKeysCi, Map<String, String> map) {
//        Map<String, String> map1 = new HashMap<>();
//        List<CassandraV2Attr> attrList = queryJsonKeysCi.getAttrList();
//        String key = null;
//        String value = null;
//        if (attrList != null) {
//            for (int i = 0; i < attrList.size(); i++) {
//
//                //全部属性中包含要查询的属性
//                if (map.containsKey(attrList.get(i).getAttrName())) {
//                    //key为ATTR_NAME1
//                    key = attrList.get(i).getAttrName();
//                    value = map.get(attrList.get(i).getAttrValue());
//                    map1.put(attrList.get(i).getAttrName(), map.get(attrList.get(i).getAttrName()));
//                }
//            }
//        } else {
//            map1 = map;
//        }
//
//
//        return map1;
//    }

    //查询条件映射为实例对象   属性放到attrList中

    /**
     * @param queryJsonKeys 接送查询关键字
     * @return CassandraV2CollectInfo
     */
    public static CassandraV2CollectInfo getQueryJsonKeys(String queryJsonKeys) {

        Map queryJsonKeysMap = JSON.parseObject(queryJsonKeys);
        CassandraV2CollectInfo ci = new CassandraV2CollectInfo();

        List<CassandraV2Attr> attrList = new ArrayList<CassandraV2Attr>();
        for (Object o : queryJsonKeysMap.entrySet()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) o;

            String key = entry.getKey().toUpperCase();
            //非ARRT_LIST对应的value
            String value = "";
            List valueList = new ArrayList();
            if (key.equals(CassandraV2Table.attr_list)) {
                valueList = (List) entry.getValue();
            } else {
                value = (String) entry.getValue();
            }
            if (key.equals(CassandraV2Table.collect_type)) {
                ci.setCollectType(value);
            } else if (key.equals(CassandraV2Table.machine_id)) {
                ci.setMachineId(value);
            } else if (key.equals(CassandraV2Table.collect_time)) {
                ci.setCollectTime(Long.parseLong(value));
                //加!
            } else if (key.equals(CassandraV2Table.attr_list)) {

                Map map3 = new HashMap();
                for (int i = 0; i < valueList.size(); i++) {
                    CassandraV2Attr attr = new CassandraV2Attr();
                    map3 = JSON.parseObject(valueList.get(i).toString());
                    //key是ATTR_NAME1 value是属性名
                    attr.setAttrName((String) map3.get("attr_name"));
                    attr.setAttrValue("attr_name");
                    attrList.add(attr);
                    System.out.println(attr.getAttrName());
                }
                ci.setAttrList(attrList);
            }
        }
        return ci;
    }


    /**
     * 转换collecttime类型
     *
     * @param queryedCi 查询Ci
     * @param map       集合
     * @return 返回
     */
    public static Map queryedCollectTimeLongMap(CassandraV2CollectInfo queryedCi, Map<String, String> map) {
        Map returnMap = new HashMap();
        returnMap.put(CassandraV2Table.collect_time, queryedCi.getCollectTime());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }

    /**
     * @param returnTimeFormat 返回时间格式
     * @param mapAfterMaxNum   集合最大数
     * @param mapFilter        集合过滤
     * @return mapFilter
     */
    public static Map<String, String> getFilterMapCollectTimeString(String returnTimeFormat, Map mapAfterMaxNum, Map mapFilter) {

        if (returnTimeFormat != null && returnTimeFormat.equals(TXIBigDBJNDINames.TIMESTEP_FORMAT)) {
            mapFilter.put(CassandraV2Table.collect_time, (mapAfterMaxNum.get(CassandraV2Table.collect_time) + ""));
        } else if (returnTimeFormat != null && returnTimeFormat.equals(TXIBigDBJNDINames.STRING_TIME_FORMAT)) {
            mapFilter.put(CassandraV2Table.collect_time, (String) mapAfterMaxNum.get(CassandraV2Table.collect_time));

        }

        return mapFilter;
    }

    /**
     * @param queryedCi 查询Ci
     * @param map       集合
     * @return returnMap
     */
    public static Map queryedCollectTimeStringMap(QueryedCassandraV2CollectInfo queryedCi, Map<String, String> map) {
        Map returnMap = new HashMap();
        returnMap.put(CassandraV2Table.collect_time, queryedCi.getCollectTime());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }

    /**
     * 删除ArrayList中重复元素
     *
     * @param list 参数
     * @return list 参数
     */
    public static List<String> removeDuplicate(List<String> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * [{"zt1+zt2+zt3":["zt1","zt2","zt3"]},{"zt4+zt5+zt6":["zt4","zt5","zt6"]},"zt7","zt8"]
     * 转换为 zt1 zt2 zt3 zt4 zt5 zt6 zt7 zt8
     *
     * @param keys 关键字
     * @return reList
     */
    public static List<String> getTongjiList(String keys) {
        List<String> reList = new ArrayList<>();
        //[{"zt1+zt2+zt3":["zt1","zt2","zt3"]},{"zt4+zt5+zt6":["zt4","zt5","zt6"]},"zt7","zt8"]
        JSONArray ks = JSON.parseArray(keys);
        List ksList = (List) ks;
        for (int shuxing = 0; shuxing < ksList.size(); shuxing++) {//属性循环
            Object j = ksList.get(shuxing);//属性
            String shuxingkey = "";
            List<String> shuxingvalue = new ArrayList<>();
            if (j instanceof String) {//"zt7","zt8"
                shuxingkey = (String) j;
                reList.add(shuxingkey);
            } else {//{"zt1+zt2+zt3":["zt1","zt2","zt3"]},{"zt4+zt5+zt6":["zt4","zt5","zt6"]}
                Map<String, List<String>> para = (Map<String, List<String>>) j;

                for (Object o : para.entrySet()) {
                    Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) o;
                    shuxingvalue = entry.getValue();//是list类型["zt1","zt2","zt3"]
                    for (int i = 0; i < shuxingvalue.size(); i++) {
                        reList.add(shuxingvalue.get(i));//"zt1","zt2","zt3"
                    }
                }
            }

        }
        reList = removeDuplicate(reList);
        return reList;
    }

    /**
     * 返回带collecttime的cql语句
     *
     * @param keys 关键字
     * @return String
     */
    public static String getCql(String keys) {
        List<String> reList = new ArrayList<>();
        reList = getTongjiList(keys);
        reList.add(CassandraV2Table.collect_time);//把COLLECT_TIME查出来
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reList.size(); i++) {
            if (i < reList.size() - 1) {
                sb.append(reList.get(i) + ",");
            } else {
                sb.append(reList.get(i));
            }
        }
        return sb.toString();
    }

    /**
     * @param row        行数
     * @param mapAll     集合
     * @param listForRow 行数
     * @return mapAll
     */
    public static Map getAttrMap(Row row, Map mapAll, List<String> listForRow) {
        for (int i = 0; i < listForRow.size(); i++) {
            //{"zt1":"0"}
            String attr = listForRow.get(i);
            if (!attr.equals("COLLECT_TIME")) {

                String value = row.getString(attr);
                mapAll.put(attr, value);
            } else {
                long value = row.getLong(attr);
                mapAll.put(attr, value);
            }
        }
        return mapAll;
    }

    /**
     * 获取今天的日期
     *
     * @return
     */
    public static String getNowDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    /**
     * 时间戳转日期格式
     *
     * @param time
     * @return
     */
    public static Date getDate(long time) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(time);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 获取两个日期之间的日期
     *
     * @param start_time
     * @param end_time
     * @return
     */
    public static List<String> getBetweenDates(long start_time, long end_time) {
        List<String> result = new ArrayList<>();
        Date start = getDate(start_time);
        Date end = getDate(end_time);
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
//        tempStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);

        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        while (tempStart.before(tempEnd)) {
            result.add(format1.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    /**
     * 获取日期cql语句
     *
     * @param start_time
     * @param end_time
     * @return
     */
    public static String getCqlDate(long start_time, long end_time) {
        List<String> list = getBetweenDates(start_time, end_time);
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        for (int i = 0; i < list.size(); i++) {

            if (i < (list.size() - 1)) {
                sb.append("'" + list.get(i) + "' ,");
            } else {
                sb.append(("'" + list.get(i) + "'"));
            }
        }
        sb.append(")");
        return sb.toString();
    }

}
