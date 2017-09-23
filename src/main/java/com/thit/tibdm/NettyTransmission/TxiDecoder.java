package com.thit.tibdm.NettyTransmission;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.io.Charsets;

import java.util.List;

/**
 * Created by wanghaoqiang on 2017/3/13.
 *
 * @author wanghaoqiang
 */
public class TxiDecoder extends ByteToMessageDecoder {
    /**
     * @param channelHandlerContext 管道
     * @param in 输入
     * @param out 输出
     * @throws Exception 异常
     */
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        // 获取协议的版本
        int version = in.readInt();
        // 获取消息长度
        int contentLength = in.readInt();
        // 获取SessionId
        byte[] sessionByte = new byte[36];
        in.readBytes(sessionByte);
        String sessionId = new String(sessionByte, Charsets.UTF_8);

        // 组装协议头
        Header header = new Header(version, contentLength, sessionId);

        // 读取消息内容
        byte[] content = in.readBytes(in.readableBytes()).array();

        Message message = new Message(header, content);

        out.add(message);
    }
}
