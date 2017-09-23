package com.thit.tibdm.NettyTransmission.util;

/**
 * Created by wanghaoqiang on 2016/10/26.
 */
public class Constants {
    /**
     * 日志
     */
    public static final String LOGBACK = "logback.xml";
    /**
     * 服务端
     */
    public static final String SERVER = "server";
    /**
     * 服务端IP
     */
    public static final String SERVER_IP = "server_ip";
    /**
     * 服务端口
     */
    public static final String SERVER_PORT = "server_port";
    /**
     * 文件路径
     */
    public static final String FILE_PATH = "file_path";
    /**
     * 文件延伸
     */
    public static final String FILE_EXTENSION = "file_extension";
    /**
     * 超时
     */
    public static final int TIME_OUT = 1000 * 5 * 60;


    /**
     * 协议头长度 0xAA 0xAB 0xAC 消息类型 大概长度在12左右
     */
    public static final int HEADER_LEN = 3;
    /**
     * 消息长度
     */
    public static final int CYC_LEN = 2;
    /**
     * 结束长度
     */
    public static final int END_LEN = 3;
}
