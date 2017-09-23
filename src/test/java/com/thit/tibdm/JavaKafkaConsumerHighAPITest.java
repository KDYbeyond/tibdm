package com.thit.tibdm;

import com.thit.tibdm.mq.bridge.JavaKafkaConsumerHighAPI;
import com.thit.tibdm.mq.bridge.util.MqConsumerConnect;
import kafka.javaapi.consumer.ConsumerConnector;
import org.junit.Test;

/**
 * Created by ibf on 12/21.
 */
public class JavaKafkaConsumerHighAPITest {

    @Test
    public void kafka() {
//        String zookeeper = "192.168.8.108:2181";
//        String groupId = "wang6";
//        String topic = "bomparse3";
//        int threads = 40;
//
//        JavaKafkaConsumerHighAPI example = new JavaKafkaConsumerHighAPI(topic, threads, zookeeper, groupId);
//        new Thread(example).start();
//
//        // 执行10秒后结束
//        int sleepMillis = 1000000000;
//        try {
//            Thread.sleep(sleepMillis);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        ConsumerConnector instance = MqConsumerConnect.INSTANCE.getInstance();
        ConsumerConnector instance1 = MqConsumerConnect.INSTANCE.getInstance();
        if (instance==instance1){
            System.out.println("相等");
        }
        // 关闭
//        example.shutdown();
    }
}