package com.thit.tibdm.NettyTransmission.util;

import net.sf.json.JSONObject;
import org.slf4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author 匡东洋 E-mail:473948143@qq.com
 * @version 创建时间：2017年7月11日 下午2:28:03 类说明
 */
public class TelnetServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 帮助
     */
    private static final String help = "help:查看帮助\n" + "connectionNums:查看连接数量\n" + "train:查看连接车辆\n"
            + "connectionDevice:查看连接设备\n" + "sendDataDevice:查看可发送数据设备\n"+"connectionInformation:连接信息";
    /**
     * 连接数量
     */
    private static final String connectionNums = "connectionNums";
    /**
     * 车
     */
    private static final String train = "train";
    /**
     * 连接设备
     */
    private static final String connectionDevice = "connectionDevice";
    /**
     * 发送数据设备
     */
    private static final String sendDataDevice = "sendDataDevice";
    /**
     *
     */
    private static final String connectionInformation = "connectionInformation";
    /**
     * 打印日志
     */
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TelnetServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(help);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String datas = (String) msg;
        String result = "";
        logger.info(datas);
        switch (datas) {
            case "help":
                result = help;
                break;
            case connectionNums:
                int connectionNum = TrainConnectionManager.getConnectionNum();
                if (connectionNum == 0) {
                    result = "尚未有设备接入";
                } else {
                    result = connectionNum + "";
                }
                break;
            case train:
                result = TrainConnectionManager.getConnectionTrains();
                if ("".equals(result)) {
                    result = "没有连接的Train";
                }
                break;
            case connectionDevice:
                result = TrainConnectionManager.getConnectionDevices();
                if ("".equals(result)) {
                    result = "没有连接的设备";
                }
                break;
            case sendDataDevice:
                result= TrainConnectionManager.getSendDataConnectionDevices();
                if ("".equals(result)) {
                    result= "没有可以发送数据的设备";
                }
                break;
            case connectionInformation:
                JSONObject jsonObject = TrainConnectionManager.getConnectionInformation();
                result = jsonObject.toString();
                break;
            default:
                result="命令不存在";
                break;
        }
        ctx.writeAndFlush(result);
    }
}
