package com.thit.tibdm.NettyTransmission.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 负载均衡算法
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-06-19 11:40
 **/
public class HashTopic {
    /**
     * 日志
     */
    public  static final Logger LOGGER = LoggerFactory.getLogger(HashTopic.class);

    /**
     * 负载均衡的获取Topic
     * @param ch 字符
     * @param topicKey 关键字
     * @return 返回
     */
    public static String getTopic(String ch, String topicKey) {
        String topicparse = ResourceUtil.getProValueByNameAndKey("kafka-mq", topicKey);
        String[] split = topicparse.split(",");
        int i = (ch.hashCode()) % (split.length);
        return split[i];
    }
}
