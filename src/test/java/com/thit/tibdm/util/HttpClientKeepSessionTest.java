package com.thit.tibdm.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author: dongzhiquan  Date: 2017/7/17 Time: 10:33
 */
public class HttpClientKeepSessionTest {

    public final static Logger  logger= LoggerFactory.getLogger(HttpClientKeepSessionTest.class);

    @Test
    public void testGetProtocal() throws Exception {

        String str=HttpClientKeepSession.getProtocal("protocol3&4");
        Map map=new HashMap<>();
        map= JSON.parseObject(str);
        Map map1=(Map)map.get("data");
        Map map2=(Map) map1.get("variable");
        String str1=map2.toString();
        logger.error(str1);

    }

}