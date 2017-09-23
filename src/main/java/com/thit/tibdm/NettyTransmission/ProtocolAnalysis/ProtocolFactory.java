package com.thit.tibdm.NettyTransmission.ProtocolAnalysis;

import com.thit.tibdm.base.InterfaceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成实现类
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-04-01 09:21
 **/
public class ProtocolFactory extends InterfaceFactory {
    /**
     *日志
     */
    public static final Logger logger = LoggerFactory.getLogger(ProtocolFactory.class);

    /**
     * 根据类的名字生成实现类
     *
     * @param className 类名
     * @return Protocolntercace 协议实例
     * @throws Exception 异常
     */
    public static Protocolntercace getInst(String className) throws Exception {
        try {
            return (Protocolntercace) getInstance(className);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
