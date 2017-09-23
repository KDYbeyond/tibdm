package com.thit.tibdm.mq.bridge.util;

import com.thit.tibdm.mq.bridge.KafkaConstant;
import com.thit.tibdm.util.ResourceUtil;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import java.util.Properties;

/**
 * Created by wanghaoqiang on 2017/1/18.
 */
public enum MqConsumerConnect {
    /**
     * 实例
     */
    INSTANCE;
    /**
     * 连接
     */
    private ConsumerConnector instance;

    MqConsumerConnect() {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", ResourceUtil.getProValueByNameAndKey(KafkaConstant.FILE_NAME, KafkaConstant.ZK_IP));//声明zk
        properties.put("group.id", ResourceUtil.getProValueByNameAndKey(KafkaConstant.FILE_NAME, KafkaConstant.GROUP));// 必须要使用别的组名称， 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据
        instance = Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
    }

    /**
     * 单例模式获取kafka-consumer连接
     *
     * @return ConsumerConnector
     */
    public ConsumerConnector getInstance() {
        return instance;
    }
}
