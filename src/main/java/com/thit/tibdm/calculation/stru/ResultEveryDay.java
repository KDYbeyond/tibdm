package com.thit.tibdm.calculation.stru;

import com.thit.tibdm.util.DateUtil;

/**
 * Created by wanghaoqiang on 2016/12/12.
 */
public class ResultEveryDay {
    /**
     * 对象名
     */
    private String objectName;
    /**
     * 采集点名称
     */
    private String tagName;
    /**
     * 统计的时间节点
     */
    private long time;
    /**
     * 最大值
     */
    private String max;
    /**
     * 最大值产生的时间
     */
    private long maxTime;
    /**
     * 最小值
     */
    private String min;
    /**
     * 最小值产生的时间
     */
    private long minTime;
    /**
     * 平均值
     */
    private String average;
    /**
     *统计数量
     */
    private String count;
    /**
     * 采集数据的总值
     */
    private String sum;

    public ResultEveryDay() {
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public long getMinTime() {
        return minTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public ResultEveryDay(String objectName, String tagName, long time, String max, long maxTime, String min, long minTime, String average, String count, String sum) {
        this.objectName = objectName;
        this.tagName = tagName;
        this.time = time;
        this.max = max;
        this.maxTime = maxTime;
        this.min = min;
        this.minTime = minTime;
        this.average = average;
        this.count = count;
        this.sum = sum;
    }


    @Override
    public String toString() {
        return "ResultEveryDay{" +
                "objectName='" + objectName + '\'' +
                ", tagName='" + tagName + '\'' +
                ", time=" + DateUtil.getFormat(time) +
                ", max='" + max + '\'' +
                ", maxTime=" + maxTime +
                ", min='" + min + '\'' +
                ", minTime=" + minTime +
                ", average='" + average + '\'' +
                ", count='" + count + '\'' +
                ", sum='" + sum + '\'' +
                '}';
    }
}

