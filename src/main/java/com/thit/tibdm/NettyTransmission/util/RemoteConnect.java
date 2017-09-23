package com.thit.tibdm.NettyTransmission.util;

import com.thit.tibdm.NettyTransmission.client.TxiConnectionHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 用作消息转发
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-07-05 16:13
 **/
public class RemoteConnect {

    /**
     * 连接状态
     */
    private AtomicBoolean flag = new AtomicBoolean(false);

    /**
     * 工作线程
     */
    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);

    private static RemoteConnect factory;

    public static RemoteConnect getConnectFactory() {
        if (factory == null) {
            synchronized (RemoteConnect.class) {
                if (factory == null) {
                    factory = new RemoteConnect();
                }
            }
        }
        return factory;
    }

    /**
     * 日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(RemoteConnect.class);

    /**
     * 管道
     */
    private Channel instance;

    /**
     * 启动
     */
    public Bootstrap bootstrap;
    /**
     * 管道列表
     */
    public static List<Channel> pool = new LinkedList<>();
    /**
     * EventLoopGroup
     */
    public static EventLoopGroup group;
    /**
     * 执行服务
     */
    public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

    /**
     * init
     */
    public synchronized void init() {
        LOGGER.info("生成了远程连接，为了转发数据使用");
        try {
            bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new IdleStateHandler(0, 0, 5));
                            p.addLast(new TxiConnectionHandler(RemoteConnect.getConnectFactory()));
                        }
                    });
            doConnect();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * getConnect
     *
     * @return Channel
     */
    public Channel getConnect() {
        return instance;
    }


    /**
     * getConnThread
     *
     * @return ExecutorService
     */
    public static ExecutorService getConnThread() {
        return fixedThreadPool;
    }


    public void doConnect() {
        if (instance != null && instance.isActive()) {
            return;
        }
        ChannelFuture future = bootstrap.connect(ResourceUtil.getProValueByNameAndKey("server", "server_ip"),
                Integer.parseInt(ResourceUtil.getProValueByNameAndKey("server", "server_port")));
        future.addListener((ChannelFutureListener) futureListener -> {
            if (futureListener.isSuccess()) {
                instance = futureListener.channel();
                flag.compareAndSet(false, true);
                LOGGER.info("连接成功!");
            } else {
                LOGGER.error("连接失败，十秒后重试");
                futureListener.channel().eventLoop().schedule(() -> doConnect(), 10, TimeUnit.SECONDS);
            }
        });
    }

    /**
     * 获取可连接状态
     *
     * @return
     */
    public static boolean getStatus() {
        return factory.flag.get();
    }

    /**
     * 修改可连接状态
     *
     * @return
     */
    public static void updateStatus(boolean bool) {
        factory.flag.set(bool);
    }

    @Override
    public String toString() {
        return "RemoteConnect{" +
                "flag=" + flag +
                ", workGroup=" + workGroup +
                ", instance=" + instance +
                ", bootstrap=" + bootstrap +
                '}';
    }
}
