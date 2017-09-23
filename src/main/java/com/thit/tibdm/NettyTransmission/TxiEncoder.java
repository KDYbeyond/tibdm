package com.thit.tibdm.NettyTransmission;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by wanghaoqiang on 2017/3/13.
 *
 * @author wanghaoqiang
 */
public class TxiEncoder extends MessageToByteEncoder<Message> {
    /**
     * @param channelHandlerContext 管道
     * @param message 消息
     * @param out 输出
     * @throws Exception 异常
     */
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        Header header = message.getHeader();
        // 写入Header信息
        out.writeInt(header.getVersion());
        out.writeInt(message.getContent().length);
        out.writeBytes(header.getSessionId().getBytes());
        // 写入消息主体信息
        out.writeBytes(message.getContent());
    }
}
