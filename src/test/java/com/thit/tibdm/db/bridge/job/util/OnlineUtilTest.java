package com.thit.tibdm.db.bridge.job.util;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.util.HttpClientKeepSession;
import com.xicrm.common.TXISystem;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by dongzhiquan on 2017/6/28.
 */
public class OnlineUtilTest {

    @Test
    public void testDoOnline() throws Exception {

        TXISystem.start();
        OnlineUtil.doOnline();
    }

    @Test
    public void testPush() throws IOException {
        String trainNumberStr="\'301\',\'302\'";
        Map map2=new HashMap<>();
        map2.put("301","offline");
        map2.put("302","offline");
        HttpClientKeepSession.pushOnline(trainNumberStr, JSON.toJSONString(map2));
    }
}