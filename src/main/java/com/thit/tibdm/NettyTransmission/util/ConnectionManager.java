package com.thit.tibdm.NettyTransmission.util;

import io.netty.channel.Channel;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

/**
 * 连接管理器
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-06-22 15:44
 **/
public class ConnectionManager {
    /**
     * 日志
     */
    public static final  Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
    /**
     * 连接集合
     */
    public static final ConcurrentMap<String, Channel> connections = new ConcurrentHashMapV8<>();

    /**
     * 添加连接
     *
     * @param connection 连接
     */
    public void add(Channel connection) {
        connections.putIfAbsent(connection.remoteAddress().toString(), connection);
    }

    /**
     *
     * @param connection 连接
     */
    public void remove(Channel connection) {
        connections.remove(connection.remoteAddress().toString());
    }
}
