package com.thit.tibdm.sparkstream.util;

import com.alibaba.fastjson.JSON;

import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.bom.entity.Variable;
import com.thit.tibdm.db.bridge.util.CassandraV2Table;
import com.thit.tibdm.util.ExceptionUtil;
import com.thit.tibdm.util.HexUtil;
import com.thit.tibdm.util.ProtocolConstants;
import org.apache.commons.jexl2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dongzhiquan on 2017/5/18.
 */
public class ParseUtil {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseUtil.class);


    /**
     * @param jexlExp jexlExp
     * @param map     集合
     * @return String
     */
    public static String doConvert(String jexlExp, Map<String, Object> map) {
        JexlEngine jexl = new JexlEngine();
        Expression e = jexl.createExpression(jexlExp);
        JexlContext jc = new MapContext();
        for (String key : map.keySet()) {
            jc.set(key, map.get(key));
        }
        return e.evaluate(jc).toString();
    }

    /**
     * @param bytes       字节数组
     * @param broProMap   消息协议
     * @param broProChMap 集合
     * @return String
     */
    public static String parseMsgTest(byte[] bytes, Map<String, MessageProtocol> broProMap, Map<String, String> broProChMap) {
        //test
        String ch = HexUtil.converByteToV(bytes, 0, 2, false) + "";
        String time = HexUtil.converByteToV(bytes, 2, 8, true) + "";
        /**
         * 根据车号获取协议号
         */
        String proId = broProChMap.get(ch);
//        System.out.println("车号为："+ch);
        MessageProtocol message = broProMap.get(proId);
        //获取多个变量
        List<Variable> list = message.getVariable();

        //存放属性
        Map map = new HashMap<>();
        Map map1 = new HashMap<>();

        //截取后的二进制字符串
        String ss = "";
        long value = 0L;

        for (Variable variable : list) {

            String byteOffset = variable.getByteOffset();
            int offset = Integer.parseInt(byteOffset) + 10;
            //排除预留字节 预留的字节位移为-1
            if (offset != -1) {
                //截取16进制字节

                //一个字节
                if (Integer.parseInt(variable.getByteLength()) == 1) {
                    if (variable.getIsSigned().equals("0")) {
                        value = HexUtil.getUnByte(bytes[offset]);

                    } else if (variable.getIsSigned().equals("1")) {
                        value = bytes[offset];
                    }
                    if (Integer.parseInt(variable.getBitLength()) == 8) {
                        ss = value + "";
                    } else {
                        //一个字节位
                        //一个字节
                        int a = Integer.parseInt(variable.getBitOffset());
                        int b = Integer.parseInt(variable.getBitOffset()) + Integer.parseInt(variable.getBitLength());
                        ss = HexUtil.getBitsByByte(bytes[offset], a, b);
                    }

                }
                //多个字节
                else if (Integer.parseInt(variable.getByteLength()) > 1) {
                    //无符号
                    if (variable.getIsSigned().equals("0")) {
                        if (variable.getByteLength().equals("2")) {
                            ss = HexUtil.converByteToV(bytes, offset, 2, false);
                        } else if (variable.getByteLength().equals("3")) {
                            ss = HexUtil.converByteToV(bytes, offset, 3, false);
                        } else if (variable.getByteLength().equals("4")) {
                            ss = HexUtil.converByteToV(bytes, offset, 4, false);
                        }
                    } else if (variable.getIsSigned().equals("1")) {
                        if (variable.getByteLength().equals("2")) {
                            ss = HexUtil.converByteToV(bytes, offset, 2, true);
                        } else if (variable.getByteLength().equals("3")) {
                            ss = HexUtil.converByteToV(bytes, offset, 3, true);
                        } else if (variable.getByteLength().equals("4")) {
                            ss = HexUtil.converByteToV(bytes, offset, 4, true);
                        }
                    }
                }

//                    //计算Conversion值
                if (!variable.getConversion().equals("x")) {
                    Map mapjexl = new HashMap<>();
                    String jexlExp = variable.getConversion();
                    mapjexl.put("x", ss);
                    ss = doConvert(jexlExp, mapjexl);
                    String format = String.format("%.2f", Double.parseDouble(ss));
                    ss = format;
                }
                map1.put(variable.getUniqueCode(), ss);
//
            }
        }

        map1.put(CassandraV2Table.machine_id, ch);//车号
        map1.put(CassandraV2Table.collect_type, "240");//待定
        map1.put(CassandraV2Table.collect_time, time + "");//待定
        map1.put(CassandraV2Table.receive_time, "1459423125123");
        map1.put("CH", ch);
        map.put("CH", ch);
        map.put(CassandraV2Table.machine_id, ch);//车号
        map.put(CassandraV2Table.collect_type, "240");//待定
        map.put(CassandraV2Table.json, map1);//属性
        map.put(CassandraV2Table.protocol, proId);//属性

        String json = JSON.toJSONString(map);
        return json;
    }

    /**
     * @param bytes       字节数组
     * @param broProMap   消息协议
     * @param broProChMap 集合
     * @return Map
     */
    public static Map parseMsgMap(byte[] bytes, Map<String, MessageProtocol> broProMap, Map<String, String> broProChMap) {
        //最终返回的数据
        Map map = new HashMap<>();
        String ch = HexUtil.converByteToV(bytes, 0, 2, false) + "";
        String time = HexUtil.converByteToV(bytes, 2, 8, true) + "";
        /**
         * 根据车号获取协议号
         */
        String proId = broProChMap.get(ch);
        if (proId == null || proId.equals("")) {
            return map;
        }
//        LOGGER.error("chehao=====");
        MessageProtocol message = broProMap.get(proId);
        //获取多个变量
        List<Variable> list = message.getVariable();
//        logger.debug("协议的内容：" + list.toString());

        //存放属性
        Map map1 = new HashMap<>();

        Integer ss_All = 0;
        Map<String, Object> map_ALL = new HashMap<>();
        //截取后的二进制字符串
        String ss = "";
        long value = 0L;

        for (Variable variable : list) {

            try {
                String byteOffset = variable.getByteOffset();
                int offset = Integer.parseInt(byteOffset) + 10;
                //排除预留字节 预留的字节位移为-1
                if (offset != -1) {
                    //截取16进制字节

                    //一个字节
                    if (Integer.parseInt(variable.getByteLength()) == 1) {
                        if (variable.getIsSigned().equals("N")) {
                            value = HexUtil.getUnByte(bytes[offset]);

                        } else if (variable.getIsSigned().equals("Y")) {
                            value = bytes[offset];
                        }
                        if (Integer.parseInt(variable.getBitLength()) == 8) {
                            ss = value + "";
                        } else {
                            //一个字节位
                            //一个字节
                            int a = Integer.parseInt(variable.getBitOffset());
                            int b = Integer.parseInt(variable.getBitOffset()) + Integer.parseInt(variable.getBitLength());
                            ss = HexUtil.getBitsByByte(bytes[offset], a, b);
                        }
                    }
                    //多个字节
                    else if (Integer.parseInt(variable.getByteLength()) > 1) {

                        //无符号
                        if (variable.getIsSigned().equals("N")) {
                            if (variable.getByteLength().equals("2")) {
                                ss = HexUtil.converByteToV(bytes, offset, 2, false);
                            } else if (variable.getByteLength().equals("3")) {
                                ss = HexUtil.converByteToV(bytes, offset, 3, false);
                            } else if (variable.getByteLength().equals("4")) {
                                ss = HexUtil.converByteToV(bytes, offset, 4, false);
                            }
                        } else if (variable.getIsSigned().equals("Y")) {
                            if (variable.getByteLength().equals("2")) {
                                ss = HexUtil.converByteToV(bytes, offset, 2, true);
                            } else if (variable.getByteLength().equals("3")) {
                                ss = HexUtil.converByteToV(bytes, offset, 3, true);
                            } else if (variable.getByteLength().equals("4")) {
                                ss = HexUtil.converByteToV(bytes, offset, 4, true);
                            }
                        }
                    }

//                try {
//                    if (variable.getType().equals(ProtocolConstants.TYPE_STATE)) {
//                        ss_All = Integer.parseInt(ss);
//                        map_ALL.put(variable.getUniqueCode(), ss_All);
//                    }
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }

//                    //计算Conversion值
                    if (!variable.getConversion().equals("x")) {
                        Map mapjexl = new HashMap<>();
                        String jexlExp = variable.getConversion();
                        mapjexl.put("x", ss);
                        ss = doConvert(jexlExp, mapjexl);
                        String format = String.format("%.2f", Double.parseDouble(ss));
                        ss = format;
                    }
                    map1.put(variable.getUniqueCode(), ss);
                }

            } catch (Exception e) {
                String s = JSON.toJSONString(variable);
                LOGGER.error("错误变量是 " + s);
                LOGGER.error("车号" + ch + "解析错误");
                LOGGER.error(ExceptionUtil.getMsgExce(e));
                map1.put(variable.getUniqueCode(), "-1");
                LOGGER.error("解析错误！");
            }
        }

//        map_ALL.put(CassandraV2Table.machine_id, ch);//车号
//        map_ALL.put(CassandraV2Table.collect_type, "240");//待定
//        map_ALL.put(CassandraV2Table.collect_time, time + "");//待定
//        map_ALL.put(CassandraV2Table.receive_time, "1459423125123");
//        map_ALL.put("CH", ch);
//        try {
//
//            String json_All=JSON.toJSONString(map_ALL);
//            ReduceUtil.save2BigDB(ResourceUtil.getProValueByNameAndKey("cassandra-db","table_all"),json_All);
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        map1.put(CassandraV2Table.machine_id, ch);//车号
        map1.put(CassandraV2Table.collect_type, "240");//待定
        map1.put(CassandraV2Table.collect_time, time + "");//待定
        map1.put(CassandraV2Table.receive_time, time + "");
        map1.put("CH", ch);
        map.put("CH", ch);
        map.put(CassandraV2Table.machine_id, ch);//车号
        map.put(CassandraV2Table.collect_type, "240");//待定
        map.put(CassandraV2Table.json, map1);//属性
        map.put(CassandraV2Table.protocol, proId);//属性

        return map;
    }


    /**
     * @param bytes       字节数组
     * @param broProMap   消息协议
     * @param broProChMap 集合
     * @return Map
     */
    public static String parseMsgAll(byte[] bytes, Map<String, MessageProtocol> broProMap, Map<String, String> broProChMap) {
        //最终返回的数据
        Map map = new HashMap<>();
        String ch = HexUtil.converByteToV(bytes, 0, 2, false) + "";
        String time = HexUtil.converByteToV(bytes, 2, 8, true) + "";
        /**
         * 根据车号获取协议号
         */
        String proId = broProChMap.get(ch);
        if (proId == null || proId.equals("")) {
            return "";
        }
        MessageProtocol message = broProMap.get(proId);
        //获取多个变量
        List<Variable> list = message.getVariable();
//        logger.debug("协议的内容：" + list.toString());

        //存放属性
        Map map1 = new HashMap<>();

        Integer ss_All = 0;
        Map<String, Object> map_ALL = new HashMap<>();
        //截取后的二进制字符串
        String ss = "";
        long value = 0L;

        for (Variable variable : list) {

            try {
                String byteOffset = variable.getByteOffset();
                int offset = Integer.parseInt(byteOffset) + 10;
                //排除预留字节 预留的字节位移为-1
                if (offset != -1) {
                    //截取16进制字节

                    //一个字节
                    if (Integer.parseInt(variable.getByteLength()) == 1) {
                        if (variable.getIsSigned().equals("N")) {
                            value = HexUtil.getUnByte(bytes[offset]);

                        } else if (variable.getIsSigned().equals("Y")) {
                            value = bytes[offset];
                        }
                        if (Integer.parseInt(variable.getBitLength()) == 8) {
                            ss = value + "";
                        } else {
                            //一个字节位
                            //一个字节
                            int a = Integer.parseInt(variable.getBitOffset());
                            int b = Integer.parseInt(variable.getBitOffset()) + Integer.parseInt(variable.getBitLength());
                            ss = HexUtil.getBitsByByte(bytes[offset], a, b);
                        }
                    }
                    //多个字节
                    else if (Integer.parseInt(variable.getByteLength()) > 1) {

                        //无符号
                        if (variable.getIsSigned().equals("N")) {
                            if (variable.getByteLength().equals("2")) {
                                ss = HexUtil.converByteToV(bytes, offset, 2, false);
                            } else if (variable.getByteLength().equals("3")) {
                                ss = HexUtil.converByteToV(bytes, offset, 3, false);
                            } else if (variable.getByteLength().equals("4")) {
                                ss = HexUtil.converByteToV(bytes, offset, 4, false);
                            }
                        } else if (variable.getIsSigned().equals("Y")) {
                            if (variable.getByteLength().equals("2")) {
                                ss = HexUtil.converByteToV(bytes, offset, 2, true);
                            } else if (variable.getByteLength().equals("3")) {
                                ss = HexUtil.converByteToV(bytes, offset, 3, true);
                            } else if (variable.getByteLength().equals("4")) {
                                ss = HexUtil.converByteToV(bytes, offset, 4, true);
                            }
                        }
                    }
                    if (variable.getType().equals(ProtocolConstants.TYPE_STATE)) {

                        map1.put(variable.getUniqueCode(), ss);
                    }

                }

            } catch (Exception e) {
                map1.put(variable.getUniqueCode(), "-1");
                LOGGER.error("解析错误！");
            }
        }


        map1.put(CassandraV2Table.machine_id, ch);//车号
        map1.put(CassandraV2Table.collect_type, "240");//待定
        map1.put(CassandraV2Table.collect_time, time + "");//待定
        map1.put(CassandraV2Table.receive_time, time + "");
        map1.put("CH", ch);
        map.put("CH", ch);
        map.put(CassandraV2Table.machine_id, ch);//车号
        map.put(CassandraV2Table.collect_type, "240");//待定
        map.put(CassandraV2Table.json, map1);//属性
        map.put(CassandraV2Table.protocol, proId);//属性

        return JSON.toJSONString(map1);
    }


}
