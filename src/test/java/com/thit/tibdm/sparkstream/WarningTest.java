package com.thit.tibdm.sparkstream;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.mq.bridge.MqProducer;
import com.thit.tibdm.util.Config;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 模拟数据来测试报警
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-06-13 10:26
 **/
public class WarningTest {
    public final static Logger LOGGER = LoggerFactory.getLogger(WarningTest.class);

    @Before
    public void init() {
        Config.init();
    }

    @Test
    public void sendKafka() {
        int max = 400;
        int min = 300;
        Random random = new Random();

        Map<String, Object> map = new HashMap<>();
        Map<String, String> kv = new HashMap<>();
        //准备数据结构
        map.put("CH", "500");

        String[] v = {"ZT274", "ZT275", "ZT276", "ZT277", "ZT278", "ZT279"};
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < v.length; j++) {
                int s = random.nextInt(max) % (max - min + 1) + min;
                kv.put(v[j],s+"");
            }
            map.put("JSON",kv);
            MqProducer.sendMsg(JSON.toJSONString(map), "bomsave");
        }
    }
}
