package com.thit.tibdm.NettyTransmission.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dongzhiquan on 2017/6/28.
 */
public class ClientUtilTest {

    private static Logger logger= LoggerFactory.getLogger(ClientUtilTest.class);

    @Test
    public void testStringToHexString() throws Exception {
            logger.info(ClientUtil.stringToHexString("00"));
    }

    @Test
    public void testGetProtocolName() throws Exception {
        logger.info(ClientUtil.getProtocolName("330"));
    }

    @Test
    public void testGetBytes() throws Exception {
//        logger.info(ClientUtil.getBytes().toString());
    }

    @Test
    public void testGet16Str() throws Exception {
        logger.info(ClientUtil.get16Str(0,"2"));
    }

    @Test
    public void testGetRandomNum() throws Exception {
        logger.info(String.valueOf(ClientUtil.getRandomNum(10,20)));
    }

    @Test
    public void testRandom_long() throws Exception {
        ClientUtil.Random_long(10,20);

    }
}