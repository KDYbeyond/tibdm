package com.thit.tibdm.db.bridge.impl;

import com.alibaba.fastjson.JSON;
import com.datastax.driver.core.Session;
import com.thit.tibdm.db.bridge.util.CassandraSingleConnect;
import com.thit.tibdm.db.bridge.util.CassandraV2Util;
import com.xicrm.common.TXISystem;
import org.junit.Test;

import java.util.*;


/**
 *
 * Created by dongzhiquan on 2017/6/14.
 */
public class TXICassandraV2ImplTest {

    @Test
    public void getTime() throws Exception {
        System.out.println(CassandraV2Util.getTimeStamp("2017/06/26 10:00:00"));
        System.out.println(CassandraV2Util.getTimeStamp("2017/06/27 10:15:00"));
        System.out.println("now==" + System.currentTimeMillis());
        System.out.println(CassandraV2Util.getStrTime("1498459981039"));
        System.out.println(CassandraV2Util.getStrTime("1498529700000"));

    }

    @Test
    public void testXiGetMutiMachinePeriodsData() throws Exception {
        TXISystem.start();
        Session instance = CassandraSingleConnect.INSTANCE.getInstance();
        List list=new ArrayList();
        list.add("326");
        list.add("327");
        list.add("328");
        list.add("329");
        list.add("330");
        list.add("331");
        list.add("332");
        list.add("333");
        list.add("334");
        list.add("335");
        list.add("435");
        list.add("436");
        list.add("437");
        list.add("438");
        list.add("439");
        list.add("440");
        list.add("441");
        list.add("442");
        String s = JSON.toJSONString(list);
        TXICassandraV2Impl t2 = new TXICassandraV2Impl();
        long a = System.currentTimeMillis();
//        String result = t2.XiGetMutiMachinePeriodsData("tablemachine_time", "240", s, "[\"ZT14\"]", "2017/07/02 09:45:19", "2017/07/02 20:45:19", "3000", "1", "2", "2");
        String result = t2.XiGetMutiMachinePeriodsData("tablemachine_time", "240", s, "[\"ZT14\"]", "2017/07/03 09:45:19", "2017/07/03 20:45:19", "3000", "1", "2", "2");
        System.out.println(result);
        long b = System.currentTimeMillis();
        System.out.println("一共用了" + (b - a) + "ms");

    }

    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        String json = JSON.toJSONString(list);
        System.out.println(json);
        json = json.substring(1, json.length() - 1);
        System.out.println(json);

    }

    @Test
    public void testXiGetMutiMachineByLineSum() throws Exception {

        //1175 12:30  1035 11:30
//        1035 3.2s
        TXISystem.start();
        List<String> ms = new ArrayList<>();
        ms.add("333");
        ms.add("330");
        List<Object> keys = new ArrayList<>();
        List<String> paras = new ArrayList<>();
        paras.add("ZT1");
        paras.add("ZT80");
        paras.add("ZT50");
        Map<String, Object> map = new HashMap<>();
        map.put("ZT1+ZT80+ZT50", paras);
//        keys.add(map);
        keys.add(map);
        keys.add("ZT80");
        keys.add("ZT50");
        keys.add("ZT1");
        List list = new ArrayList<>();
        list.add("336");
        list.add("l03");
        list.add("l04");
        list.add("l03");
        list.add("l05");
        list.add("l06");
        list.add("l07");
        list.add("l08");
        list.add("l17");
        String json = JSON.toJSONString(keys);
        System.out.println("json" + json);
        String line = JSON.toJSONString(list);
        TXICassandraV2Impl t2 = new TXICassandraV2Impl();
        long a = System.currentTimeMillis();
//        String result=t2.XiGetMutiMachineByLineSum("table1min","240",line,json,"1497821400000","1497966600000","3","1","1","2");
        String result = t2.XiGetMutiMachineByLineSum("tableline_notime", "240", "[\"l17\"]", "[\"ZT1\"]", "2017/07/02 05:30:00", "2017/07/02 13:30:03", "3000", "1", "2", "2");
//        String result=t2.XiGetMutiMachineByLineSum("table1min","240",line,json,"2017/06/18 05:30:00","2017/06/18 05:30:00","700","1","2","2");
        System.out.println(result);
        long b = System.currentTimeMillis();
        System.out.println("一共用了" + (b - a) + "ms");
    }


    //    @Test
    public String getAttr() {
        List<Object> keys = new ArrayList<>();
        List<String> paras = new ArrayList<>();
        paras.add("ZT1");
        paras.add("ZT2");
        paras.add("ZT50");
        Map<String, Object> map = new HashMap<>();
        map.put("ZT1+ZT2+ZT50", paras);
        keys.add(map);
        keys.add("ZT30");
        keys.add("ZT20");
        keys.add("ZT50");
        System.out.println(keys);
        return JSON.toJSONString(keys);
    }

    @Test
    public void testXiGetPeriodsData() throws Exception {

        String str = getAttr();
        TXICassandraV2Impl t2 = new TXICassandraV2Impl();
        String result = t2.XiGetPeriodsData("tablemachine_time", "240", "330", str, "2017/06/28 05:30:00", "2017/06/28 11:30:03", "3000", "1", "2", "2");
        System.out.println(result);
    }
}