package com.thit.tibdm.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * 测试自定义日志部分
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-05-21 09:26
 **/
public class LogBackConfigLoaderTest {
    @Test
    public void load() throws Exception {
//        LogBackConfigLoader.load("/Users/rmbp13/Desktop/dev/103-SVN/tibdm/target/test-classes/logback.xml");
//        LogBackConfigLoader.load("./logback.xml");
        LogBackConfigLoader.logInit();
        Logger logger = LoggerFactory.getLogger(LogBackConfigLoader.class);
        logger.debug("现在的时间是 {}", new Date().toString());
        logger.info(" This time is {}", new Date().toString());
        logger.warn(" This time is {}", new Date().toString());
        logger.error(" This time is {}", new Date().toString());
    }

    @Test
    public void name() throws Exception {
        LogBackConfigLoader.logInit();
        Logger logger = LoggerFactory.getLogger(LogBackConfigLoader.class);
        logger.debug("现在的时间是 {}", new Date().toString());
    }
}