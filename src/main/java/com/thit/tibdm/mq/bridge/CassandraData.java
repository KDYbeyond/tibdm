package com.thit.tibdm.mq.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息解析类
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-03-31 16:20
 **/
public class CassandraData {
    /**
     *
     */
    public static final Logger logger = LoggerFactory.getLogger(CassandraData.class);
    /**
     * 车号ID
     */
    private String machineId;
    /**
     * 采集类型
     */
    private String collectType;
    /**
     * 完整采集信息
     */
    private String json;

    public CassandraData() {
    }

    public CassandraData(String machineId, String collectType, String json) {
        this.machineId = machineId;
        this.collectType = collectType;
        this.json = json;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getCollectType() {
        return collectType;
    }

    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "CassandraData{" +
                "machineId='" + machineId + '\'' +
                ", collectType='" + collectType + '\'' +
                ", json='" + json + '\'' +
                '}';
    }

}
