package com.thit.tibdm.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * @author: dongzhiquan  Date: 2017/6/28 Time: 20:19
 */
public class DateUtilTest {

    private static final Logger logger= LoggerFactory.getLogger(DateUtilTest.class);
    @Test
    public void testGetTime() throws Exception {
        logger.info(String.valueOf(DateUtil.getTime("2017/05/06 10:12")));
    }

    @Test
    public void testGetFormat() throws Exception {
        long l=System.currentTimeMillis();
        logger.info(DateUtil.getFormat(l));
    }

    @Test
    public void testGetTimeByFormat() throws Exception {
        logger.info(String.valueOf(DateUtil.getTimeByFormat("2017/05/06 10:12","yyyy/MM/dd HH:mm")));
    }

    @Test
    public void testGetAddZero() throws Exception {
        logger.info(DateUtil.getAddZero(2));
    }
}