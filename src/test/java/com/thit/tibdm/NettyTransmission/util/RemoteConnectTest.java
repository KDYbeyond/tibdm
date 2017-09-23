package com.thit.tibdm.NettyTransmission.util;

import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * 连接重置
 * Created by wanghaoqiang on 2017/7/5.
 */
public class RemoteConnectTest {
    public final static Logger LOGGER = LoggerFactory.getLogger(RemoteConnectTest.class);

//    @Test
//    public void getConnect() throws Exception {
//
//        RemoteConnect.init();
//        Channel connect1 = RemoteConnect.getConnect();
//        Channel connect2 = RemoteConnect.getConnect();
//
//        if (connect1 == connect2) {
//            LOGGER.info("同一个连接");
//        }
//
//        Channel connect3 = RemoteConnect.reConnect();
//
//        if (connect3 != connect2) {
//            LOGGER.info("新的连接");
//        }
//        Channel connect4 = RemoteConnect.getConnect();
//        if (connect3 == connect4) {
//            LOGGER.info("相同的连接");
//        }
//    }

}