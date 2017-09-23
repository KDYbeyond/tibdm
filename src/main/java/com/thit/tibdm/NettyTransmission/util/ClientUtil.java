package com.thit.tibdm.NettyTransmission.util;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.bom.entity.Variable;
import com.thit.tibdm.util.ResourceUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dongzhiquan on 2017/2/10.
 */
public class ClientUtil {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ClientUtil.class);
    /**
     * 套接字
     */
    private static Socket socket;
    /**
     * 输入流
     */
    private static InputStream is = null;
    /**
     * 缓冲流
     */
    private static BufferedReader br = null;


    /**
     * 字符串转换为16进制字符串
     *
     * @param s 参数
     * @return 返回
     */
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 建立客户端连接
     *
     * @param address 地址
     * @param port    端口
     * @return 返回
     */

    public static Socket connect(String address, int port) {

        try {
            socket = new Socket(address, port);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return socket;
    }

    /**
     * 发送模拟数据
     */

    public static void sendMsg() {
        try {
            String ip = ResourceUtil.getProValueByNameAndKey("server", "server_ip");
            int port = Integer.parseInt(ResourceUtil.getProValueByNameAndKey("server", "server_port"));

            socket = new Socket("localhost", 8099);
            OutputStream os = socket.getOutputStream();
            String info = "";
            //默认发送28个字节

            int i = 0;
            //发送次数
            int num = Integer.parseInt(ResourceUtil.getProValueByNameAndKey("server", "send_times"));
            //里程初始值 在此基础上累加
            float liCheng = 100;
            //默认里程累计数小于255

            ExecutorService executore = Executors.newFixedThreadPool(2);
            while (i == 0) {

                executore.execute(() -> {
                    for (int j = 0; j <= 0; j++) {

                        byte[] bytes = null;
                        bytes = getBytes();
                        try {
                            os.write(bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Thread.sleep(2000);
                i++;
                System.out.println("发了" + i + "次了");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
//
//    public static void save2File(String msg, String filePath) {
//
//        File file = FileUtils.getFile(filePath);
//
//        try {
//            FileUtils.writeStringToFile(file, msg, "GBK", true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }

//    public static void readFromFile(String filePath) {
//
////        filePath="C:/Users/zc/Desktop/file.txt";
//        File file = FileUtils.getFile(filePath);
//
//        try {
//            LineIterator iterator = FileUtils.lineIterator(file, "GBK");
//            while (iterator.hasNext()) {
//                String line = iterator.nextLine() + "\r\n";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 根据车号拿协议名
     *
     * @param CH zifu
     * @return 字符串
     */
    public static String getProtocolName(String CH) {
        /**
         *
         */
        String chPro = ResourceUtil.getProValueByNameAndKey("rdb", "ch-pro");
        Map chProMap = (Map) JSON.parse(chPro);
        String protocolName = chProMap.get(CH).toString();
        return protocolName;
    }

    /**
     * @return 字节数
     */
    public static byte[] getBytes() {

        String protocalAll = ResourceUtil.getProValueByNameAndKey("rdb", "pro");
        Map mapALl = (Map) JSON.parse(protocalAll);
        //根据车号获取协议名(前期没有车号 随机取一个)
        String protocolName = getProtocolName("321");
        //根据协议名获取协议
        String myProtocol = mapALl.get(protocolName).toString();
        //转换为实体类
        MessageProtocol messageProtocol = JSON.parseObject(myProtocol, MessageProtocol.class);
        List<Variable> list = messageProtocol.getVariable();
        ByteBuf buf = Unpooled.buffer();
        //头11字节
        buf.writeInt(1);
        buf.writeInt(10);
        buf.writeShort(10);
        //长度2字节
        buf.writeInt(1422);
        buf.writeInt(10);
        buf.writeInt(10);
        buf.writeMedium(10);
        buf.writeShort(10);
        buf.writeInt(10);
        buf.writeInt(10);

        buf.writeInt(10);
        System.out.println(buf.readableBytes());

        //body
        byte[] bytes = new byte[1];

        for (Variable variable : list) {

            if (variable.getByteLength().equals("1")) {

                if (buf.readableBytes() <= (Integer.parseInt(variable.getByteOffset()) + 12)) {
                    buf.writeByte(getRandomNum(Integer.parseInt(variable.getRealMin()), Integer.parseInt(variable.getRealMax())));
                }
            } else if (variable.getByteLength().equals("2")) {
                buf.writeShort(getRandomNum(Integer.parseInt(variable.getRealMin()), Integer.parseInt(variable.getRealMax())));

            } else if (variable.getByteLength().equals("3")) {
                buf.writeMedium(getRandomNum(Integer.parseInt(variable.getRealMin()), Integer.parseInt(variable.getRealMax())));
            } else if (variable.getByteLength().equals("4")) {
                buf.writeInt(getRandomNum(Integer.parseInt(variable.getRealMin()), Integer.parseInt(variable.getRealMax())));
            }

        }

        System.out.println(buf.readableBytes());

        /**
         * 尾5字节
         */
        buf.writeInt(10);
        buf.writeByte(10);
        System.out.println(buf.readableBytes());
        byte[] bytes1 = new byte[1430];
        buf.readBytes(bytes1);
        return bytes1;
    }

    /**
     * @param num        数量
     * @param byteLength 字节长度
     * @return 常数
     */
    public static String get16Str(int num, String byteLength) {

        String re = "";
        String str = Integer.toHexString(num);
        String zero = "";
        int reLength = Integer.parseInt(byteLength) * 2;
        if (str.length() == reLength) {
            re = str;
        } else if (str.length() < reLength) {
            for (int i = 0; i < reLength - str.length(); i++) {
                zero += "0";
            }
            re = zero + str;
        }
//        logger.debug("十六进制："+re);
        return re;
    }


    /**
     * 获得一个给定范围的随机整数
     *
     * @param smallistNum 最小数
     * @param BiggestNum  最大数
     * @return 返回
     */
    public static int getRandomNum(int smallistNum, int BiggestNum) {
        Random random = new Random();
        return (Math.abs(random.nextInt()) % (BiggestNum - smallistNum + 1)) + smallistNum;
    }

    /**
     * @param min 最小
     * @param max 最大
     * @throws Exception 异常
     */
    public static void Random_long(long min, long max) throws Exception {


        long rangeLong = min + (((long) (new Random().nextDouble() * (max - min))));
        System.out.println(rangeLong);
    }


}


