package com.thit.tibdm.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 测试配置文件
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-06-03 14:36
 **/
public class ResourceUtilTest {

    public final static Logger logger = LoggerFactory.getLogger(ResourceUtilTest.class);

    @Test
    public void getRedisProValue() throws Exception {
        String ip = ResourceUtil.getRedisProValue("ip");
        logger.info(ip);
    }

    @Test
    public void getCassandraProValue() throws Exception {
        String ip = ResourceUtil.getCassandraProValue("type");
        logger.info(ip);
    }

    @Test
    public void getProValueByNameAndKey() throws Exception {
        String ip = ResourceUtil.getProValueByNameAndKey("zeppelin","urlPath");
        logger.info(ip);
    }

}