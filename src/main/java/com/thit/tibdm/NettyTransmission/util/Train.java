package com.thit.tibdm.NettyTransmission.util;

/**
 * @author 匡东洋 E-mail:473948143@qq.com
 * @version 创建时间：2017年7月4日 上午10:31:08 类说明
 */
public class Train {
    /**
     * 列车的唯一ID
     */
    private String trainID;
    /**
     * 列车前面的连接设备
     */
    private ConnectionDevice frontConnectionDevice;
    /**
     * 列车后面的连接设备
     */
    private ConnectionDevice behindConnectionDevice;

    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public ConnectionDevice getFrontConnectionDevice() {
        return frontConnectionDevice;
    }

    public void setFrontConnectionDevice(ConnectionDevice frontConnectionDevice) {
        this.frontConnectionDevice = frontConnectionDevice;
    }

    public ConnectionDevice getBehindConnectionDevice() {
        return behindConnectionDevice;
    }

    public void setBehindConnectionDevice(ConnectionDevice behindConnectionDevice) {
        this.behindConnectionDevice = behindConnectionDevice;
    }

}
