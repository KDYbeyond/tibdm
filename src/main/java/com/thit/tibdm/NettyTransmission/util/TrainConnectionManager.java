package com.thit.tibdm.NettyTransmission.util;

import io.netty.channel.ChannelHandlerContext;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 匡东洋 E-mail:473948143@qq.com
 * @version 创建时间：2017年7月4日 上午11:19:52 类说明 ：管理连接
 */
public class TrainConnectionManager {
    /**
     * 打印日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainConnectionManager.class);
    /**
     * 时间转换格式
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 添加连接
     *
     * @param train 列车
     */
    public static void add(Train train) {
        if (train.getFrontConnectionDevice() != null && train.getFrontConnectionDevice().isConnected()) {
            Global.connections.putIfAbsent(train.getTrainID() + "_" + train.getFrontConnectionDevice().getDeviceID(),
                    train.getFrontConnectionDevice());
        } else if (train.getBehindConnectionDevice() != null && train.getBehindConnectionDevice().isConnected()) {
            Global.connections.putIfAbsent(train.getTrainID() + "_" + train.getBehindConnectionDevice().getDeviceID(),
                    train.getBehindConnectionDevice());
        }
    }


    /**
     * 提出连接设备
     *
     * @param connectionID 设备ID
     */
    public static void delete(String connectionID) {
        Global.connections.remove(connectionID);
    }


    /**
     * 获得连接设备
     *
     * @param key 设备号
     * @return 链接设备
     */
    public static ConnectionDevice getConnectionDevice(String key) {
        return Global.connections.get(key);
    }


    /**
     * 获得连接设备的数量
     *
     * @return 连接数量
     */
    public static int getConnectionNum() {
        return Global.connectionNums.intValue();
    }


    /**
     * 连接数目变化
     *
     * @param i 改变值
     * @return 连接数量
     */
    public static AtomicInteger changeConnectionNum(int i) {
        Global.connectionNums = new AtomicInteger(Global.connectionNums.addAndGet(i));
        return Global.connectionNums;
    }


    /**
     * 判断某一个具体的设备是否有资格向服务器发送数据
     *
     * @param trainID            列车ID
     * @param connectionDeviceID 连接ID
     * @return 布尔
     */
    public static boolean isSendData(String trainID, String connectionDeviceID) {
        String key = trainID + "_" + connectionDeviceID;
        ConnectionDevice connectionDevice = Global.connections.get(key);
        return connectionDevice.getIsReceived().get();
    }


    /**
     * 方法重载 判断某一个具体的设备是否有资格向服务器发送数据
     *
     * @param key 关键字
     * @return boolean
     */
    public static boolean isSendData(String key) {
        ConnectionDevice connectionDevice = Global.connections.get(key);
        return connectionDevice.getIsReceived().get();
    }


    /**
     * 获得所有的连接车辆
     *
     * @return String
     */
    public static String getConnectionTrains() {
        Set<String> keySet = Global.connections.keySet();
        StringBuilder trains = new StringBuilder();
        for (String string : keySet) {
            trains.append(string.substring(0, string.length() - 2));
            trains.append("\n");
        }
        return trains.toString();
    }


    /**
     * 获得所有连接设备
     *
     * @return String
     */
    public static String getConnectionDevices() {
        Set<String> keySet = Global.connections.keySet();
        StringBuilder connectionDevice = new StringBuilder();
        for (String string : keySet) {
            connectionDevice.append(string);
            connectionDevice.append("\n");
        }
        return connectionDevice.toString();
    }


    /**
     * 获得可以发送数据的连接设备
     *
     * @return String 数据
     */
    public static String getSendDataConnectionDevices() {
        Set<String> keySet = Global.connections.keySet();
        StringBuilder sendDataEnableConnectionDevice = new StringBuilder();
        for (String string : keySet) {
            if (isSendData(string)) {
                sendDataEnableConnectionDevice.append(string);
                sendDataEnableConnectionDevice.append("\n");
            }
        }
        return sendDataEnableConnectionDevice.toString();
    }
    
    /**
     * 连接信息
     * @return JsonObject
     */
    public static JSONObject getConnectionInformation() {
        ConcurrentMap<String, ConnectionDevice> connectionDeviceMap = Global.connections;
        JSONObject jsonObject=JSONObject.fromObject(connectionDeviceMap);
        return jsonObject;
    }
    /**
     * 对发来的数据包进行处理
     * @param trainID  列车ID
     * @param deviceID 设备ID
     * @param ctx      管道
     */
    public static void addConn(String trainID, String deviceID, ChannelHandlerContext ctx){
        // 检测该连接是否已经存在
        String key = trainID + "_" + deviceID;
        // 同一个车上的另外一个设备
        String twinKey ="";
        if ("1".equals(deviceID))
            twinKey = trainID+"_2";
        if ("2".equals(deviceID))
            twinKey = trainID+"_1";
        // 如果管理map中不包含这两个连接
        if (!Global.connections.containsKey(key)&&!Global.connections.containsKey(twinKey)){
            addTrain(trainID,deviceID,ctx,new AtomicBoolean(true));
        }
        if(!Global.connections.containsKey(key)&&Global.connections.containsKey(twinKey)){
            addTrain(trainID,deviceID,ctx,new AtomicBoolean(false));
        }
    }

    /**
     * @param trainID 列车ID
     * @param deviceID 设备ID
     * @param ctx 管道
     * @param atomicBoolean 是否发送数据
     */
    public static void addTrain(String trainID, String deviceID, ChannelHandlerContext ctx,AtomicBoolean atomicBoolean){
        Train train = new Train();
        train.setTrainID(trainID);
        ConnectionDevice connectionDevice = new ConnectionDevice();
        connectionDevice.setDeviceID(deviceID);
        connectionDevice.setChannel(ctx.channel());
        connectionDevice.setIsReceived(atomicBoolean);
        String dateStr = dateFormat.format(System.currentTimeMillis());
        connectionDevice.setLastUpdateTime(dateStr);
        if (deviceID != null) {
            if ("1".equals(deviceID)) {
                train.setFrontConnectionDevice(connectionDevice);
            } else if ("2".equals(deviceID)) {
                train.setBehindConnectionDevice(connectionDevice);
            }
        }
        TrainConnectionManager.add(train);
        // 连接数目增加1
        TrainConnectionManager.changeConnectionNum(1);
    }
}
