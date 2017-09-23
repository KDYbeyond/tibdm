package com.thit.tibdm.mq.bridge.util;

import com.thit.tibdm.mq.bridge.KafkaConstant;
import com.thit.tibdm.util.ResourceUtil;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import kafka.serializer.DefaultEncoder;
import java.util.Properties;

/**
 * Created by wanghaoqiang on 2017/1/16.
 */
public enum MqProducerConnect {
    /**
     * 实例
     */
    INSTANCE;
    /**
     * 生产者
     */
    private Producer instance;

    MqProducerConnect() {
        Properties properties = new Properties();
//        properties.put("serializer.class", StringEncoder.class.getName());
        properties.put("serializer.class",DefaultEncoder.class.getName());
        properties.put("metadata.broker.list", ResourceUtil.getProValueByNameAndKey(KafkaConstant.FILE_NAME, KafkaConstant.KAFKA_IP));
        System.out.println(ResourceUtil.getProValueByNameAndKey(KafkaConstant.FILE_NAME, KafkaConstant.KAFKA_IP));
        instance = new Producer(new ProducerConfig(properties));
    }

    /**
     * 单例模式获取kafka-producer连接
     *
     * @return Producer
     */
    public Producer getInstance() {
        return instance;
    }
}
