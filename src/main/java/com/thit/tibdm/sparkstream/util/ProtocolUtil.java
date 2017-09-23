package com.thit.tibdm.sparkstream.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.bom.entity.Variable;
import com.thit.tibdm.calculation.DBConnection;
import com.thit.tibdm.db.bridge.util.CassandraSingleConnect;
import com.thit.tibdm.imdb.bridge.util.RedisUtil;
import com.thit.tibdm.util.CreateCql;
import com.thit.tibdm.util.HttpClientKeepSession;
import com.thit.tibdm.util.ProtocolConstants;
import com.thit.tibdm.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;
import java.util.*;

/**
 * @author: dongzhiquan  Date: 2017/7/17 Time: 13:45
 */
public class ProtocolUtil {
    /**
     * 日志
     */
    public static final Logger logger = LoggerFactory.getLogger(ProtocolUtil.class);

    /**
     * 根据协议名称获取一个协议对应的map
     *
     * @param protocolCode 协议码
     * @return 集合
     */
    public static Map<String, MessageProtocol> getProtocolMap(String protocolCode) {
        String pro = "";

        long a = System.currentTimeMillis();
        try {
            pro = HttpClientKeepSession.getProtocal(protocolCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long b = System.currentTimeMillis();
        System.out.println(b - a);
        Map map1 = new HashMap<>();
        map1 = JSON.parseObject(pro);
        Map map2 = (Map) map1.get("data");
        Map map3 = (Map) map2.get("variable");
        Map<String, MessageProtocol> map4 = new HashMap<>();
        map3.forEach((k, v) -> {
            MessageProtocol messageProtocol = JSON.parseObject(v.toString(), MessageProtocol.class);
            map4.put(k.toString(), messageProtocol);
        });
        return map4;
    }

    /**
     * 获取 协议名和协议内容的map  + 车号和协议名的map
     *
     * @param protocolCode
     * @return
     */
    public static Map<String, Object> getProtocolAll(String protocolCode) {
        String pro = "";

        long a = System.currentTimeMillis();
        try {
            pro = HttpClientKeepSession.getProtocal(protocolCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long b = System.currentTimeMillis();
//        System.out.println(b-a);
        Map map1 = new HashMap<>();
        map1 = JSON.parseObject(pro);
        Map<String, Object> map2 = new HashMap<>();
        map2 = (Map<String, Object>) map1.get("data");

        return map2;
    }

    /**
     * 获取协议名和协议的map
     *
     * @param objectMap
     * @return
     */
    public static Map<String, MessageProtocol> getProtocolMap(Map<String, Object> objectMap) {
        Map<String, MessageProtocol> map = new HashMap<>();
        Map map1 = (Map) objectMap.get("variable");
        map1.forEach((k, v) -> {
            MessageProtocol messageProtocol = JSON.parseObject(v.toString(), MessageProtocol.class);
            map.put(k.toString(), messageProtocol);
        });
        return map;
    }

    /**
     * 获取车号和协议的map
     *
     * @param objectMap
     * @return
     */
    public static Map<String, String> getChProtocolMap(Map<String, Object> objectMap) {
        Map<String, String> map = (Map) objectMap.get("vehclePRo");
        return map;
    }

    public static void setProNameZero(String object_name) {
        Map<String, String> mapStatus = new HashMap<>();
        mapStatus = RedisUtil.getHgetAllCluster(object_name);
        mapStatus.forEach((k, v) -> {
            RedisUtil.hset(object_name, k, "0");
        });
    }

    /**
     * 获取车号对应协议的map
     *
     * @param protocolCode 协议码
     * @return Map<String, String> 集合
     */
    public static Map<String, String> getChProtocolMap(String protocolCode) {
        String pro = "";
        try {
            pro = HttpClientKeepSession.getProtocal(protocolCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map map1 = new HashMap<>();
        map1 = JSON.parseObject(pro);
        Map map2 = (Map) map1.get("data");
        Map<String, String> map3 = (Map) map2.get("vehclePRo");

        return map3;
    }

    /**
     * 获取所有的协议名
     *
     * @return List<String>
     */
    public static List<String> getProNames() {
        String sql = ResourceUtil.getProValueByNameAndKey("cassandra-db", "pronames_sql");
        List<String> listBySql = DBConnection.getListBySql(sql);
        String[] s = listBySql.get(0).split(",");
        List<String> list = Arrays.asList(s);
        return list;

    }

    /**
     * 检查是否有新协议在redis里面没有值
     *
     * @param protocol 协议
     * @param change   改变
     */
    public static void checkStatus(String protocol, String change) {
        List<String> proNames = ProtocolUtil.getProNames();
        if (proNames == null) {
            logger.error("proNames==null");
        }

        if (proNames.size() == 0) {
            logger.error("proNames size 为0");
        }
        proNames.forEach(pro -> {
            String proStatus = RedisUtil.hget(protocol, pro);
            if (change == null) {
                RedisUtil.hset(protocol, "change", "0");
            }
            if (proStatus == null) {
                RedisUtil.hset(protocol, pro, "0");
            }
        });
    }

    /**
     * 协议更新成功
     *
     * @param protocolCode 协议码
     */
    public static void updateProOK(String protocolCode) {
        try {
            String sql = ResourceUtil.getProValueByNameAndKey("cassandra-db", "updateok") + "'" + protocolCode + "'";
            DBConnection.executeSql(sql);
            logger.error("执行表示协议更新成功的sql语句成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("执行表示协议更新成功的sql语句失败");
        }
    }


    /**
     * 协议更新失败
     *
     * @param protocolCode 协议码
     */
    public static void updateProNO(String protocolCode) {

        try {
            String sql = ResourceUtil.getProValueByNameAndKey("cassandra-db", "updatefail") + "'" + protocolCode + "'";
            DBConnection.executeSql(sql);
            logger.error("执行表示协议更新失败的sql语句成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("执行表示协议更新失败的sql语句失败");
        }

    }

    /**
     * 根据协议更新修改Cassandra表结构
     */

    public static String change() {

        //修改Cassandra表结构
        List<String> list = CreateCql.getAllList("all");
        String all = RedisUtil.hget("cassandratable", "all");
        JSONArray parse = (JSONArray) JSON.parse(all);
        List<String> list1 = (List) parse;
        if (!list.containsAll(list1) && list1.containsAll(list)) {
            for (int i = 0; i < list1.size(); i++) {

                for (int j = 0; j < list.size(); j++) {

                    if (!list.contains(list1.get(i))) {

                        String s = list1.get(i);
                        //批处理

                        String[] split = s.split("-");
                        String cql = "alter table " + s + " add " + split[1] + "";
                        CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
                    }

                }
            }
            String cql = CreateCql.getKey2("data_all");
            CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
        }

        return "";

    }


    /**
     * 获取降频的keys
     *
     * @return
     */
    public static String getReduceKeys1() {

//        这一次的keys
        List<String> list2 = CreateCql.getAllList("reduce");
        //取上一次的keys
        String reduce = RedisUtil.hget("cassandratable", "reduce");
        JSONArray parse1 = (JSONArray) JSON.parse(reduce);
        List<String> list3 = (List) parse1;
        String keys = "";
        //判断两次keys有无差异
        if (!list2.containsAll(list3) && list3.containsAll(list2)) {

            List<String> list = new ArrayList<>();
            for (int i = 0; i < list3.size(); i++) {
                //变量名-位长度-有无符号
                String s = list3.get(i);
                String[] split = s.split("-");
                list.add(split[0]);
            }
            keys = JSON.toJSONString(list);
            RedisUtil.hset("cassandratable", "keys", keys);
        } else keys = reduce;

        return keys;
    }

    /**
     * 获取降频的keys
     *
     * @return
     */
    public static String getReduceKeys(String tableName) {

        String cql_query = "select * from " + tableName + " limit 1";
        List<String> listProtocol = new ArrayList<String>();
        ResultSet resultSet = CassandraSingleConnect.INSTANCE.getInstance().execute(cql_query);
        logger.info("当前表的字段");
        ColumnDefinitions columnDefinitions = resultSet.getColumnDefinitions();
        for (ColumnDefinitions.Definition column : columnDefinitions) {
            listProtocol.add(column.getName().toUpperCase());
        }
        Map<String, Object> map = ProtocolUtil.getProtocolAll("");
        Map<String, MessageProtocol> messageProtocolMap = ProtocolUtil.getProtocolMap(map);
        logger.info("variable中的协议");
        for (Map.Entry<String, MessageProtocol> entry : messageProtocolMap.entrySet()) {
            MessageProtocol messageProtocol = entry.getValue();
            List<Variable> variables = messageProtocol.getVariable();
            for (Variable variable : variables) {
                if (variable.getType().equals(ProtocolConstants.TYPE_STATE)) {
                    boolean isContains = listProtocol.contains(variable.getUniqueCode().toUpperCase());
                    if (!isContains) {
                        listProtocol.add(variable.getUniqueCode().toUpperCase());
                    }
                }
            }
        }
        return JSON.toJSONString(listProtocol);
    }


    public static void main(String[] args) {
        String s = getReduceKeys("tablemachine_time");
        System.out.println(s);
    }

}

