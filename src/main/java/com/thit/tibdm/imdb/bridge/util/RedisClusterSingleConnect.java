package com.thit.tibdm.imdb.bridge.util;

import com.thit.tibdm.imdb.bridge.RedisConstant;
import com.thit.tibdm.util.ResourceUtil;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wanghaoqiang on 2017/1/16.
 */
public enum RedisClusterSingleConnect {
    /**
     *实例
     */
    INSTANCE;
    /**
     *
     */
    private JedisCluster jedisCluster;

    RedisClusterSingleConnect() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        String redisHost = ResourceUtil.getRedisProValue(RedisConstant.IP);
        String[] split = redisHost.split(",");
        for (String host : split) {
            jedisClusterNodes.add(new HostAndPort(host, Integer.parseInt(ResourceUtil.getRedisProValue(RedisConstant.PORT))));
        }
        jedisCluster = new JedisCluster(jedisClusterNodes);
    }

    public JedisCluster getInstance() {
        return jedisCluster;
    }
}
