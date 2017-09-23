package com.thit.tibdm.calculation.stru;

import com.thit.tibdm.util.DateUtil;

/**
 * Created by wanghaoqiang on 2016/12/9.
 */

public class CollectInfo {
    /**
     * 主键1:采集点名称
     */
    private String key;
    /**
     * 主键2:采集时间
     */
    private long column1;
    /**
     * 采集的数值
     */
    private String value;

    public CollectInfo() {
    }

    public CollectInfo(String key, int column1, String value) {
        this.key = key;
        this.column1 = column1;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getColumn1() {
        return column1;
    }

    public void setColumn1(long column1) {
        this.column1 = column1;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CollectInfo{" +
                "key='" + key + '\'' +
                ", column1=" + DateUtil.getFormat(column1) +
                ", value='" + value + '\'' +
                '}';
    }


}
