package com.thit.tibdm.NettyTransmission;

import com.thit.tibdm.NettyTransmission.entity.MsgTransmission;
import com.thit.tibdm.NettyTransmission.util.Constants;
import com.thit.tibdm.NettyTransmission.util.RemoteConnect;
import com.thit.tibdm.util.ResourceUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by dongzhiquan on 2017/4/10.
 *
 * @author wanghaoqiang
 */
public class BomDecoder extends ByteToMessageDecoder {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BomDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        LOGGER.info("收到的可读长度为：" + in.readableBytes());
        try {
            /**
             * 当读取到的字节数大于包头时执行下面方法
             * 起码要大于包头的长度+后面的长度时候才会去读取
             *
             */
            while (in.readableBytes() >= Constants.HEADER_LEN + 11) {
                //1.记录当前读取位置位置.如果读取到非完整的frame,要恢复到该位置,便于下次读取
                in.markReaderIndex();
                if (in != null) {
                    out.add(decodeFrame(in));
                }
            }
        } catch (Exception e) {
            //2.读取到不完整的frame,恢复到最近一次正常读取的位置,便于下次读取
            LOGGER.error("出现包解析异常");
            e.printStackTrace();
        }
    }

    /**
     * 对数据包进行粘包处理
     *
     * @param in ByteBuf
     * @return MsgTransmission
     * @throws Exception 不完整的包invalid frame
     */
    private MsgTransmission decodeFrame(ByteBuf in) throws Exception {
        //进行数据转发，先进性拷贝然后发送
        ByteBuf copy = in.copy();
        int bufferSize = in.readableBytes();
        //获取所有的数据
        byte[] all = new byte[bufferSize];
        in.getBytes(0, all);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < all.length; i++) {
            String str = Integer.toHexString(all[i] & 0xFF);
            if (str.length() == 1) {
                str = '0' + str;
            }
            sb.append(str + " ");
        }
        LOGGER.info("数据区内容为：[" + sb.toString() + "]");


        MsgTransmission msg = new MsgTransmission();
        //读取头
        int head = in.readUnsignedMedium();
        LOGGER.info("包头：" + head);
        msg.setHead(head);
        //读取消息类型
        short type = in.readUnsignedByte();
        msg.setType(type);
        //读取消息ID
        long id = in.readUnsignedInt();
        msg.setId(id);
        //读取通信协议类型
        short proType = in.readUnsignedByte();
        msg.setProType(proType);
        //读取通讯新协议版本号
        short version = in.readUnsignedByte();
        msg.setVersion(version);
        //读取数据长度字节
        long length = in.readUnsignedInt();
        msg.setLength(length);
        long msgLength = length + Constants.HEADER_LEN + Constants.CYC_LEN + Constants.END_LEN;
        if (bufferSize != msgLength) {
            LOGGER.error("本应该的长度为：" + msgLength);
            LOGGER.error("收到的长度为：" + msgLength);
            LOGGER.debug("有问题的包为：[" + sb.toString() + "]");
            in.clear();
            throw new Exception("不完整的包invalid frame");
        }

        //读取城市ID
        short city = in.readUnsignedByte();
        msg.setCity(city);
        LOGGER.info("城市ID：" + city);
        //读取线路ID
        short lineNum = in.readUnsignedByte();
        msg.setLineNum(lineNum);
        LOGGER.info("线路ID：" + lineNum);
        //读取编组ID
        int bianzuId = in.readUnsignedShort();
        LOGGER.error("======转发前的车号是  "+bianzuId+"");
        if (ResourceUtil.getProValueByNameAndKey("server", "forward").equals("true")) {
            String remoteCh=ResourceUtil.getProValueByNameAndKey("server", "remotech");
            if (remoteCh.indexOf((bianzuId+""))!=-1 ) {
                LOGGER.error("转发的车号是  "+bianzuId+"");
                RemoteConnect.getConnThread().execute(() -> {
                    RemoteConnect factory = RemoteConnect.getConnectFactory();
                    Channel instance = factory.getConnect();

                    if (factory.getStatus()) {
                        instance.writeAndFlush(copy);
                    } else {
                        LOGGER.error("不发送数据，连接状态出现问题:" + factory.toString());
                    }
                });
            }
        }

        msg.setBianzuId(bianzuId);
        LOGGER.info("编组ID：" + bianzuId);
        //读取设备ID
        short machineId = in.readUnsignedByte();
        msg.setMachineId(machineId);
        LOGGER.info("设备ID：" + machineId);
        //读取日期
        short year = in.readUnsignedByte();
        msg.setYear(year);
        short month = in.readUnsignedByte();
        msg.setMonth(month);
        short day = in.readUnsignedByte();
        msg.setDay(day);
        short hour = in.readUnsignedByte();
        msg.setHour(hour);
        short minu = in.readUnsignedByte();
        msg.setMinu(minu);
        short se = in.readUnsignedByte();
        msg.setSe(se);
        int ms = in.readUnsignedShort();
        msg.setMs(ms);
        LOGGER.info("时间：" + year + "-" + month + "-" + day + ":" + hour + "-" + minu + "-" + se);


        //读取预留字节
        byte[] yuliu;
        if (msg.getType() == 1) {
            yuliu = new byte[8];
        } else {
            yuliu = new byte[18];
        }
        in.readBytes(yuliu);

        msg.setYuliu(yuliu);
        //读取消息体
        long bodyLength;
        if (msg.getType() == 1) {
            bodyLength = length - 32;
        } else {
            bodyLength = length - 42;
        }
        byte[] body = new byte[(int) bodyLength];
        in.readBytes(body);
        msg.setBody(body);

        //读取校验字节
        int crc = in.readUnsignedShort();
        msg.setCrc(crc);
        //读取包尾
        int end = in.readUnsignedMedium();
        msg.setEnd(end);
        LOGGER.info("数据的内容为：" + msg.toString());
        return msg;
    }

}

