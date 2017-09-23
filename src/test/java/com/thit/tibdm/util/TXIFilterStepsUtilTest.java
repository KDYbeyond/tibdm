package com.thit.tibdm.util;

import com.alibaba.fastjson.JSONArray;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by dongzhiquan on 2017/6/27.
 */
public class TXIFilterStepsUtilTest {

    public final static Logger LOGGER = LoggerFactory.getLogger(TXIFilterStepsUtilTest.class);

    @Test
    public void testGetFilterStepsList() throws Exception {

    }

    @Test
    public void testFilterMap() throws Exception {

        Map map=new HashMap<>();
        map.put("zt1",1);
        map.put("zt2",1);
        String keys="[\"zt1\"]";
        LOGGER.info(TXIFilterStepsUtil.filterMap(map, JSONArray.parseArray(keys)).toString());

    }
}