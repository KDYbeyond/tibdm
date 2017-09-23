package com.thit.tibdm.NettyTransmission.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author 匡东洋 E-mail:473948143@qq.com
 * @version 创建时间：2017年7月4日 上午10:46:23 类说明 :线程类，用来检测每个连接是否有心跳 如果没有就从集合中移除
 */
public class CheckThread implements Runnable {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(CheckThread.class);
    /**
     * 时间转换格式
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
        while (true) {
            Set<Entry<String, ConnectionDevice>> connectionSet = Global.connections.entrySet();
            if (connectionSet.size() > 0) {
                logger.info("连接数目：" + TrainConnectionManager.getConnectionNum());
                for (Entry<String, ConnectionDevice> entry : connectionSet) {
                    String key = entry.getKey();
                    ConnectionDevice connectionDevice = entry.getValue();
                    logger.info("连接的设备：" + key);
                    // 如果连接释放
                    long lastUpdateTime = 0;
                    try {
                        lastUpdateTime = dateFormat.parse(connectionDevice.getLastUpdateTime()).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if ((System.currentTimeMillis() - lastUpdateTime) > (Integer
                            .parseInt(ResourceUtil.getProValueByNameAndKey("server", "timeThreshold")) * 1000)) {
                        connectionDevice.getChannel().close();
                        TrainConnectionManager.delete(key);
                        TrainConnectionManager.changeConnectionNum(-1);
                        String deviceID=connectionDevice.getDeviceID();
                        // 切换接收数据的设备
                        switch (deviceID){
                            case 1+"":
                                String newKey=key.replace("_1","_2");
                                ConnectionDevice connectionDevice2 = TrainConnectionManager.getConnectionDevice(newKey);
                                if (connectionDevice2!=null){
                                    connectionDevice2.setIsReceived(new AtomicBoolean(true));
                                }
                                break;
                            case 2+"":
                                String newKey2=key.replace("_2","_1");
                                ConnectionDevice connectionDevice3 = TrainConnectionManager.getConnectionDevice(newKey2);
                                if (connectionDevice3!=null){
                                    connectionDevice3.setIsReceived(new AtomicBoolean(true));
                                }
                                break;
                        }
                    }
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


