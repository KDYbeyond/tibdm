package com.thit.tibdm.bom.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongzhiquan
 */
public class WarningProtocolBean implements Serializable {

    /**
     * SerialNumber : 9012
     * Name : 牵引逆变器状态异常
     * param : ["GK012","GK013"]
     * jexl : GK012 = 1 && GK013 = 1
     */

    private int SerialNumber;
    /**
     * 名称
     */
    private String Name;
    /**
     * 多长时间内
     */
    private int Timespan;

    /**
     * 发生次数
     */
    private int Frequency;

    /**
     * 持续时间
     */
    private int TimeDuration;


    /**
     *
     */
    private String Jexl;
    /**
     *
     */
    private List<String> Param;

    public int getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getTimespan() {
        return Timespan;
    }

    public void setTimespan(int timespan) {
        Timespan = timespan;
    }

    public int getFrequency() {
        return Frequency;
    }

    public void setFrequency(int frequency) {
        Frequency = frequency;
    }

    public int getTimeDuration() {
        return TimeDuration;
    }

    public void setTimeDuration(int timeDuration) {
        TimeDuration = timeDuration;
    }

    public String getJexl() {
        return Jexl;
    }

    public void setJexl(String jexl) {
        Jexl = jexl;
    }

    public List<String> getParam() {
        return Param;
    }

    public void setParam(List<String> param) {
        Param = param;
    }

    @Override
    public String toString() {
        return "WarningProtocolBean{" +
                "SerialNumber=" + SerialNumber +
                ", Name='" + Name + '\'' +
                ", Timespan=" + Timespan +
                ", Frequency=" + Frequency +
                ", TimeDuration=" + TimeDuration +
                ", Jexl='" + Jexl + '\'' +
                ", Param=" + Param +
                '}';
    }
}