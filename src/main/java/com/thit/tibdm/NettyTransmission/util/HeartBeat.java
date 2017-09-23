package com.thit.tibdm.NettyTransmission.util;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

/**
 * @author 匡东洋 E-mail:473948143@qq.com
 * @version 创建时间：2017年7月4日 下午3:06:48 类说明： 心跳检测
 */
public class HeartBeat extends IdleStateAwareChannelHandler {
    /**
     * 参数
     */
    private int i = 0;

    @Override
    public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
        super.channelIdle(ctx, e);
        if (e.getState() == IdleState.WRITER_IDLE) {
            i++;
        }
        if (i == 3) {
            // 连接断掉
            e.getChannel().close();
        }
    }
}
