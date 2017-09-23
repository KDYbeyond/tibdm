package com.thit.tibdm.NettyTransmission.client;

import com.thit.tibdm.NettyTransmission.Header;
import com.thit.tibdm.NettyTransmission.Message;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by wanghaoqiang on 2017/3/13.
 * @author wanghaoqiang
 */
public class TxiConnection {
    /**
     * 日志
     */
    public  static final Logger logger = LoggerFactory.getLogger(TxiConnection.class);
    /**
     * 管道
     */
    private static Channel channel = NettyConnection.INSTANCE.getInstance();

    /**
     * 发送数据包以 -|-结尾
     *
     * @param content 内容
     */
    public static void send(final byte[] content) {
        logger.info("正在发送===" + new String(content));
        Message msg = new Message();
        msg.setContent(content);
        Header header = new Header();
        header.setSessionId(UUID.randomUUID().toString());
        header.setVersion(1);
        header.setContentLength(content.length);
        msg.setHeader(header);
        channel.writeAndFlush(msg);
    }

    /**
     * 结束连接
     *
     * @param channel 管道
     */
    public static void stop(Channel channel) {
        NettyConnection.INSTANCE.stopConnection();
    }

}
