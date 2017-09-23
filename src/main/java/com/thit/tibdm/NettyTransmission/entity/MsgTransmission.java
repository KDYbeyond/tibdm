package com.thit.tibdm.NettyTransmission.entity;


/**
 * Created by dongzhiquan on 2017/6/9.
 * @author wanghaoqiang
 */
public class MsgTransmission {

//    private byte[]  header;
//    private byte[]  type;
//    private byte[]   sessionId;
//    private byte[] bodyInfo;
//    private byte[] bodyMsg;
//    private byte[] check;
//    private byte[] tail;
//
//
//    public MsgTransmission() {
//    }
//
//
//
//    public byte[] getHeader() {
//        return header;
//    }
//
//    public void setHeader(byte[] header) {
//        this.header = header;
//    }
//
//    public byte[] getType() {
//        return type;
//    }
//
//    public void setType(byte[] type) {
//        this.type = type;
//    }
//
//    public byte[] getSessionId() {
//        return sessionId;
//    }
//
//    public void setSessionId(byte[] sessionId) {
//        this.sessionId = sessionId;
//    }
//
//    public byte[] getBodyInfo() {
//        return bodyInfo;
//    }
//
//    public void setBodyInfo(byte[] bodyInfo) {
//        this.bodyInfo = bodyInfo;
//    }
//
//    public byte[] getBodyMsg() {
//        return bodyMsg;
//    }
//
//    public void setBodyMsg(byte[] bodyMsg) {
//        this.bodyMsg = bodyMsg;
//    }
//
//    public byte[] getCheck() {
//        return check;
//    }
//
//    public void setCheck(byte[] check) {
//        this.check = check;
//    }
//
//    public byte[] getTail() {
//        return tail;
//    }
//
//    public void setTail(byte[] tail) {
//        this.tail = tail;
//    }
    /**
     * 头部
     */
    private int head;
    /**
     * 类型
     */
    private short type;
    /**
     * id
     */
    private long id;
    /**
     * 协议类型
     */
    private short proType;
    /**
     * 版本
     */
    private short version;
    /**
     * 长度
     */
    private long length;
    /**
     * 城市
     */
    private short city;
    /**
     * 连接数量
     */
    private short lineNum;
    /**
     * 编组ID
     */
    private int bianzuId;
    /**
     * 机器ID
     */
    private short machineId;
    /**
     * 年
     */
    private short year;
    /**
     * 月
     */
    private short month;
    /**
     * 日
     */
    private short day;
    /**
     * 时
     */
    private short hour;
    /**
     * 分钟
     */
    private short minu;
    /**
     * se
     */
    private short se;
    /**
     * 毫秒
     */
    private int ms;
    /**
     * 预留
     */
    private byte[] yuliu;
    /**
     * 消息体
     */
    private byte[] body;
    /**
     * 开始
     */
    private int crc;
    /**
     * 结束
     */
    private int end;

    public MsgTransmission(int head, short type, long id, short proType, short version, long length, short city, short lineNum, int bianzuId, short machineId, short year, short month, short day,
                           short hour, short minu, short se, int ms, byte[] yuliu, byte[] body, int crc, int end) {
        this.head = head;
        this.type = type;
        this.id = id;
        this.proType = proType;
        this.version = version;
        this.length = length;
        this.city = city;
        this.lineNum = lineNum;
        this.bianzuId = bianzuId;
        this.machineId = machineId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minu = minu;
        this.se = se;
        this.ms = ms;
        this.yuliu = yuliu;
        this.body = body;
        this.crc = crc;
        this.end = end;
    }

    public MsgTransmission() {
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getProType() {
        return proType;
    }

    public void setProType(short proType) {
        this.proType = proType;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public short getCity() {
        return city;
    }

    public void setCity(short city) {
        this.city = city;
    }

    public short getLineNum() {
        return lineNum;
    }

    public void setLineNum(short lineNum) {
        this.lineNum = lineNum;
    }

    public int getBianzuId() {
        return bianzuId;
    }

    public void setBianzuId(int bianzuId) {
        this.bianzuId = bianzuId;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public short getMonth() {
        return month;
    }

    public void setMonth(short month) {
        this.month = month;
    }

    public short getDay() {
        return day;
    }

    public void setDay(short day) {
        this.day = day;
    }

    public short getHour() {
        return hour;
    }

    public void setHour(short hour) {
        this.hour = hour;
    }

    public short getMinu() {
        return minu;
    }

    public void setMinu(short minu) {
        this.minu = minu;
    }

    public short getSe() {
        return se;
    }

    public void setSe(short se) {
        this.se = se;
    }

    public int getMs() {
        return ms;
    }

    public void setMs(int ms) {
        this.ms = ms;
    }

    public byte[] getYuliu() {
        return yuliu;
    }

    public void setYuliu(byte[] yuliu) {
        this.yuliu = yuliu;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getCrc() {
        return crc;
    }

    public void setCrc(int crc) {
        this.crc = crc;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }


    public short getMachineId() {
        return machineId;
    }

    public void setMachineId(short machineId) {
        this.machineId = machineId;
    }

    @Override
    public String toString() {
        return "MsgTransmission{" +
                "head=" + head +
                ", type=" + type +
                ", id=" + id +
                ", proType=" + proType +
                ", version=" + version +
                ", length=" + length +
                ", city=" + city +
                ", lineNum=" + lineNum +
                ", bianzuId=" + bianzuId +
                ", machineId=" + machineId +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minu=" + minu +
                ", se=" + se +
                ", ms=" + ms +
                ", yuliu=" + yuliu.length +
                ", body=" + body.length +
                ", crc=" + crc +
                ", end=" + end +
                '}';
    }
}
