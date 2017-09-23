package com.thit.tibdm.sparkstream;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.NettyTransmission.util.ResourceUtil;
import com.thit.tibdm.db.bridge.util.CassandraSingleConnect;
import com.thit.tibdm.mq.bridge.MqProducer;
import com.thit.tibdm.util.CreateCql;
import com.xicrm.common.TXISystem;
import kafka.producer.KeyedMessage;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

/**
 * Created by dongzhiquan on 2017/6/7.
 */
public class ReduceFrequencyTest {

    @org.junit.Test
    public void testSaveToCassandra() throws Exception {
//        TXISystem.start();
//        ReduceFrequency.saveToCassandra();
        //json字符串转java对象
//        ReduceFrequency.s();




    }

    @org.junit.Test
    public void testSendMsg() throws Exception {
//        TXISystem.start();
//        ReduceFrequency.saveToCassandra();
            ExecutorService executor= Executors.newFixedThreadPool(100);
        int num=1;
        for (int j = 0; j <1; j++) {

            for (int i = 0; i < num; i++) {
                //Send one message.
                String msg="";
                executor.execute(()-> MqProducer.sendMsg(msg,"devsave"));
            }

            Thread.sleep(500);
            System.out.println("执行了"+(j+1)+"次");
        }

    }

    @Test
   public void createTable(){
        String cql= ResourceUtil.getProValueByNameAndKey("cql","cqlline");
//        String cql= CreateCql.getCQL();
        System.out.println(cql);
//        CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
   }

    @Test
    public void testInsert(){


        Map map=new HashMap<>();
        map.put("machine_id","3");
        map.put("collect_time","4");
        map.put("collect_type","3");
        map.put("zt1","1");
        map.put("zt3","1");
        map.put("zt2","8");
        String json=JSON.toJSONString(map);
//        ReduceFrequency.save2BigDB("test0619",json);
    }

    @Test
    public void getList(){
        List<String> list=new ArrayList<>();
        for (int i = 1; i <= 17; i++) {
            if (i<10){

                list.add("l0"+i);
            }else {

                list.add("l"+i);
            }
        }
        String lineList="[\"l01\",\"l02\",\"l03\",\"l04\",\"l05\",\"l06\",\"l07\",\"l08\",\"l09\",\"l10\",\"l11\",\"l12\",\"l13\",\"l14\",\"l15\",\"l16\",\"l17\"]";

        System.out.println(JSON.toJSONString(list));
        System.out.println(lineList);

    }






}