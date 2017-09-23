package com.thit.tibdm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * CRC数组处理工具类及数组合并
 */


public class CrcUtil {
    /**
     * 日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CrcUtil.class);


    /**
     * CRC-16/CCITT-FALSE x16+x12+x5+1 算法
     * <p>
     * info
     * Name:CRC-16/CCITT-FAI
     * Width:16
     * Poly:0x1021
     * Init:0xFFFF
     * RefIn:False
     * RefOut:False
     * XorOut:0x0000
     *
     * @param bytes 字节数组
     * @param length 长度
     * @return int 返回
     */
    public static int crc_16_CCITT_False(byte[] bytes, int length) {
        int crc = 0xffff; // initial value
        int polynomial = 0x1021; // poly value
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xffff;
        //输出String字样的16进制
        String strCrc = Integer.toHexString(crc).toUpperCase();
        LOGGER.info(strCrc);
        return crc;
    }

}