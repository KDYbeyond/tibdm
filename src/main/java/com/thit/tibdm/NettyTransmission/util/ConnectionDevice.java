package com.thit.tibdm.NettyTransmission.util;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author 匡东洋 E-mail:473948143@qq.com
 * @version 创建时间：2017年7月4日 上午10:33:47 类说明
 */
public class ConnectionDevice extends NettyClient {
    /**
     * 列车连接设备的ID
     */

    private String deviceID;
    /**
     * 连接设备的连接管道
     */

    private Channel channel;
    /**
     * 最后发送数据的时间
     */

    private String lastUpdateTime;
    /**
     * 是否接收数据
     */

    private AtomicBoolean isReceived = new AtomicBoolean(false);

    /**
     * 是否接收数据boolean值
     */
    private boolean isReceivedDataDevice;

    public AtomicBoolean getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(AtomicBoolean isReceived) {
        this.isReceived = isReceived;
        this.isReceivedDataDevice = isReceived.get();
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * 该连接设备是否已经连接到服务端
     *
     * @return boolean
     */

    public boolean isConnected() {
        boolean flag = false;
        flag = channel.isActive();
        return flag;
    }

    /**
     * 连接服务器
     *
     * @param host
     * @param port
     */

    @Override
    public void connect(String host, int port) {
        super.connect(host, port);
    }

    public boolean isReceivedDataDevice() {
        return isReceived.get();
    }
}
