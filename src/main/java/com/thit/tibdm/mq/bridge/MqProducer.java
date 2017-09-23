package com.thit.tibdm.mq.bridge;

import com.thit.tibdm.mq.bridge.util.MqProducerConnect;
import com.thit.tibdm.mq.bridge.util.MqProducerConnectString;
import com.thit.tibdm.util.ResourceUtil;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author wanghaoqiang
 */
public class MqProducer implements Runnable {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(MqProducer.class);
    /**
     * 随机数
     */
    private static final int randomNum = Integer.parseInt(ResourceUtil.getProValueByNameAndKey("kafka-mq", "partition_num"));
    /**
     * 优先数
     */
    private int partition_num = 0;
    /**
     * 信息数
     */
    private int msg_num;
    /**
     * 信息
     */
    private byte[] message;
    /**
     * 字符串
     */
    private String s;

    public MqProducer(int partition_num, int msg_num) {
        this.partition_num = partition_num;
        this.msg_num = msg_num;
    }

    public MqProducer(int partition_num, byte[] message) {
        this.partition_num = partition_num;
        this.message = message;
    }

    public MqProducer(int partition_num, String s) {
        this.partition_num = partition_num;
        this.s = s;
    }

    /**
     * 发送kafka信息
     *
     * @param message 信息
     * @param topic   话题
     */
    public static void sendMsg(String message, String topic) {
        Producer instance = MqProducerConnectString.INSTANCE.getInstance();
//        logger.info(message);
        Random random = new Random();
        int partKey = random.nextInt(randomNum);
//        instance.send(new KeyedMessage<>(topic, null, null, message));
        instance.send(new KeyedMessage<>(topic, message));
    }

    /**
     * @param bytes 字节数组
     * @param topic 话题
     */
    public static void sendMsg(byte[] bytes, String topic) {
        Producer instance = MqProducerConnect.INSTANCE.getInstance();
        Random random = new Random();
        int partKey = random.nextInt(randomNum);
        instance.send(new KeyedMessage<>(topic, null, partKey, bytes));
    }


    @Override
    public void run() {
        Producer instance = MqProducerConnect.INSTANCE.getInstance();
        //分区数一般为kafka节点数cpu的总核数

        for (int i = 1; i <= partition_num; i++) { //往6个分区发数据
//            List<KeyedMessage<String, String>> messageList = new ArrayList<KeyedMessage<String, String>>();
//            for(int j = 0; j < msg_num; j++){ //每个分区6条讯息
//                messageList.add(new KeyedMessage<String, String>
//                        //String topic, String partition, String message
//                        ("test1", "partition[" + i + "]", "[The " + i + " pp]"));
//            }

            //message可以带key, 根据key来将消息分配到指定区, 如果没有key则随机分配到某个区
//          KeyedMessage<Integer, String> keyedMessage = new KeyedMessage<Integer, String>("test", 1, message);
            instance.send(new KeyedMessage<Integer, String>(
                    ResourceUtil.getProValueByNameAndKey(KafkaConstant.FILE_NAME, KafkaConstant.TOPIC), s));
        }

    }

    /**
     * 批量发送数据
     *
     * @param partition_num 往几个分区发送
     * @param message       数据
     */

    public static void sendMsg(int partition_num, byte[] message) {

        Thread t = new Thread(new MqProducer(partition_num, message));
        t.start();
    }

    /**
     * @param partition_num 优先级
     * @param message       信息
     */
    public static void sendMsg(int partition_num, String message) {

        Thread t = new Thread(new MqProducer(partition_num, message));
        t.start();
    }

}