package com.thit.tibdm.bom.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dongzhiquan on 2017/4/7.
 */
public class MessageProtocol implements Serializable {
    /**
     * 协议名称
     */
    private  String protocolName;
    /**
     * 消息类型
     */
    private  String messagetype;
    /**
     * 长度
     */
    private  int length;
    /**
     * 可见
     */
    private List<Variable> variable;
    /**
     * 警告协议
     */
    private List<WarningProtocolBean> WarningProtocol;

    public MessageProtocol(String protocolName, String messagetype, int length,
                           List<Variable> variable,
                           List<WarningProtocolBean> warningProtocol) {
        this.protocolName = protocolName;
        this.messagetype = messagetype;
        this.length = length;
        this.variable = variable;
        WarningProtocol = warningProtocol;
    }

    public MessageProtocol() {
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    public void setLength(int length) {
        this.length = length;
    }


    public String getProtocolName() {
        return protocolName;
    }

    public String getMessagetype() {
        return messagetype;
    }

    public int getLength() {
        return length;
    }

    public void setVariable(List<Variable> variable) {
        this.variable = variable;
    }

    public List<Variable> getVariable() {
        return variable;
    }

    public List<WarningProtocolBean> getWarningProtocol() {
        return WarningProtocol;
    }

    public void setWarningProtocol(List<WarningProtocolBean> warningProtocol) {
        WarningProtocol = warningProtocol;
    }

    @Override
    public String toString() {
        return "MessageProtocol{" +
               "protocolName='" + protocolName + '\'' +
               ", messagetype='" + messagetype + '\'' +
               ", length=" + length +
               ", variable=" + variable +
               ", WarningProtocol=" + WarningProtocol +
               '}';
    }
}
