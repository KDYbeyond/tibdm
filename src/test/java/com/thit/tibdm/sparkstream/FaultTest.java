package com.thit.tibdm.sparkstream;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.bom.entity.Variable;
import com.thit.tibdm.mq.bridge.MqProducer;
import com.thit.tibdm.util.ResourceUtil;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

/**
 * Created by dongzhiquan on 2017/6/6.
 */
public class FaultTest {

    @Test
    public void testSetStatus() throws Exception {

        String protocalAll= ResourceUtil.getProValueByNameAndKey("rdb","pro");
        Map mapALl =(Map) JSON.parse(protocalAll);
        //根据车号获取协议名(前期没有车号 随机取一个)
        String protocolName="protocol3&4";
        //根据协议名获取协议
        String myProtocol=  mapALl.get(protocolName).toString();
        //转换为实体类
        MessageProtocol messageProtocol= JSON.parseObject(myProtocol,MessageProtocol.class);
        List<Variable> list=messageProtocol.getVariable();

        for (Variable variable:
             list) {

            if (variable.getType().equals("fault")){
                for (int i=300;i<350;i++) {
//                  Fault.setStatus(i+"", variable.getUniqueCode(),"0");
                }
            }

        }

    }

    @Test
    public void testGetStatus() throws Exception {

        String protocalAll= ResourceUtil.getProValueByNameAndKey("rdb","pro");
        Map mapALl =(Map) JSON.parse(protocalAll);
        //根据车号获取协议名(前期没有车号 随机取一个)
        String protocolName="protocol3&4";
        //根据协议名获取协议
        String myProtocol=  mapALl.get(protocolName).toString();
        //转换为实体类
        MessageProtocol messageProtocol= JSON.parseObject(myProtocol,MessageProtocol.class);
        List<Variable> list=messageProtocol.getVariable();

        for (Variable variable:
                list) {

            if (variable.getType().equals("fault")){
                for (int i=300;i<301;i++) {
                    System.out.println(variable.getUniqueCode());
                }
            }

        }


    }

    @Test
    public  void test(){

        String json=ResourceUtil.getProValueByNameAndKey("faultmsg","msg");
        System.out.println(json.length());
        ExecutorService executor= Executors.newFixedThreadPool(30);
        int num=1;
        int j=0;
        while (j<1){
            j++;
            for (int i = 0; i < num; i++) {
                //Send one message.
                executor.execute(()-> MqProducer.sendMsg(json.getBytes(),"bomparse4"));
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("执行了"+(j+1)+"次");
        }

    }


}