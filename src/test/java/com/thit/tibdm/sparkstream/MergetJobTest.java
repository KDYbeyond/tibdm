package com.thit.tibdm.sparkstream;

import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.imdb.bridge.util.RedisUtil;
import com.thit.tibdm.sparkstream.util.ProtocolUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author: dongzhiquan  Date: 2017/7/18 Time: 14:20
 */
public class MergetJobTest {
    @Test
    public void sumTimes() throws Exception {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(0);


        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
//        int b = MergetJob.sumTimes(list, 5, true);
//        LOGGER.info("计算结果为：" + b);
    }

    public final static Logger LOGGER = LoggerFactory.getLogger(MergetJobTest.class);

    @Test
    public void testMain() throws Exception {


//        MergetJob.main(null);

    }


}