package com.thit.tibdm.imdb.bridge.util;


import com.alibaba.fastjson.JSON;
import com.thit.tibdm.util.Constants;
import com.thit.tibdm.util.ResourceUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;

/**
 * Created by rmbp13 on 16/9/18.
 */
public class RedisUtil {

    /**
     * 获取Redis连接
     *
     * @return Jedis
     */
    public static Jedis getJedisConnect() {
        Jedis jedis = RedisSentinelSingleConnect.INSTANCE.getInstance().getResource();
        return jedis;
    }


    /**
     * 保存hash
     *
     * @param key   关键字
     * @param field 域
     * @param value 值
     * @return String
     */
    public static String saveHashCluster(String key, String field, String value) {
        long hset;
        long startTime = System.currentTimeMillis();//获取当前时间
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            hset = RedisClusterSingleConnect.INSTANCE.getInstance().hset(key, field, value);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                hset = jedisConnect.hset(key, field, value).intValue();
            } finally {
                jedisConnect.close();
            }

        }
        long endTime = System.currentTimeMillis();
        System.out.println("redis插入程序运行时间：" + (endTime - startTime) + "ms");
        if (hset == 0 || hset == 1) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * 获取key -> field 获取到属性的值
     *
     * @param key   关键字
     * @param field 域
     * @return String
     */
    public static String getHgetCluster(String key, String field) {

        String hget;
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            hget = RedisClusterSingleConnect.INSTANCE.getInstance().hget(key, field);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                hget = jedisConnect.hget(key, field);
            } finally {
                jedisConnect.close();
            }
        }
        return hget;
    }


    /**
     * 一次获取多个
     *
     * @param key       关键字
     * @param fieldList 阈值
     * @return List
     */
    public static List getHmgetCluster(String key, String[] fieldList) {
        List<String> resultList;
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            resultList = RedisClusterSingleConnect.INSTANCE.getInstance().hmget(key, fieldList);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                resultList = jedisConnect.hmget(key, fieldList);
            } finally {
                jedisConnect.close();
            }
        }
        return resultList;
    }

    /**
     * 一次获取多个
     *
     * @param key 关键字
     * @return map
     */
    public static Map<String, String> getHgetAllCluster(String key) {
        Map<String, String> map;
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            map = RedisClusterSingleConnect.INSTANCE.getInstance().hgetAll(key);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                map = jedisConnect.hgetAll(key);
            } finally {
                jedisConnect.close();
            }
        }
        return map;
    }

    /**
     * 比较采集时间
     *
     * @param oldJson 旧Json
     * @param newJson 新Json
     * @return boolean
     */
    public static boolean comparaCollectTime(String oldJson, String newJson) {
        Map oldMap = (Map) JSON.parse(oldJson);
        Map newMap = (Map) JSON.parse(newJson);
        if (oldMap == null || oldMap.isEmpty() || oldMap.get("COLLECT_TIME") == null) {
            return true;
        } else if (Long.parseLong(oldMap.get("COLLECT_TIME").toString()) < Long.parseLong(newMap.get("COLLECT_TIME").toString())) return true;
        else {
            return false;
        }
    }


    /**
     * 获取kv
     *
     * @param key 关键字
     * @return String
     */
    public static String get(String key) {
        String v = "";
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            v = RedisClusterSingleConnect.INSTANCE.getInstance().get(key);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                v = jedisConnect.get(key);
            } finally {
                jedisConnect.close();
            }
        }
        return v;
    }

    /**
     * 获取kv
     *
     * @param key   关键字
     * @param value 值
     * @return
     */
    public static void set(String key, String value) {
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            RedisClusterSingleConnect.INSTANCE.getInstance().set(key, value);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                jedisConnect.set(key, value);
            } finally {
                jedisConnect.close();
            }
        }
    }

    /**
     * 获取kv
     *
     * @param key   关键字
     * @param value 值
     * @return
     */
    public static void lpush(String key, String value) {
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            RedisClusterSingleConnect.INSTANCE.getInstance().lpush(key, value);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                jedisConnect.lpush(key, value);
            } finally {
                jedisConnect.close();
            }
        }
    }

    /**
     * 添加有序数组
     *
     * @param key   关键字
     * @param value 值
     * @param score 分数
     * @return
     */
    public static void zadd(String key, double score, String value) {
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            RedisClusterSingleConnect.INSTANCE.getInstance().zadd(key, score, value);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                jedisConnect.zadd(key, score, value);
            } finally {
                jedisConnect.close();
            }
        }
    }


    /**
     * 移除有序集合中的信息
     *
     * @param key   关键字
     * @param start 开始
     * @param end   结束
     * @return
     */
    public static void zremrangeByScore(String key, double start, double end) {
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            RedisClusterSingleConnect.INSTANCE.getInstance().zremrangeByScore(key, start, end);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                jedisConnect.zremrangeByScore(key, start, end);
            } finally {
                jedisConnect.close();
            }
        }
    }

    /**
     * 序集合中的大小
     *
     * @param key 关键字
     * @param min 最小
     * @param max 最大
     * @return res 数量
     */
    public static long zcount(String key, double min, double max) {
        Long zcount;
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            zcount = RedisClusterSingleConnect.INSTANCE.getInstance().zcount(key, min, max);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                zcount = jedisConnect.zcount(key, min, max);
            } finally {
                jedisConnect.close();
            }
        }
        return zcount;
    }

    /**
     * hset更新
     *
     * @param key   关键字
     * @param field 阈值
     * @param value 值
     */
    public static void hset(String key, String field, String value) {
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            RedisClusterSingleConnect.INSTANCE.getInstance().hset(key, field, value);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                jedisConnect.hset(key, field, value);
            } finally {
                jedisConnect.close();
            }
        }
    }

    /**
     * hmset更新
     *
     * @param key 关键字
     * @param map 阈值
     */
    public static void hmset(String key, Map<String, String> map) {
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            RedisClusterSingleConnect.INSTANCE.getInstance().hmset(key, map);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                jedisConnect.hmset(key, map);
            } finally {
                jedisConnect.close();
            }
        }
    }

    /**
     * 订阅
     *
     * @param pubSub  订阅类
     * @param channel 通道
     */
    public static void sub(JedisPubSub pubSub, String... channel) {
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            RedisClusterSingleConnect.INSTANCE.getInstance().subscribe(pubSub, channel);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                jedisConnect.subscribe(pubSub, channel);
            } finally {
                jedisConnect.close();
            }
        }
    }

    /**
     * 发布内容
     *
     * @param info    发布内容
     * @param channel 通道
     */
    public static void pub(String channel, String info) {
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            RedisClusterSingleConnect.INSTANCE.getInstance().publish(channel, info);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                jedisConnect.publish(channel, info);
            } finally {
                jedisConnect.close();
            }
        }
    }

    /**
     * hset更新
     *
     * @param key   关键字
     * @param field 阈值
     * @return String
     */
    public static String hget(String key, String field) {
        String result = "";
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            result = RedisClusterSingleConnect.INSTANCE.getInstance().hget(key, field);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                result = jedisConnect.hget(key, field);
            } finally {
                jedisConnect.close();
            }
        }
        return result;
    }


    /**
     * @param n
     * @return long
     */
    /**
     * 获取所有的set集合
     *
     * @param args   参数
     * @param keys   关键字
     * @param script 字符串
     * @return 对象
     */
    public static Object eval(String script, List<String> keys, List<String> args) {
        Object eval;
        if (ResourceUtil.getRedisProValue("type").equals(Constants.REDIS_TYPE_CLUSTER)) {
            eval = RedisClusterSingleConnect.INSTANCE.getInstance().eval(script, keys, args);
        } else {
            Jedis jedisConnect = getJedisConnect();
            try {
                eval = jedisConnect.eval(script, keys, args);
            } finally {
                jedisConnect.close();
            }
        }
        return eval;
    }


    /**
     * @param table 字符串
     * @param ch    字符串
     * @return String
     */
    public static String getOnlineStatus(String table, String ch) {
        Jedis jedis = RedisUtil.getJedisConnect();
        String hget = jedis.hget(table, ch);
        jedis.close();
        return hget;

    }

    /**
     * @param table  字符串
     * @param ch     字符串
     * @param status 状态
     */
    public static void setOnlineStatus(String table, String ch, String status) {
        Jedis jedis = RedisUtil.getJedisConnect();
        jedis.hset(table, ch, status);
        jedis.close();
    }
}
