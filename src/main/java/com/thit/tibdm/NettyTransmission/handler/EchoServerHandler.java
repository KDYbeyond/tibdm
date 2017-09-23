package com.thit.tibdm.NettyTransmission.handler;


import com.alibaba.fastjson.JSON;
import com.thit.tibdm.NettyTransmission.Message;
import com.thit.tibdm.NettyTransmission.ProtocolAnalysis.ProtocolFactory;
import com.thit.tibdm.NettyTransmission.ProtocolAnalysis.Protocolntercace;
import com.thit.tibdm.NettyTransmission.util.ServerUtil;
import com.thit.tibdm.db.bridge.util.TXIBigDBJNDINames;
import com.thit.tibdm.mq.bridge.CassandraData;
import com.xicrm.common.TXISystem;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by dongzhiquan on 2017/2/10.
 *
 * @author wanghaoqiang
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EchoServerHandler.class);

    /**
     * java8提供的线程安全的类，确保正确的相加
     */
    AtomicInteger i = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message packet = (Message) msg;
        CassandraData parse = parse(packet.getContent());
        String body = JSON.toJSONString(parse);

//        String body = new String(packet.getContent(), Charsets.UTF_8);
        logger.info("This is " + i.incrementAndGet() + " times receive client : ["
                + body.length() + "]");
        //消息写入kafka
        ServerUtil.saveMsgToKafka(body);
//        ServerUtil.saveMsgToKafka(parse);
        packet.setContent(body.getBytes());
        ctx.writeAndFlush(packet);
    }

    /**
     * 将数据包转换成json字符串形式
     * @param content 数组内容
     * @return CassandraData 数据
     * @throws Exception  异常
     */
    private CassandraData parse(byte[] content) throws Exception {
        /**
         * 通过接口方式来使用，同时动态生成类，来达到根据配置文件来修改对应的实现累
         */
        String impl = TXISystem.config.getProperty(TXIBigDBJNDINames.Tibdm_Protocol_ImplClass);
        Protocolntercace info = null;
        if (impl == null) {
            //使用默认的解析类来解析
        } else {
            info = ProtocolFactory.getInst(impl);
        }
        CassandraData cassandraData = info.parseProtocol(content);
        return cassandraData;
    }
}

