package com.thit.tibdm.db.bridge.util;

/**
 * @author wanghaoqiang
 */
public interface CassandraV2Table {
    /**
     *
     */
    String collect_type = "COLLECT_TYPE";
    /**
     *
     */
    String machine_id = "MACHINE_ID";
    /**
     *
     */
    String collect_time = "COLLECT_TIME";
    /**
     *
     */
    String attr_list = "ATTR_LIST";
    /**
     *
     */
    String attr_name = "ATTR_NAME";
    /**
     *
     */
    String attr_value = "ATTR_VALUE";
    /**
     *
     */
    String receive_time = "RECEIVE_TIME";
    /**
     *
     */
    String save_time = "SAVE_TIME";
    /**
     *
     */
    String uuid = "UUID";
    /**
     *
     */
    String json = "JSON";
    /**
     *
     */
    String attr = "ATTR";
    /**
     * 表名
     */
    String object_name = "OBJECT_NAME";
    /**
     * 补录表名
     */
    String makeup_data = "makeup_data";
    /**
     *
     */
    String protocol = "protocol";

    String collect_date = "COLLECT_DATE";
}
