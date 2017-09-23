package com.thit.tibdm.mq.bridge;

import com.xxl.conf.core.XxlConfClient;
import kafka.producer.Partitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据key来确定kafka分区
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-03-31 09:35
 **/
public class RoutePartition implements Partitioner {
    /**
     * 日志
     */
    public static Logger logger = LoggerFactory.getLogger(RoutePartition.class);

    @Override
    public int partition(Object key, int numPartitions) {
        //根据key来从配置中心获取他的分区
        String partition = XxlConfClient.get(key.toString(), null);
        return Integer.parseInt(partition);
    }
}
