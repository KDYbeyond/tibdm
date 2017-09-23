package com.thit.tibdm.db.bridge.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: dongzhiquan  Date: 2017/6/29 Time: 9:53
 */
public class CassandraV2UtilTest {

    private static final Logger logger = LoggerFactory.getLogger(CassandraV2UtilTest.class);

    @Test
    public void testGetTimeStamp() throws Exception {

        logger.info(CassandraV2Util.getTimeStamp("2017/06/12 10:20:10"));
    }

    @Test
    public void testGetStrTime() throws Exception {
        logger.info(CassandraV2Util.getStrTime("1497234010000"));
    }

    @Test
    public void testMapToCollectInfo() throws Exception {

    }

    @Test
    public void testGetQueryJsonKeys() throws Exception {

    }

    @Test
    public void testQueryedCollectTimeLongMap() throws Exception {

    }

    @Test
    public void testGetFilterMapCollectTimeString() throws Exception {

    }

    @Test
    public void testQueryedCollectTimeStringMap() throws Exception {

    }

    @Test
    public void testRemoveDuplicate() throws Exception {

        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("123");
        logger.info(CassandraV2Util.removeDuplicate(list).toString());

    }

    @Test
    public void testGetTongjiList() throws Exception {
        String keys = "[\"ZT1\"]";
        logger.info(CassandraV2Util.getTongjiList(keys).toString());
    }

    @Test
    public void testGetCql() throws Exception {
        String keys = "[\"ZT1\"]";

        logger.info(CassandraV2Util.getCql(keys));
    }

    @Test
    public void testGetAttrMap() throws Exception {

    }

    @Test
    public void testDoJsonArray() throws Exception {

    }

    @Test
    public void testGetNowDate() throws Exception {

        logger.error(CassandraV2Util.getNowDate());

    }

    /**
     * 求两个日期之间的日期
     *
     * @throws Exception
     */
    @Test
    public void testGetBetweenDates() throws Exception {
        long l1 = 1503213025000l;
        long l2 = 1503557204000l;
        System.out.println(JSON.toJSONString(CassandraV2Util.getBetweenDates(l1, l2)));
    }

    /**
     * 日期cql语句
     *
     * @throws Exception
     */
    @Test
    public void testGetCqlDate() throws Exception {
        long l1 = 1503213025000l;
        long l2 = 1503559799100l;
        String s = CassandraV2Util.getCqlDate(l1, l2);
        System.out.println(s);
    }
}