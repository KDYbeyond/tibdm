package com.thit.tibdm.NettyTransmission.util;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author 匡东洋 E-mail:473948143@qq.com
 * @version 创建时间：2017年7月11日 下午2:27:44 类说明
 */
public class TelnetServer implements Runnable {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TelnetServer.class);

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.childHandler(new ChildChannelHandler());
            String Telnet_port = ResourceUtil.getProValueByNameAndKey("server", "Telnet_port");
            int telnet_port = Integer.parseInt(Telnet_port.trim());
            // 绑定端口
            LOGGER.info("启动查询服务器，端口为：" + telnet_port);
            ChannelFuture f = b.bind(telnet_port).sync();
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅的退出
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
