package com.thit.tibdm.imdb.bridge.util;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.bom.entity.Variable;
import com.thit.tibdm.util.ResourceUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by dongzhiquan on 2017/6/16.
 */
public class RedisUtilTest {

    public final static Logger LOGGER = LoggerFactory.getLogger(RedisUtilTest.class);

    @Test
    public void testGetJedisConnect() throws Exception {
        Jedis jedisConnect = RedisUtil.getJedisConnect();
        System.out.println(jedisConnect.hget("beijing-fault", "336-GZ273"));

        String protocalAll = ResourceUtil.getProValueByNameAndKey("rdb", "pro");
        Map mapALl = (Map) JSON.parse(protocalAll);
        //根据车号获取协议名(前期没有车号 随机取一个)
        String protocolName = "protocol3&4";
        //根据协议名获取协议
        String myProtocol = mapALl.get(protocolName).toString();
        //转换为实体类
        MessageProtocol messageProtocol = JSON.parseObject(myProtocol, MessageProtocol.class);
        List<Variable> list = messageProtocol.getVariable();
        int i = 1;
        for (Variable variable : list) {
            if (variable.getType().equals("fault")) {
                i++;
            }

        }
        System.out.println(i);

    }


    @Test
    public void testGetHgetCluster() throws Exception {

        RedisUtil.getHgetAllCluster("shanghai");
    }


    @Test
    public void testComparaCollectTime() throws Exception {

        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();
        RedisUtil.comparaCollectTime(map1.toString(), map2.toString());

    }

    @Test
    public void testHget() throws Exception {

        LOGGER.info(RedisUtil.hget("proStatus", "pro17"));
    }

    @Test
    public void testGet() throws Exception {
        String name = RedisUtil.get("name");
        LOGGER.info(name);
    }
    @Test
    public void testSet() throws Exception {
        RedisUtil.set("name","xiaoqiang");
    }

    @Test
    public void testHmset() throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("aa", "cc");
        RedisUtil.hmset("hashset",map);
    }



    @Test
    public void testGetHgetAllCluster() throws Exception {
        LOGGER.info(RedisUtil.getHgetAllCluster("proStatus").toString());
    }

    @Test
    public void testHset() throws Exception {

        RedisUtil.hset("proStatus", "test", "0");

    }

    @Test
    public void eval() throws Exception {
        String script = "local value = redis.call('get', KEYS[1]) \n" +
                "if (value == '6')\n" +
                "then\n" +
                "\tredis.call('set',KEYS[1],0)\n" +
                "else\n" +
                "\tredis.call('INCR',KEYS[1])\n" +
                "end\n" +
                "return redis.call('get',KEYS[1])";
        List<String> keys = new ArrayList<>();
        keys.add("warn_count");
        List<String> args = new ArrayList<>();
        Object eval2 = RedisUtil.eval(script, keys, args);
        LOGGER.info(eval2.toString());
    }

    @Test
    public void zadd(){
        RedisUtil.zadd("sort_set",System.currentTimeMillis(),"a");
    }

}