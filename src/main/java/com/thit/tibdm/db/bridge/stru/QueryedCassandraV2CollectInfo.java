package com.thit.tibdm.db.bridge.stru;

import java.util.List;
import java.util.Map;

/**
 * Created by dongzhiquan on 2017/2/6.
 * 新增一个实体类用于处理Long类型的collectTime到string类型的转变
 */
public class QueryedCassandraV2CollectInfo {
    /**
     * 车号ID
     */
    private String machineId;
    /**
     * 采集类型
     */
    private String collectType;
    /**
     * 采集时间
     */
    private String collectTime;
    /**
     * 入库时间
     */
    private String saveTime;
    /**
     * 接收时间
     */
    private String receiveTime;
    /**
     * 属性列表 处理查询条件时使用
     */
    private List<CassandraV2Attr> attrList;
    /**
     * 属性  实际查询结果转实体类时使用
     */
    private Map attr;

    public QueryedCassandraV2CollectInfo() {
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

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public List<CassandraV2Attr> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<CassandraV2Attr> attrList) {
        this.attrList = attrList;
    }

    public Map getAttr() {
        return attr;
    }

    public void setAttr(Map attr) {
        this.attr = attr;
    }
}
