package com.thit.tibdm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thit.tibdm.NettyTransmission.ProtocolAnalysis.WarningRules;
import com.thit.tibdm.NettyTransmission.util.ClientUtil;
import com.thit.tibdm.NettyTransmission.util.DoSingleExcel;
import com.thit.tibdm.api.TXIBDMApi;
import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.bom.entity.Variable;
import com.thit.tibdm.imdb.bridge.util.RedisUtil;
import com.thit.tibdm.sparkstream.util.ParseUtil;
import com.typesafe.config.ConfigException.Parse;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 进制测试类
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-04-12 09:53
 **/
public class HexUtilTest {
    public final static Logger logger = LoggerFactory.getLogger(HexUtilTest.class);
    @Test
    public void hex16ToByte() throws Exception {
        byte b = HexUtil.hex16ToByte("0a");
        logger.info(b+"");
        assertEquals(b,(byte) 10);
    }

    @Before
    public void start(){
        Config.init();
    }

    @Test
    public void byteTo2() throws Exception {
        byte b = HexUtil.hex16ToByte("0a");
        String s = HexUtil.byteTo2(b);
        logger.info(s);
        assertEquals(s,"00001010");
    }

    @Test
    public void testByte(){
        int i = 255;
        byte b= (byte) i;
        int a= b & 0xff;
        System.out.println(a);
    }

    @Test
    public void testString() throws IOException {
        byte[] bytes = {100, 100, 100, 100, 104, 69, -33, 44, 27, -98, 0, 103};
        String s = bytesToHexString(bytes);
        System.out.println(s);
        byte[] bytes1 = hexStringToByte(s);
        System.out.println(Arrays.toString(bytes1));
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


    @Test
    public void testRedis(){
//        Jedis jedis = RedisUtil.getJedisConnect();
//        String lastWarn = jedis.hget("shanghai-warn", "9012" + "-329");
//        System.out.println(lastWarn);
    }

    @Test
    public void testParse(){
        Map<String,JSONObject> messageProtocol = DoSingleExcel.INSTANCE.getInstance();
        String rdb = ResourceUtil.getProValueByNameAndKey("rdb", "ch-pro");
        Map<String,String> parse = (Map<String,String>)JSON.parse(rdb);
//        String msg = ClientUtil.getMsg("329");
//        logger.error("msg"+msg);
//        String s = ParseUtil.parseMsg("329-"+msg, messageProtocol, parse);
//        logger.error(s);
    }

    /**
     *
     */
    @Test
    public void test(){
        String str="{\"ZT22\":\"33\",\"ZT23\":\"15\",\"ZT20\":\"9\",\"ZT21\":\"2\",\"ZT24\":\"22\",\"ZT19\":\"10\"}";
        Map parse = (Map) JSON.parse(str);
        boolean b = WarningRules
                .convertToCode("ZT19*0.1>0.1 && ZT20*0.1>0.1",
                               parse,"");

        logger.error("运算结果："+b);
    }

    @Test
    public void testUnsign(){
        ByteBuf buf= Unpooled.buffer();

        buf.writeByte(200);
        buf.writeShort(40000);
//        short aShort = buf.getShort(1);
//        int unsignedShort = buf.getUnsignedShort(1);
//        short unsignedByte = buf.getUnsignedByte(0);
//        logger.debug(unsignedByte+"");
//        logger.debug(aShort+"");
//        logger.debug(unsignedShort+"");

        byte[]  aByte = null;
        try {
            aByte[0]=buf.getByte(10);

        }catch (Exception e){

            System.out.println("sssss");
        }

    }

//    @Test
//    public void test1(){
//        byte[] bytes=ClientUtil.getBytes();
//        System.out.println(bytes.length);
//
//
//    }

    @Test
    public void testConfig(){
        com.typesafe.config.Config conf = ConfigFactory.load();
        ConfigList list = conf.getList("tibdm.kafka.kafka_ip");
        list.unwrapped().forEach(l-> logger.info(l.toString()));
        ConfigObject object = conf.getObject("tibdm.rdm.pro");
        logger.info(object.unwrapped().toString());
    }

    @Test
    public void testLong(){
        ByteBuf buf= Unpooled.buffer();
        buf.writeLong(1496997769606L);
        long l = buf.readLong();
        logger.info("转换后的："+l);
    }

    @Test
    public void hexIntTo8Binary(){
        String s = HexUtil.hexIntTo8Binary(100);
        logger.info(s);
    }


    @Test
    public void getBit(){
        String s = HexUtil.byteToBit((byte) 200);
        String bitByByte = HexUtil.getBitByByte((byte) 200, 1);
        logger.info("之前的二进制数："+s);
        logger.info("根据索引获取的二进制数："+bitByByte);

        String twnBina = HexUtil.getTwnBina(s);
        logger.info("无符号数"+twnBina);

        long l = System.currentTimeMillis();
        logger.info(l+"");
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeByte(10);
        byteBuf.writeLong(l);

        logger.info(byteBuf.getLong(1)+"");
        int i = byteBuf.readableBytes();
        byte[] bytes = new byte[i];
        byteBuf.readBytes(bytes);

        long l1 = HexUtil.bytes2Long(bytes,1);
        String s1 = HexUtil.converByteToV(bytes, 1, 8, true);
        String s2 = HexUtil.converByteToV(bytes, 1, 4, true);
        String s3 = HexUtil.converByteToV(bytes, 1, 3, true);
        String s4 = HexUtil.converByteToV(bytes, 1, 2, true);
        String n1 = HexUtil.converByteToV(bytes, 1, 8, false);
        String n2 = HexUtil.converByteToV(bytes, 1, 4, false);
        String n3 = HexUtil.converByteToV(bytes, 1, 3, false);
        String n4 = HexUtil.converByteToV(bytes, 1, 2, false);
        logger.info(s1);
        logger.info(s2);
        logger.info(s3);
        logger.info(s4);
        logger.info(n1);
        logger.info(n2);
        logger.info(n3);
        logger.info(n4);
        logger.info(l1+"");
    }




}