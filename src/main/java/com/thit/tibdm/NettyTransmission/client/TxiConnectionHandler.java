package com.thit.tibdm.NettyTransmission.client;

import com.thit.tibdm.NettyTransmission.util.RemoteConnect;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wanghaoqiang on 2017/3/13.
 *
 * @author wanghaoqiang
 */
public class TxiConnectionHandler extends ChannelInboundHandlerAdapter {
    private RemoteConnect client;

    public TxiConnectionHandler(RemoteConnect client) {
        this.client = client;
    }

    /**
     * 布尔标识
     */
    private static AtomicInteger counter = new AtomicInteger(0);
    /**
     * 日志
     */
    public static final Logger logger = LoggerFactory.getLogger(TxiConnectionHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("已经连接上服务器。。。");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int andIncrement = counter.getAndIncrement();
//        logger.info("返回的数量:" + andIncrement);

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.updateStatus(false);
        client.doConnect();
    }
}
