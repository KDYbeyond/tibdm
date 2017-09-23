package com.thit.tibdm.db.bridge.stru;

/**
 * Created by rmbp13 on 16/9/20.
 */
public class CassandraV2Attr {
    /**
     * 属性名称
     */
    private String attrName;
    /**
     * 属性内容
     */
    private String attrValue;

    public CassandraV2Attr() {
    }

    public CassandraV2Attr(String attrName, String attrValue) {
        this.attrName = attrName;
        this.attrValue = attrValue;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    @Override
    public String toString() {
        return "Attr{" +
                "attrName='" + attrName + '\'' +
                ", attrValue='" + attrValue + '\'' +
                '}';
    }
}
