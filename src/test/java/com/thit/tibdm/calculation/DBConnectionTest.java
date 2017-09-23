package com.thit.tibdm.calculation;

import com.thit.tibdm.sparkstream.util.ProtocolUtil;
import com.thit.tibdm.util.ResourceUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * @author: dongzhiquan  Date: 2017/6/29 Time: 9:51
 */
public class DBConnectionTest {

    private static final Logger logger= LoggerFactory.getLogger(DBConnectionTest.class);

    @Test
    public void testGetTagNameList() throws Exception {

//        logger.info(DBConnection.getTagNameList().toString());
    }

    @Test
    public void testGetListBySql() throws Exception {

    }

    @Test
    public void testGetColumn1List() throws Exception {

    }

    @Test
    public void testGetCollectInfoByColumn1() throws Exception {

    }

    @Test
    public void testExecuteSql() throws Exception {
        String sql= ResourceUtil.getProValueByNameAndKey("cassandra-db","updateok")+"\'protocol3&4\'";
        logger.error(sql);
//        DBConnection.executeSql(sql);
//        ProtocolUtil.updateProOK("protocol3&4");
    }
}