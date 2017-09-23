package com.thit.tibdm.NettyTransmission.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thit.tibdm.NettyTransmission.TxiDecoder;
import com.thit.tibdm.NettyTransmission.TxiEncoder;
import com.thit.tibdm.NettyTransmission.handler.EchoServerHandler;
import com.thit.tibdm.mq.bridge.MqProducer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


/**
 * Created by dongzhiquan on 2017/2/10.
 */
public class ServerUtil {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ClientUtil.class);

//    private static int partition_num =
//            Integer.parseInt(ResourceUtil.getProValueByNameAndKey("kafka-mq", "partition_num"));

    /**
     * 建立server连接
     *
     * @param port 端口
     * @throws Exception 异常
     */
    public static void bind(int port) throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            ch.pipeline().addLast(new TxiDecoder());
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            ch.pipeline().addLast(new TxiEncoder());
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 消息写入kafka  失败的话写入文件
     *
     * @param body 消息体
     */

    public static void saveMsgToKafka(String body) {
        try {
            //保存到kafka
            MqProducer.sendMsg(body, "test1");
//            MqProducer.sendMsg(partition_num, new String(body, Charsets.UTF_8));
        } catch (Exception e) {
            //失败消息保存到文件
            logger.error(e.toString());
        }

    }
}
