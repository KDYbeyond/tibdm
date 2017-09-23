//package com.thit.tibdm.bom.util;
//
//import com.alibaba.fastjson.JSON;
//import com.thit.tibdm.bom.entity.MessageProtocol;
//import com.thit.tibdm.bom.entity.Variable;
//import com.thit.tibdm.db.bridge.stru.CassandraV2CollectInfo;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by dongzhiquan on 2017/4/7.
// */
//public class ParseProtocol {
//
//    /**
//     * 入库协议解析
//     * @param message
//     * @param protocol
//     * @return
//     */
//    public static CassandraV2CollectInfo parse(byte[] message,String protocol){
//
//        String msg=new String(message);
//        CassandraV2CollectInfo info=new CassandraV2CollectInfo();
//        //获取协议
//        MessageProtocol messageProtocol= JSON.parseObject(protocol,MessageProtocol.class);
//        List<Variable> list=messageProtocol.getVariable();
//        Map map=new HashMap<>();
//        String traincode="";
//
//        for (int i=0;i<list.size();i++){
//            Variable variable=list.get(i);
//            if (variable.getSerialNumber()!=0) {
//                map.put(variable.getUniqueCode(),Integer.parseInt(msg.substring(variable.getBitOffset(),variable.getBitLength()),10));
//            }else {
//                traincode=Integer.parseInt(msg.substring(variable.getBitOffset(),variable.getBitLength()),10)+"";
//            }
//        }
//
//
//        info.setAttr(map);
//        info.setTrainCode(traincode);
//        info.setMachineId("");//待定
//        info.setCollectTime(0);//待定
//        info.setCollectType("");//待定
//
//     return info;
//    }
//}
