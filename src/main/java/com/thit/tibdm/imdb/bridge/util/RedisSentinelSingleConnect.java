package com.thit.tibdm.imdb.bridge.util;

import com.thit.tibdm.imdb.bridge.RedisConstant;
import com.thit.tibdm.util.ResourceUtil;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wanghaoqiang on 2017/1/17.
 */
public enum RedisSentinelSingleConnect {
    /**
     *实例
     */
    INSTANCE;
    /**
     *pool
     */
    private JedisPool pool;
//    private JedisSentinelPool pool;

    RedisSentinelSingleConnect() {
        Set<String> IPS = new HashSet<>();
//        int port = Integer.parseInt(ResourceUtil.getRedisProValue(RedisConstant.PORT));
        int port = Integer.parseInt(ResourceUtil.getProValueByNameAndKey("redis-db","port"));
        String redisHost = ResourceUtil.getProValueByNameAndKey("redis-db",RedisConstant.IP);
//        String redisHost = "192.168.8.109";
        String[] split = redisHost.split(",");
        for (String host : split) {
            IPS.add(new HostAndPort(host, port).toString());
        }

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1024);
        config.setMaxIdle(200);
        config.setMaxWaitMillis(20000);
        pool = new JedisPool(config,split[0],port);
//        GenericObjectPoolConfig gPoolConfig = new GenericObjectPoolConfig();
//        gPoolConfig.setMaxIdle(10);
//        gPoolConfig.setMaxTotal(10);
//        gPoolConfig.setMaxWaitMillis(10);
//        gPoolConfig.setJmxEnabled(true);
//        pool = new JedisSentinelPool("mymaster", IPS, gPoolConfig);
    }

//    public JedisSentinelPool getInstance() {
//        return pool;
//    }
    public JedisPool getInstance() {
        return pool;
    }
}
