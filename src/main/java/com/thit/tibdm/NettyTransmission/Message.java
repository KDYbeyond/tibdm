package com.thit.tibdm.NettyTransmission;

/**
 * 消息的主体
 * @author wanghaoqiang
 */
public class Message {

    /**
     * 消息头实体
     */
    private Header header;
    /**
     * 内容
     */
    private byte[] content;

    public Message() {
    }

    public Message(Header header, byte[] content) {
        this.header = header;
        this.content = content;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "header=" + header +
                ", content=" + new String(content) +
                '}';
    }
}