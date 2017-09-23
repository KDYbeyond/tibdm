package com.thit.tibdm.NettyTransmission.handler;

import com.thit.tibdm.NettyTransmission.entity.MsgTransmission;
import com.thit.tibdm.NettyTransmission.util.BomServerUtil;
import com.thit.tibdm.NettyTransmission.util.TrainConnectionManager;
import com.thit.tibdm.util.CrcUtil;
import com.thit.tibdm.util.DateUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dongzhiquan on 2017/4/10.
 */
public class BomServerHandler extends ChannelInboundHandlerAdapter {

    // MessageProtocol messageProtocol = DoSingleExcel.INSTANCE.getInstance();
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BomServerHandler.class);
    /**
     * java8提供的线程安全的类，确保正确的相加
     */
    AtomicInteger i = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
         * 需要在此处将车号取出,然后拼接到数据BODY后面
         */
        MsgTransmission msgTransmission = (MsgTransmission) msg;
        // 分别获得列车ID和连接设备ID
        String trainID = msgTransmission.getBianzuId() + "";
        String deviceID = msgTransmission.getMachineId() + "";
        TrainConnectionManager.addConn(trainID, deviceID, ctx);
        LOGGER.info(
                "This is" + i.incrementAndGet() + "times receive client:[" + msgTransmission.getBody().length + "个字节]");
        ByteBuf buf = getBytebuf(msgTransmission);
        ctx.writeAndFlush(buf);
    }

    /**
     * 下行返回消息
     *
     * @param msgTransmission 消息体
     * @return ByteBuf 缓冲字节
     * @throws ParseException 异常
     */
    public ByteBuf getBytebuf(MsgTransmission msgTransmission) throws ParseException {

        ByteBuf buf = Unpooled.buffer();
        ByteBuf send = Unpooled.buffer();
        // 状态消息
        buf.writeMedium(msgTransmission.getHead());// 头
        buf.writeByte(msgTransmission.getType());
        buf.writeInt((int) msgTransmission.getId());
        buf.writeByte(msgTransmission.getProType());
        buf.writeByte(msgTransmission.getVersion());
        // 数据长度
        if (msgTransmission.getType() != 01) {
            buf.writeInt((int) msgTransmission.getLength());
        } else {
            buf.writeInt(24);
        }
        buf.writeByte(msgTransmission.getCity());
        buf.writeByte(msgTransmission.getLineNum());
        buf.writeShort(msgTransmission.getBianzuId());

        send.writeShort(msgTransmission.getBianzuId());
        LOGGER.info("编组ID为：" + msgTransmission.getBianzuId());
        buf.writeByte(msgTransmission.getMachineId());
        buf.writeByte(msgTransmission.getYear());
        buf.writeByte(msgTransmission.getMonth());
        buf.writeByte(msgTransmission.getDay());
        buf.writeByte(msgTransmission.getHour());
        buf.writeByte(msgTransmission.getMinu());
        buf.writeByte(msgTransmission.getSe());
        buf.writeShort(msgTransmission.getMs());
        if (msgTransmission.getType() != 01) {
            // 不为状态消息
            buf.writeBytes(msgTransmission.getYuliu());// 头
            buf.writeShort(msgTransmission.getCrc());
        } else {
            // 获取需要校验的数据包
            byte[] crcData = new byte[24];
            buf.getBytes(3, crcData);
            int crc = CrcUtil.crc_16_CCITT_False(crcData, crcData.length);
            buf.writeShort(crc);
        }
        buf.writeMedium(msgTransmission.getEnd());
        if (msgTransmission.getType() == 01) {
            // 从数据包中获取时间
            long timeStame = getTimeStame(msgTransmission);
            LOGGER.info("时间戳为：" + timeStame);
            send.writeLong(timeStame);
            // send.writeInt((int) timeStame);
            send.writeBytes(msgTransmission.getBody());
            LOGGER.info("送到解析端的长度为：" + send.readableBytes());
            // 增加8位字节来存储时间
            byte[] body = new byte[10 + msgTransmission.getBody().length];
            send.readBytes(body);

            //判断是否可以发送数据，查询的key为车号拼上设备ID
            if (TrainConnectionManager.isSendData(msgTransmission.getBianzuId() + "_" + msgTransmission.getMachineId())) {
                BomServerUtil.saveMsgToKafka(msgTransmission.getBianzuId() + "", body);
            }
        }
        int lenth;
        if (msgTransmission.getType() == 01) {
            lenth = 32;
        } else {
            lenth = 50;
        }
        byte[] response = new byte[lenth];
        buf.getBytes(0, response);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < response.length; i++) {
            String str = Integer.toHexString(response[i] & 0xFF);
            if (str.length() == 1) {
                str = '0' + str;
            }
            // LOGGER.info(str);
            sb.append(str);
        }

        LOGGER.info(sb.toString());
        LOGGER.info("字节长度为" + sb.length() / 2);
        return buf;
    }

    /**
     * @param msgTransmission 消息
     * @return long
     * @throws ParseException 异常
     */
    private long getTimeStame(MsgTransmission msgTransmission) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        StringBuffer time = new StringBuffer();
        time.append("20");
        time.append(DateUtil.getAddZero(msgTransmission.getYear()));
        time.append(DateUtil.getAddZero(msgTransmission.getMonth()));
        time.append(DateUtil.getAddZero(msgTransmission.getDay()));
        time.append(DateUtil.getAddZero(msgTransmission.getHour()));
        time.append(DateUtil.getAddZero(msgTransmission.getMinu()));
        time.append(DateUtil.getAddZero(msgTransmission.getSe()));
        time.append(DateUtil.getAddZero(msgTransmission.getMs()));
        Date parse = sdf.parse(time.toString());
        return parse.getTime();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught ex, conn={}", cause.toString());
        LOGGER.error("关闭连接上下文", cause.toString());
        ctx.close();
    }
}
