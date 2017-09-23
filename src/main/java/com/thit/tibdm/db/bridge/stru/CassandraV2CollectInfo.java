package com.thit.tibdm.db.bridge.stru;

import java.util.List;
import java.util.Map;

/**
 * Created by rmbp13 on 16/9/20.
 */
public class CassandraV2CollectInfo {
    /**
     * 车号ID
     */
    private String machineId;
    /**
     * 车号
     */
    private String trainCode;
    /**
     * 采集类型
     */
    private String collectType;
    /**
     * 采集类型
     */
    private long collectTime;
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

    public CassandraV2CollectInfo() {
    }

    public CassandraV2CollectInfo(String machineId, String collectType, long collectTime, String saveTime, String receiveTime, List<CassandraV2Attr> attrList, Map attr) {
        this.machineId = machineId;
        this.collectType = collectType;
        this.collectTime = collectTime;
        this.saveTime = saveTime;
        this.receiveTime = receiveTime;
        this.attrList = attrList;
        this.attr = attr;
    }

    public CassandraV2CollectInfo(String machineId, String trainCode, String collectType, long collectTime, String saveTime, String receiveTime, List<CassandraV2Attr> attrList, Map attr) {
        this.machineId = machineId;
        this.trainCode = trainCode;
        this.collectType = collectType;
        this.collectTime = collectTime;
        this.saveTime = saveTime;
        this.receiveTime = receiveTime;
        this.attrList = attrList;
        this.attr = attr;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public Map getAttr() {
        return attr;
    }

    public void setAttr(Map attr) {
        this.attr = attr;
    }

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
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

    @Override
    public String toString() {
        return "CassandraV2CollectInfo{" +
                "machineId='" + machineId + '\'' +
                ", collectType='" + collectType + '\'' +
                ", collectTime='" + collectTime + '\'' +
                ", saveTime='" + saveTime + '\'' +
                ", receiveTime='" + receiveTime + '\'' +
                ", attrList=" + attrList +
                '}';
    }
}
