package com.thit.tibdm.NettyTransmission.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 匡东洋 E-mail:473948143@qq.com
 * @version 创建时间：2017年7月4日 上午10:56:33 类说明
 */
public class Global {

    /**
     * 线程安全Map,用于存放连接设备;key:列车ID+设备ID,Value:连接设备
     */
    public static final ConcurrentMap<String, ConnectionDevice> connections = new ConcurrentHashMap<>();

    /**
     * 连接的数目
     */
    public static AtomicInteger connectionNums = new AtomicInteger();
}
