package com.thit.tibdm;

import com.google.common.base.Charsets;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import kafka.serializer.DefaultDecoder;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 测试kafka接收字节
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-05-27 10:31
 **/
public class KafkaTest {
    public final static Logger logger = LoggerFactory.getLogger(KafkaTest.class);

    public static void main(String[] args) throws InterruptedException {
        final Timer timer = new HashedWheelTimer();
        timer.newTimeout(timeout -> System.out.println("timeout 5"), 5, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout 10"), 10, TimeUnit.SECONDS);
    }

    @org.junit.Test
    public void sendKafka() throws InterruptedException {
        final Timer timer = new HashedWheelTimer();
        timer.newTimeout(timeout -> System.out.println("timeout 5"), 5, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout 10"), 10, TimeUnit.SECONDS);
    }
}
