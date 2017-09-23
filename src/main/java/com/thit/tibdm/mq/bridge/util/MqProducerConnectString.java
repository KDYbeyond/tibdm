package com.thit.tibdm.mq.bridge.util;

import com.thit.tibdm.mq.bridge.KafkaConstant;
import com.thit.tibdm.util.ResourceUtil;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;
import java.util.Properties;

/**
 * Created by dongzhiquan on 2017/5/27.
 */
public enum MqProducerConnectString {
    /**
     * 实例
     */
    INSTANCE;
    /**
     * 生产者
     */
    private Producer instance;

    MqProducerConnectString() {
        Properties properties = new Properties();
        properties.put("serializer.class", StringEncoder.class.getName());
        properties.put("metadata.broker.list", ResourceUtil.getProValueByNameAndKey(KafkaConstant.FILE_NAME, KafkaConstant.KAFKA_IP));
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
