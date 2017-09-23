//package com.thit.tibdm.NettyTransmission.util;
//
//import com.alibaba.fastjson.JSON;
//import com.thit.tibdm.bom.entity.MessageProtocol;
//import com.thit.tibdm.bom.entity.Variable;
//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;
//import jxl.read.biff.BiffException;
//import org.apache.commons.io.FileUtils;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by dongzhiquan on 2017/4/11.
// */
//public class DoExcel {
//
//
//    public static void main(String[] args) {
//        String json = getJsonProtocol();
//        MessageProtocol messageProtocol = JSON.parseObject(json, MessageProtocol.class);
//        System.out.println(messageProtocol.getVariable().get(2).getUniqueCode());
//        System.out.println(json);
//
////        getMap();
//    }
//
//    /**
//     * 暂时未用到
//     * @return
//     */
//    public static String getJsonProtocol() {
//
////            String fileName=new File("").getAbsolutePath()+File.separator+"jsonv2.txt";
//        String fileName = DoExcel.class.getClassLoader().getResource("pro.json").getPath();
//
//        File file = new File(fileName);
//        String json = "";
//        try {
//            json = FileUtils.readFileToString(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return json;
//    }
//
//    public static String getJsonProtocol1() {
//        Workbook readwb = null;
//        String json = "";
//
//
//        try {
//
//            //构建Workbook对象, 只读Workbook对象
//            //直接从本地文件创建Workbook
//            InputStream instream = new FileInputStream("C:/Users/zc/Desktop/新建文件夹/上海无线传输系统数据解析文档V10_20160812.xls");
//            readwb = Workbook.getWorkbook(instream);
//
//            //Sheet的下标是从0开始
//            Sheet readsheet = readwb.getSheet(1);
//
//            MessageProtocol messageProtocol = new MessageProtocol();
//
//            List<Variable> list = new ArrayList<>();
//            for (int i = 1; i < 4000; i++) {
//                Variable variable = new Variable();
//                Variable variable1 = new Variable();
//                //变量名
//                Cell column1 = readsheet.getCell(1, i);
//                //中文描述
//                Cell column2 = readsheet.getCell(2, i);
//                //数据类型
//                Cell column3 = readsheet.getCell(3, i);
//                //字节偏移
//                Cell column7 = readsheet.getCell(7, i);
//                //比特偏移
//                Cell column8 = readsheet.getCell(8, i);
//                variable1 = getbytebitLength(column3.getContents());
//
//                //处理预留字节
//                if (!column3.getContents().isEmpty() && column3.getContents() != null) {
//                    variable.setByteOffset(Integer.parseInt(column7.getContents()));
//                    variable.setBitOffset(Integer.parseInt(column8.getContents()));
//                } else {
//                    variable.setByteOffset(-1);
//                    variable.setBitOffset(-1);
//                }
//
//                variable.setUniqueCode(column1.getContents());
//                variable.setName(column2.getContents());
//                variable.setSerialNumber(i - 1);
//                variable.setByteLength(variable1.getByteLength());
//                variable.setBitLength(variable1.getBitLength());
//                list.add(variable);
//
//            }
//            messageProtocol.setProtocolName("TestProtocol--state");
//            messageProtocol.setMessagetype("state");
//            messageProtocol.setLength(896);
//            messageProtocol.setVariable(list);
//            json = JSON.toJSONString(messageProtocol);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (BiffException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return json;
//    }
//
//
//    /**
//     * 获取字节和位长度等
//     *
//     * @param type
//     *
//     * @return
//     */
//
//    public static Variable getbytebitLength(String type) {
//        int length = 0;
//        Variable variable = new Variable();
//        if (type.equals("Unsigned16")) {
//            variable.setByteLength(2);
//            variable.setBitLength(16);
//        }
//        if (type.equals("BOOLEAN1")) {
//            variable.setByteLength(1);
//            variable.setBitLength(1);
//        }
//        if (type.equals("Unsigned8")) {
//            variable.setByteLength(1);
//            variable.setBitLength(8);
//        }
//        if (type.equals("INTEGER16")) {
//            variable.setByteLength(2);
//            variable.setBitLength(16);
//        }
//        if (type.isEmpty() || type == null) {
//            variable.setByteLength(0);
//            variable.setBitLength(0);
//            variable.setName("预留");
//        }
//        if (type.equals("ENUM4")) {
//
//            variable.setByteLength(1);
//            variable.setBitLength(4);
//        }
//        if (type.equals("ANTIVALENT2")) {
//
//            variable.setByteLength(1);
//            variable.setBitLength(2);
//        }
//
//        return variable;
//
//    }
//}
