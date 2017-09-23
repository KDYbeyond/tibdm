package com.thit.tibdm.NettyTransmission.ProtocolAnalysis;

import com.thit.tibdm.base.InterfaceBase;
import com.thit.tibdm.mq.bridge.CassandraData;

/**
 * 协议解析接口
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-03-31 09:56
 **/
public interface Protocolntercace extends InterfaceBase {
    /**
     * 协议解析方法，将byte数据解析为字符串
     *
     * @param contents 内容
     * @return CassandraData
     */
    CassandraData parseProtocol(byte[] contents);
}
