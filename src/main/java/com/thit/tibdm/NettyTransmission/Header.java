package com.thit.tibdm.NettyTransmission;

/**
 * Created by wanghaoqiang on 2017/3/13.
 *
 * @author wanghaoqiang
 */
public class Header {
    /**
     * 协议版本
     */
    private int version;
    /**
     * 协议版本
     */
    private int contentLength;
    /**
     * 服务名称
     */

    private String sessionId;

    public Header() {
    }

    public Header(int version, int contentLength, String sessionId) {
        this.version = version;
        this.contentLength = contentLength;
        this.sessionId = sessionId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "Header{" +
                "version=" + version +
                ", contentLength=" + contentLength +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
