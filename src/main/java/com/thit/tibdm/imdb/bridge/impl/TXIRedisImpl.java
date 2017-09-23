package com.thit.tibdm.imdb.bridge.impl;


import com.alibaba.fastjson.JSON;
import com.thit.tibdm.api.TXIBDMApi;
import com.thit.tibdm.db.bridge.api.TXIBDBApi;
import com.thit.tibdm.imdb.bridge.TXIInMemoryDB;
import com.thit.tibdm.imdb.bridge.util.RedisUtil;
import com.thit.tibdm.util.Constants;
import com.thit.tibdm.util.ResourceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rmbp13 on 16/9/20.
 *
 * @author wanghaoqiang
 */
public class TXIRedisImpl implements TXIInMemoryDB {
    /**
     * @param object_name  对象名
     * @param machine_id   机器ID
     * @param collect_type 采集类型
     * @return String
     */
    public String getRealTimeData(String object_name, String machine_id, String collect_type) {
        String value;
        //依据carId和type拼成的key拿到值value
        value = RedisUtil.getHgetCluster(object_name, machine_id + "-" + collect_type);
        Map parse = new HashMap();
        String s;
        if (value != null) {
            Object obj = JSON.parse(value);
            if (obj != null) {
                Map map = (Map) obj;
                parse.put(map.get("CH"), map);
            }
        }
        s = JSON.toJSONString(parse);
        return s;
    }

    /**
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machineIds   机器ID数组
     * @return String
     */
    public String getRealTimeDataBatch(String object_name, String collect_type, String[] machineIds) {
        //将ID组遍历出来然后拼起来
        System.out.println(object_name + collect_type + machineIds + "=======================");
        for (int i = 0; i < machineIds.length; i++) {
            machineIds[i] = machineIds[i] + "-" + collect_type;
        }
        //存入redis,key-object_name+车号ID+采集类型,value-json
        //判断模式是cluster则用集群模式的API否则则用单例模式的API
        Map<String, Map<String, String>> mapList = new HashMap<>();
        List<String> hmget = RedisUtil.getHmgetCluster(object_name, machineIds);
        for (String map : hmget) {
            Map<String, String> parse = new HashMap();
            Object obj = JSON.parse(map);
            if (obj != null) {
                parse = (Map) obj;
                mapList.put(parse.get("CH"), parse);
            }
        }

        String result = JSON.toJSONString(mapList);
        return result;
    }

    /**
     * @param object_name  对象名
     * @param machine_id   机器ID
     * @param collect_type 采集类型
     * @param json         其他属性
     * @return String  字符串
     * @throws Exception 异常
     */
    public String save2InMemoryDB(String object_name, String machine_id, String collect_type, String json) throws Exception {
        //首先判断新来得json是否有以前的时间大,如果没有的话则不插入
        boolean b = RedisUtil.comparaCollectTime(TXIBDMApi.XiGetRealTimeData(object_name, machine_id, collect_type, null), json);
//        boolean b = true;
        String result = insertRedis(b, object_name, machine_id, collect_type, json);
        return result;
    }

    /**
     * @param object_name  对象名
     * @param machine_id   机器ID
     * @param collect_type 采集类型
     * @param jsonList     其他属性
     * @return String 字符串
     * @throws Exception 异常
     */
    public String save2InMemoryDBBatch(String object_name, String machine_id, String collect_type, String jsonList) throws Exception {
        List<Map> list = (List) JSON.parse(jsonList);
        boolean b;
        String result = "1";
        String realTime = TXIBDMApi.XiGetRealTimeData(object_name, machine_id, collect_type, null);
        //优化批量的代码,对于实时的数据可以先计算出时间最大的值,然后再去执行插入,对于补录的数据,可以收集为一个集合,然后在执行批插入
        List makeupList = new ArrayList();
        String max = null;
        for (Map map : list) {
            String s = JSON.toJSONString(map);
            //判断新来的时间是否大于原来的实时数据
            b = RedisUtil.comparaCollectTime(realTime, s);
            if (b) {
                max = ((Map) JSON.parse(s)).get("COLLECT_TIME").toString();
            } else {
                makeupList.add(map);
            }
        }
        //将最大值插入redis
        if (max != null) {
            RedisUtil.saveHashCluster(object_name, machine_id + "-" + collect_type, max);
        }

        //将补录数据列表批量插入补录表中
//        TXICassandraV2Impl impl = new TXICassandraV2Impl();
//        impl.saveMakeupDataBatch(makeupList);

//        result = insertRedis(b, object_name, machine_id, collect_type, s);
        return result;
    }

    /**
     * 插入数据,并判断是否collect_time大于之前数据的时间,如果大于的话才插入
     *
     * @param b            是否大于输入的数据
     * @param object_name  对象名
     * @param collect_type 采集类型
     * @param machine_id   机器ID
     * @param json         其他属性
     * @return String   字符串
     * @throws Exception 异常
     */
    public static String insertRedis(Boolean b, String object_name, String machine_id, String collect_type, String json) throws Exception {
        String result = "1";
        if (b) {
            //存入redis,key-object_name+车号ID+采集类型,value-json
            //判断模式是cluster则用集群模式的API否则则用单例模式的API
            result = RedisUtil.saveHashCluster(object_name, machine_id + "-" + collect_type, json);
        } else {
            //如果开启的话则去保存数据
            if (ResourceUtil.getCassandraProValue("makeup_swith").equals("0")) {
                System.out.println(json);
//            if (true) {
                Map map = (Map) JSON.parse(json);
                //保存补录数据到cassandra
                TXIBDBApi txibdbApi = new TXIBDBApi();
                txibdbApi.saveMarkupData(object_name, machine_id, collect_type, map.get(Constants.COLLECT_TIME).toString());
            }
        }
        return result;
    }
}
