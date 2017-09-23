package com.thit.tibdm.util;

import com.thit.tibdm.KafkaTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Created by wanghaoqiang on 2017/6/3.
 *
 */
public class CreateCqlTest {
    public final static Logger LOGGER = LoggerFactory.getLogger(CreateCqlTest.class);
    @Test
    public void getCQL() throws Exception {
        String cql = CreateCql.getCQL();
        LOGGER.info(cql);
    }


    @Test
    public void testGetKey() throws Exception {
        LOGGER.info(CreateCql.getKey());
    }
//    @Test
//    public void clearRedis(){
//        Jedis jedisConnect = RedisUtil.getJedisConnect();
//        String[] ids=new String[700];
//        for (int i = 0; i < 700; i++) {
//            if (i!=336){
//                ids[i]=i+"-240";
//            }else {
//                ids[i]="240";
//            }
//        }
//        jedisConnect.hdel("shanghai",ids);
//        jedisConnect.close();
//    }
//
//    @Test
//    public void update(){
//        Jedis jedisConnect = RedisUtil.getJedisConnect();
//        String shanghai = jedisConnect.hget("shanghai", "336-240");
//        Map<String,String> parse = (Map<String,String>)JSON.parse(shanghai);
//        LOGGER.info(parse.toString());
////        parse.put("ZT15","5");
////        parse.put("ZT16","6");
////        parse.put("ZT17","20");
////        String s = JSON.toJSONString(parse);
//////        jedisConnect.hset("shanghai","336-240",s);
////        LOGGER
//        jedisConnect.close();
//    }


}