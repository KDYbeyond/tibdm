package com.thit.tibdm.util;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.api.TXIBDMApi;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by wanghaoqiang on 2017/7/21.
 */
public class JexlUtilTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(JexlUtilTest.class);

    @Test
    public void convertToDouble() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("a", "6.2");
        map.put("b", "3");
        Object o = TXIBDMApi.XiJexl("a - b", JSON.toJSONString(map), "0", null);
        LOGGER.info(JSON.toJSONString(o));
    }

}