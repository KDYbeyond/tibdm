package com.thit.tibdm.mq.bridge;

import com.thit.tibdm.mq.bridge.util.MqConsumerConnect;
import com.thit.tibdm.util.ResourceUtil;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wanghaoqiang on 2017/1/18.
 *
 * @author wanghaoqiang
 */
public class MqConsumer {
    /**
     * ExecutorService
     */
    private static ExecutorService threadPool;
    /**
     * String
     */
    private String topic;
    /**
     * MessageExecutor
     */
    private MessageExecutor executor;
    /**
     * numThreads
     */
    private int numThreads = 6;

    public MqConsumer(String topic, int numThreads, MessageExecutor executor) {
        this.topic = topic;
        this.numThreads = numThreads;
        this.executor = executor;
    }

    /**
     *
     */
    public void start() {
        ConsumerConnector instance = MqConsumerConnect.INSTANCE.getInstance();
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        String topic = ResourceUtil.getProValueByNameAndKey(KafkaConstant.FILE_NAME, KafkaConstant.TOPIC);
        String group = ResourceUtil.getProValueByNameAndKey(KafkaConstant.FILE_NAME, KafkaConstant.GROUP);
        // 一次从主题中获取一个streams流,topic由几个线程来处理
        topicCountMap.put(topic, numThreads);
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = instance.createMessageStreams(topicCountMap);
        threadPool = Executors.newFixedThreadPool(numThreads);
        List<KafkaStream<byte[], byte[]>> streams = messageStreams.get(topic);
        for (KafkaStream<byte[], byte[]> stream : streams) {
            threadPool.execute(new MessageRunner(stream));
        }
    }

    class MessageRunner implements Runnable {
        /**
         * KafkaStream
         */
        private KafkaStream<byte[], byte[]> stream;

        /**
         * @param stream
         */
        MessageRunner(KafkaStream<byte[], byte[]> stream) {
            this.stream = stream;
        }

        @Override
        public void run() {
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while (it.hasNext()) {
                MessageAndMetadata<byte[], byte[]> item = it.next();
//                System.out.println("partiton:" + item.partition());
//                System.out.println("offset:" + item.offset());
                executor.execute(new String(item.message()));//UTF-8
                System.out.println(Thread.currentThread().getName() + ": partition[" + item.partition() + "],"
                        + "offset[" + item.offset() + "], " + "接受到的消息是" + new String(item.message()));
            }
        }
    }

    interface MessageExecutor {
        /**
         *
         * @param message 消息
         */
        void execute(String message);
    }

    /**
     * @param args 参数
     */
    public static void main(String[] args) {
        MqConsumer consumer = null;
        MessageExecutor executor = new MessageExecutor() {
            @Override
            public void execute(String message) {
//                System.out.println("接收到的消息是"+message);
            }
        };
        consumer = new MqConsumer("topic1", 2, executor);
        consumer.start();
    }

}



