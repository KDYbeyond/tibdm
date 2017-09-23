package com.thit.tibdm.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author: dongzhiquan  Date: 2017/6/28 Time: 19:55
 */
public class CSVFileUtilTest {

    private static final Logger logger= LoggerFactory.getLogger(CSVFileUtilTest.class);

    @Test
    public void testReadLine() throws Exception {

        logger.info(CSVFileUtil.fromCSVLine("C:/Users/zc/Desktop/data_import.csv",10).toString());

    }

    @Test
    public void testFromCSVLine() throws Exception {
        logger.info(CSVFileUtil.fromCSVLine("C:/Users/zc/Desktop/data_import.csv",10).toString());
    }

    @Test
    public void testFromCSVLinetoArray() throws Exception {
        logger.info(CSVFileUtil.fromCSVLinetoArray("C:/Users/zc/Desktop/data_import.csv").toString());
    }

    @Test
    public void testToCSVLine() throws Exception {
        String[] s={"1","2"};
        logger.info(CSVFileUtil.toCSVLine(s));
    }

    @Test
    public void testToCSVLine1() throws Exception {
        ArrayList arrayList=new ArrayList();
        arrayList.add("1");
        logger.info(CSVFileUtil.toCSVLine(arrayList));
    }
}