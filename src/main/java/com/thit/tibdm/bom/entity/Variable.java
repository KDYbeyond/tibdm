package com.thit.tibdm.bom.entity;

import java.io.Serializable;

/**
 * 通信协议下的变量类
 * Created by dongzhiquan on 2017/4/7.
 */
public class Variable implements Serializable {
    /**
     * 类型
     */
    private String type;
    /**
     * 序号
     */
    private String serialNumber;
    /**
     * 中文名称
     */
    private String name;
    /**
     * 唯一代码
     */
    private String uniqueCode;
    /**
     * 位移
     */
    private String byteOffset;
    /**
     * 位长度
     */
    private String byteLength;//位长度
    /**
     * 偏移字节
     */
    private String bitOffset;
    /**
     * 字节长度
     */
    private String bitLength;
    /**
     * 最小
     */
    private String realMin;
    /**
     * 最大
     */
    private String realMax;
    /**
     * 是否注册
     */
    private String isSigned;
    /**
     * 会话
     */
    private String conversion;


    public Variable() {
    }

    public Variable(String type, String serialNumber, String name, String uniqueCode, String byteOffset, String byteLength, String bitOffset, String bitLength, String realMin, String realMax, String isSigned, String conversion) {
        this.type = type;
        this.serialNumber = serialNumber;
        this.name = name;
        this.uniqueCode = uniqueCode;
        this.byteOffset = byteOffset;
        this.byteLength = byteLength;
        this.bitOffset = bitOffset;
        this.bitLength = bitLength;
        this.realMin = realMin;
        this.realMax = realMax;
        this.isSigned = isSigned;
        this.conversion = conversion;
    }

    public String getConversion() {
        return conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(String byteOffset) {
        this.byteOffset = byteOffset;
    }

    public String getByteLength() {
        return byteLength;
    }

    public void setByteLength(String byteLength) {
        this.byteLength = byteLength;
    }

    public String getBitOffset() {
        return bitOffset;
    }

    public void setBitOffset(String bitOffset) {
        this.bitOffset = bitOffset;
    }

    public String getBitLength() {
        return bitLength;
    }

    public void setBitLength(String bitLength) {
        this.bitLength = bitLength;
    }

    public String getRealMin() {
        return realMin;
    }

    public void setRealMin(String realMin) {
        this.realMin = realMin;
    }

    public String getRealMax() {
        return realMax;
    }

    public void setRealMax(String realMax) {
        this.realMax = realMax;
    }

    public String getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(String isSigned) {
        this.isSigned = isSigned;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "type='" + type + '\'' +
                ", serialNumber=" + serialNumber +
                ", name='" + name + '\'' +
                ", uniqueCode='" + uniqueCode + '\'' +
                ", byteOffset='" + byteOffset + '\'' +
                ", byteLength='" + byteLength + '\'' +
                ", bitOffset='" + bitOffset + '\'' +
                ", bitLength='" + bitLength + '\'' +
                ", realMin='" + realMin + '\'' +
                ", realMax='" + realMax + '\'' +
                ", isSigned='" + isSigned + '\'' +
                ", conversion='" + conversion + '\'' +
                '}';
    }
}
