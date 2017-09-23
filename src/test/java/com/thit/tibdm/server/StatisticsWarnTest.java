package com.thit.tibdm.server;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaoqiang on 2017/7/31.
 */
public class StatisticsWarnTest {
    @Test
    public void init() throws Exception {
        StatisticsWarn.init();
        CountDownLatch countDownLatch=new CountDownLatch(100);
        countDownLatch.await(1000, TimeUnit.SECONDS);
    }
    @Test
    public void testUpdate(){
        List<String> tableNames = StatisticsWarn.getTableNames();
        for (String tableName:tableNames) {
            StatisticsWarn.updateProtocol(tableName);
        }
    }

}