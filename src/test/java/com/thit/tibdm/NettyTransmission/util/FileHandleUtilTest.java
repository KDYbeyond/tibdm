package com.thit.tibdm.NettyTransmission.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Created by dongzhiquan on 2017/6/27.
 */
public class FileHandleUtilTest {

    public final static Logger LOGGER = LoggerFactory.getLogger(FileHandleUtilTest.class);

    @Test
    public void testGetDay() throws Exception {

        LOGGER.info(FileHandleUtil.getDay());

    }
}