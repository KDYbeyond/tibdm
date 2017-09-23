package com.thit.tibdm.NettyTransmission.util;

import com.thit.tibdm.NettyTransmission.BomDecoder;
import com.thit.tibdm.NettyTransmission.handler.BomServerHandler;
import com.thit.tibdm.mq.bridge.MqProducer;
import com.thit.tibdm.server.InitServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by dongzhiquan on 2017/4/10.
 */
public class BomServerUtil {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(BomServerUtil.class);

    /**
     * 建立server连接
     *
     * @param port 端口
     * @throws Exception 异常
     */
    public static void bind(int port) throws Exception {
        InitServer.initModule();

        if (ResourceUtil.getProValueByNameAndKey("server", "forward").equals("true")) {
            RemoteConnect.getConnectFactory().init();
        }

        //启动其他的计算
        Thread checkThread = new Thread(new CheckThread());
        checkThread.start();
        Thread tel = new Thread(new TelnetServer());
        tel.start();

        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //获取协议
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
                            //在解码部分获取数据包长度来解析
                            ByteBuf buf = Unpooled.buffer();
                            buf.writeByte(0xba);
                            buf.writeByte(0xbb);
                            buf.writeByte(0xbc);
                            DelimiterBasedFrameDecoder deli = new DelimiterBasedFrameDecoder(65535, false, buf);
                            ch.pipeline().addLast(deli);
                            ch.pipeline().addLast(new BomDecoder());
                            ch.pipeline().addLast(new BomServerHandler());
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
     * 启动程序
     *
     * @throws Exception 异常
     */
    public static void startConnectionManager() throws Exception {
        bind(Integer.parseInt(ResourceUtil.getProValueByNameAndKey("server", "server_port")));
    }

    /**
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main(String[] args) throws Exception {
        startConnectionManager();
    }

    /**
     * 消息写入kafka  失败的话写入文件
     *
     * @param body 消息体
     */

    public static void saveMsgToKafka(String body) {

        try {
            //保存到kafka
            MqProducer.sendMsg(body, ResourceUtil.getProValueByNameAndKey("kafka-mq", "topicparse"));
//            MqProducer.sendMsg(partition_num,new String(body));
        } catch (Exception e) {
            //失败消息保存到文件
            e.printStackTrace();
            logger.error(e.toString());
            FileHandleUtil.createFiles(body);
        }

    }

    /**
     * 均匀发送到三个kafka的topic里面去，方便负载均衡
     *
     * @param ch    字符串
     * @param bytes 字节数组
     */
    public static void saveMsgToKafka(String ch, byte[] bytes) {
        //topic列表
        String topic = ResourceUtil.getProValueByNameAndKey("kafka-mq", "topicparse");
//        String topic = HashTopic.getTopic(ch, parseList);
        try {
            //保存到kafka
            MqProducer.sendMsg(bytes, topic);
        } catch (Exception e) {
            //失败消息保存到文件
            e.printStackTrace();
            logger.error(e.toString());
        }

    }
}
