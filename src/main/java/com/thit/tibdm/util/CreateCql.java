package com.thit.tibdm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.thit.tibdm.NettyTransmission.Message;
import com.thit.tibdm.base.InterfaceBase;
import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.bom.entity.Variable;
import com.thit.tibdm.db.bridge.util.CassandraV2Table;
import com.thit.tibdm.imdb.bridge.util.RedisUtil;
import com.thit.tibdm.sparkstream.util.ProtocolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;
import scala.Tuple3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成sql
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-06-03 19:55
 **/
public class CreateCql {
    /**
     * 日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CreateCql.class);

    /**
     * @return 返回
     */
    public static String getCQL() {
        Config.init();
        String proValueByNameAndKey = ResourceUtil.getProValueByNameAndKey("rdb", "pro");
        Map map = (Map) JSON.parse(proValueByNameAndKey);
        List list = (List) ((Map) map.get("protocol3&4")).get("Variable");
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE shanghai (");

        list.forEach(l -> {
            String type = ((Map<String, String>) l).get("Type");
//            if (!type.equals("fault")){
            String uniqueCode = ((Map<String, String>) l).get("UniqueCode");
            sb.append(" " + uniqueCode + " text,");
//            }

        });
        sb.append(" MACHINE_ID text,COLLECT_TIME bigint,COLLECT_TYPE text,CH text,RECEIVE_TIME text, SAVE_TIME text,");
        sb.append("PRIMARY KEY (MACHINE_ID,COLLECT_TYPE, COLLECT_TIME) ) ");
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * @return 返回
     */
    public static String getKey() {
        Config.init();
        String proValueByNameAndKey = ResourceUtil.getProValueByNameAndKey("rdb", "pro");
        Map map = (Map) JSON.parse(proValueByNameAndKey);
        List list = (List) ((Map) map.get("protocol3&4")).get("Variable");
        List<String> list1 = new ArrayList<>();

        list.forEach(l -> {
            String type = ((Map<String, String>) l).get("Type");
            if (!type.equals("fault")) {
                String uniqueCode = ((Map<String, String>) l).get("UniqueCode");
                list1.add(uniqueCode);
            }

        });
        String s = JSON.toJSONString(list1);
        System.out.println(s);
        return s;
    }


    public static void getKeyKey(String table_name) {
        String tableline_notime = ProtocolUtil.getReduceKeys(table_name);
        JSONArray jsonArray = JSON.parseArray(tableline_notime);

        StringBuffer sb = new StringBuffer();

        sb.append("CREATE TABLE bomkp." + table_name + " (");
        jsonArray.forEach(j -> {
            if (j.equals(CassandraV2Table.collect_time)) {

                sb.append(" " + j + " bigint " + ",");
            } else {
                sb.append(" " + j + " text " + ",");
            }
        });

        sb.append(" COLLECT_DATE text,");
        sb.append("PRIMARY KEY ((MACHINE_ID,COLLECT_DATE),COLLECT_TYPE, COLLECT_TIME) ) ");
        String ss = sb.toString();
        System.out.println("s==" + ss);

    }


    public static String getKey1(String table_name) {
        Map<String, Object> protocolAll = ProtocolUtil.getProtocolAll("");
        Map<String, MessageProtocol> protocolMap = ProtocolUtil.getProtocolMap(protocolAll);
        int maxSize = 0;
        int proSize = 0;
        String proName = "";
        MessageProtocol messageProtocol = new MessageProtocol();
        Map<Integer, String> map = new HashMap<>();

        //统计每个协议中的state类型的变量的个数
        for (Map.Entry<String, MessageProtocol> entry : protocolMap.entrySet()) {
            String key = entry.getKey();
            messageProtocol = entry.getValue();
            List<Variable> list = messageProtocol.getVariable();
            int i = 0;
            for (int j = 0; j < list.size(); j++) {
                Variable l = list.get(j);
                String type = l.getType();
                if (!type.equals("TROUBLE")) {
                    i++;
                }
            }
            map.put(i, key);

        }
        //找出上面最多的协议名
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            proSize = key;
            if (proSize > maxSize) {
                maxSize = proSize;
                proName = value;
            }
        }

        //遍历协议生成cql语句
        MessageProtocol messageProtocol1 = protocolMap.get(proName);
        List<Variable> list = messageProtocol1.getVariable();

        StringBuffer sb = new StringBuffer();

        sb.append("CREATE TABLE " + table_name + " (");
        list.forEach(l -> {
            if (l.getType().equals(ProtocolConstants.TYPE_STATE)) {
                sb.append(" " + l.getUniqueCode() + " " + getType(l.getBitLength(), l.getIsSigned()) + " , ");
            }
        });

        sb.append(" MACHINE_ID text,COLLECT_TIME bigint,COLLECT_TYPE text,CH text,RECEIVE_TIME text, SAVE_TIME text,");
        sb.append("PRIMARY KEY (MACHINE_ID,COLLECT_TYPE, COLLECT_TIME) ) ");
        String ss = sb.toString();
        System.out.println("s==" + ss);
        return ss;
    }


    public static List<String> getAllList(String flag) {

        //获取所有协议
        Map<String, Object> protocolAll = ProtocolUtil.getProtocolAll("");
        Map<String, MessageProtocol> protocolMap = ProtocolUtil.getProtocolMap(protocolAll);
        MessageProtocol messageProtocol = new MessageProtocol();
        List<String> list1 = new ArrayList<>();

        //把每个协议中的state类型的变量放入map中
        for (Map.Entry<String, MessageProtocol> entry : protocolMap.entrySet()) {
            String key = entry.getKey();
            messageProtocol = entry.getValue();
            List<Variable> list = messageProtocol.getVariable();
            for (int j = 0; j < list.size(); j++) {
                Variable v = list.get(j);
                String type = v.getType();
                if (flag.equals("all")) {
                    list1.add(key + "_" + v.getUniqueCode());
                } else if (flag.equals("reduce")) {

                    if (!type.equals("TROUBLE")) {
                        list1.add(key + "_" + v.getUniqueCode());
                    }
                }
            }
        }
        return list1;
    }

    public static String getKey2(String table_name) {
        //获取所有协议
        Map<String, Object> protocolAll = ProtocolUtil.getProtocolAll("");
        Map<String, MessageProtocol> protocolMap = ProtocolUtil.getProtocolMap(protocolAll);
        MessageProtocol messageProtocol = new MessageProtocol();
        Map<String, String> mapVariable = new HashMap<>();
        List<String> list1 = new ArrayList<>();

        //把每个协议中的state类型的变量放入map中
        for (Map.Entry<String, MessageProtocol> entry : protocolMap.entrySet()) {
            String key = entry.getKey();
            messageProtocol = entry.getValue();
            List<Variable> list = messageProtocol.getVariable();
            for (int j = 0; j < list.size(); j++) {
                Variable v = list.get(j);
                String type = v.getType();
                if (!type.equals("TROUBLE")) {
                    mapVariable.put(key + "_" + v.getUniqueCode(), v.getBitLength() + "-" + v.getIsSigned());
                }
            }
        }

        //降频的key保存下来
        RedisUtil.hset("cassandratable", "keys", JSON.toJSONString(list1));

        List<String> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE " + table_name + " (");
        for (Map.Entry<String, String> entry : mapVariable.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String str = key + "-" + value;
            list.add(str);
            String[] split = value.split("-");
            sb.append(" " + key + " " + getType(split[0], split[1]) + " , ");
        }

        String json = JSON.toJSONString(list);
        RedisUtil.hset("cassandraTable", "table", json);
        sb.append(" MACHINE_ID text,COLLECT_TIME bigint,COLLECT_TYPE text,CH text,RECEIVE_TIME text, SAVE_TIME text,");
        sb.append("PRIMARY KEY (MACHINE_ID,COLLECT_TYPE, COLLECT_TIME) ) ");

        String ss = sb.toString();
//        System.out.println("s=="+ss);
        return ss;
    }

    public static List<String> getKey3() {

        //获取所有协议
        Map<String, Object> protocolAll = ProtocolUtil.getProtocolAll("");
        Map<String, MessageProtocol> protocolMap = ProtocolUtil.getProtocolMap(protocolAll);
        MessageProtocol messageProtocol = new MessageProtocol();
        Map<String, String> mapVariable = new HashMap<>();
        List<String> list1 = new ArrayList<>();

        //把每个协议中的state类型的变量放入map中
        for (Map.Entry<String, MessageProtocol> entry : protocolMap.entrySet()) {
            String key = entry.getKey();
            messageProtocol = entry.getValue();
            List<Variable> list = messageProtocol.getVariable();
            for (int j = 0; j < list.size(); j++) {
                Variable v = list.get(j);
                String type = v.getType();
                if (!type.equals("TROUBLE")) {
                    list1.add(key + "_" + v.getUniqueCode() + "-" + v.getBitLength() + "-" + v.getIsSigned());
                }
            }
        }


        return list1;

    }

    /**
     * 根据字节长度和有无符号判断Cassandra字段类型
     *
     * @param bitLength
     * @param IsSinged
     * @return
     */
    public static String getType(String bitLength, String IsSinged) {
        String type = "";
        long l = Long.parseLong(bitLength);

        if (l > 0 && l <= 8 && IsSinged.equals("Y")) {
            type = "tinyint";
        } else if (l > 0 && l <= 8 && IsSinged.equals("N")) {
            type = "smallint";
        }
        if (l > 8 && l <= 16 && IsSinged.equals("Y")) {
            type = "smallint";
        } else if (l > 8 && l <= 16 && IsSinged.equals("N")) {
            type = "int";
        }
        if (l > 16 && l <= 24 && IsSinged.equals("Y")) {
            type = "int";
        } else if (l > 16 && l <= 24 && IsSinged.equals("N")) {
            type = "int";
        }

        if (l > 24 && l <= 32 && IsSinged.equals("Y")) {
            type = "int";
        } else if (l > 24 && l <= 32 && IsSinged.equals("N")) {
            type = "bigint";
        }
        if (l > 32 && l <= 64 && IsSinged.equals("Y")) {
            type = "bigint";
        } else if (l > 32 && l <= 64 && IsSinged.equals("N")) {
            type = "text";
        }
        if (l > 64) {
            type = "text";
        }


        return type;
    }

}
