package com.thit.tibdm.NettyTransmission.client;

import com.thit.tibdm.NettyTransmission.util.RemoteConnect;
import com.thit.tibdm.util.ResourceUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * Created by wanghaoqiang on 2017/3/13.
 */
public enum NettyConnection {
    /**
     * 实例
     */
    INSTANCE;
    /**
     * 管道
     */
    private Channel instance;
    /**
     * 事件组
     */
    private EventLoopGroup group;

    NettyConnection() {

        group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TxiConnectionHandler(new RemoteConnect()));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            instance = bootstrap.connect(ResourceUtil.getProValueByNameAndKey("server", "server_ip"),
                    Integer.parseInt(ResourceUtil.getProValueByNameAndKey("server", "server_port")))
                    .sync().channel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得netty连接实例
     *
     * @return Channel
     */
    public Channel getInstance() {
        return instance;
    }

    /**
     * 关闭netty连接实例
     */
    public void stopConnection() {
        group.shutdownGracefully();
    }
}
