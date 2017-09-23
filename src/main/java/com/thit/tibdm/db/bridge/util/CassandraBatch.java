package com.thit.tibdm.db.bridge.util;

import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * cassandra批量插入
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-06-05 11:39
 **/
public class CassandraBatch {
    /**
     * 日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CassandraBatch.class);

    /**
     * 批量插入数据
     *
     * @param list 参数
     */
    public static void batchInserter(List<String> list) {
        Session instance = CassandraSingleConnect.INSTANCE.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("BEGIN BATCH");
        list.forEach(l -> stringBuffer.append("   INSERT INTO shanghai JSON '" + l + "'"));
        stringBuffer.append("APPLY BATCH");
        instance.execute(stringBuffer.toString());
    }
}
