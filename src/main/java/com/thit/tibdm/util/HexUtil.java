package com.thit.tibdm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 进制转换工具类
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-04-12 09:53
 **/
public class HexUtil {
    /**
     * 日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(HexUtil.class);

    /**
     * 将一个字符串16进制数字转成16进制的byte
     *
     * @param num 参数
     * @return byte 数组
     */
    public static byte hex16ToByte(String num) {
        byte b = (byte) Integer.parseInt(num, 16);
        return b;
    }


    /**
     * byte转int
     *
     * @param b 字节
     * @return 字数
     */
    public static int byteToInt(byte b) {

        return b & 0xff;
    }

    /**
     * 将byte转为2进制
     *
     * @param b 字节
     * @return String
     */
    public static String byteTo2(byte b) {
        String binary = Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
        return binary;
    }


    /**
     * 字节数组转16进制字符串
     *
     * @param b 字节数组
     * @return 返回
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }

        return r;
    }


    /**
     * 字符转换为字节
     *
     * @param c 字符
     * @return 字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 16进制字符串转字节数组
     *
     * @param hex 字符
     * @return 字节数组
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }

    }


    /**
     * int转八位二进制
     *
     * @param num 八进制
     * @return 二进制
     */
    public static String hexIntTo8Binary(int num) {
        String binaryString = Integer.toBinaryString(num);//1111
        int binaryInt = Integer.parseInt(binaryString);//1111
        String re = String.format("%08d", binaryInt);
        return re;
    }


    /**
     * 获取无符号的一个字节
     *
     * @param b 字节
     * @return 无符号
     */
    public static int getUnByte(byte b) {
        return b & 0xff;
    }

    /**
     * 获取有符号的一个字节
     *
     * @param b 字节
     * @return 无符号
     */
    private int getByte(byte b) {
        return b;
    }


    /**
     * 把byte转为字符串的bit
     *
     * @param b 字节
     * @return 比特
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);

    }

    /**
     * 根据索引获取比特位
     *
     * @param b     字节
     * @param index 索引
     * @return 比特
     */
    public static String getBitByByte(byte b, int index) {
        return ""
                + (byte) ((b >> (7 - index)) & 0x1);
    }

    /**
     * 获取字节里面的多个比特位组成的数字
     *
     * @param b     字节
     * @param start 开始位置
     * @param end   结束位置
     * @return 比特
     */
    public static String getBitsByByte(byte b, int start, int end) {
        return Integer.valueOf(byteToBit(b).substring(start, end), 2).toString();
    }

    /**
     * 将二进制字符串转为无符号的整数
     * 在使用时候需要将
     *
     * @param binary 二进制
     * @return 无符号
     */
    public static String getTwnBina(String binary) {
        long i = Long.parseLong(binary, 2);
        return i + "";

//        Integer.reverseBytes()
    }


    /**
     * 有符号
     *
     * @param b1 字节
     * @param b2 字节
     * @return int
     */
    public static int convertTwoBytesToInt(byte b1, byte b2) {
        return (b2 << 8) | (b1 & 0xFF);
    }

    /**
     * @param b1 字节
     * @param b2 字节
     * @param b3 字节
     * @return 整数
     */
    public static int convertThreeBytesToInt(byte b1, byte b2, byte b3) {
        return (b3 << 16) | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    /**
     * @param b1 字节
     * @param b2 字节
     * @param b3 字节
     * @param b4 字节
     * @return 整数
     */
    public static int convertFourBytesToInt(byte b1, byte b2, byte b3, byte b4) {
        return (b4 << 24) | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }


    /**
     * 无符号
     *
     * @param b1 字节
     * @param b2 字节
     * @return 整数
     */
    public static int convertTwoBytesToIntUn(byte b1, byte b2) {
        return (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    /**
     * @param b1 字节
     * @param b2 字节
     * @param b3 字节
     * @return 整数
     */
    public static int convertThreeBytesToIntUn(byte b1, byte b2, byte b3) {
        return (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    /**
     * @param b1 字节
     * @param b2 字节
     * @param b3 字节
     * @param b4 字节
     * @return long
     */
    public static long convertFourBytesToIntUn(byte b1, byte b2, byte b3, byte b4) {
        return (long) (b4 & 0xFF) << 24 | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }


    /**
     * 根据bytes来获取值，可以传入索引，长度,有无符号
     *
     * @param bytes  字节数组
     * @param index  索引
     * @param length 长度
     * @param isSign 有无符号
     * @return 字符串
     */
    public static String converByteToV(byte[] bytes, int index, int length, boolean isSign) {
        String result = "";
        //有符号
        if (isSign) {
            //两个字节，三个字节，四个字节
            if (length == 2) {
                result = convertTwoBytesToInt(bytes[index + 1], bytes[index + 0]) + "";
            } else if (length == 3) {
                result = convertThreeBytesToInt(bytes[index + 2], bytes[index + 1], bytes[index]) + "";
            } else if (length == 4) {
                result = convertFourBytesToInt(bytes[index + 3], bytes[index + 2], bytes[index + 1], bytes[index]) + "";
            } else if (length == 8) {
                result = bytes2Long(bytes, index) + "";
            }
        } else {
            //无符号
            //两个字节，三个字节，四个字节
            if (length == 2) {
                result = convertTwoBytesToIntUn(bytes[index + 1], bytes[index]) + "";
            } else if (length == 3) {
                result = convertThreeBytesToIntUn(bytes[index + 2], bytes[index + 1], bytes[index]) + "";
            } else if (length == 4) {
                result = convertFourBytesToIntUn(bytes[index + 3], bytes[index + 2], bytes[index + 1], bytes[index]) + "";
            }
        }
        return result;
    }

    /**
     * @param byteNum 字节数组
     * @param index   索引
     * @return long
     */
    public static long bytes2Long(byte[] byteNum, int index) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix + index] & 0xff);
        }
        return num;
    }

}