package com.thit.tibdm.sparkstream.util;

import com.thit.tibdm.mq.bridge.MqProducer;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by dongzhiquan on 2017/6/18.
 */
public class ParseUtilTest {

    public final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ParseUtilTest.class);
    @Test
    public void testParseMsg() throws Exception {


        ExecutorService executor= Executors.newFixedThreadPool(100);
        int num=1;
        for (int j = 0; j <1; j++) {

            for (int i = 0; i < num; i++) {
                //Send one message.
                String msg="";
                executor.execute(()-> MqProducer.sendMsg(msg,"devsave"));
            }

            Thread.sleep(500);
            System.out.println("执行了"+(j+1)+"次");
        }

    }

    @Test
    public void testDoConvert() throws Exception {
        Map map=new HashMap<>();
        map.put("x",2);

        LOGGER.info(ParseUtil.doConvert("x * 2",map));

    }
}