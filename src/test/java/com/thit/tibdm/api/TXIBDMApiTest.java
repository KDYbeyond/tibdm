package com.thit.tibdm.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.thit.tibdm.NettyTransmission.ProtocolAnalysis.WarningRules;
import com.thit.tibdm.util.HexUtilTest;
import com.xicrm.common.TXISystem;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by wanghaoqiang on 2017/5/26.
 */
public class TXIBDMApiTest {

    public final static Logger logger = LoggerFactory.getLogger(TXIBDMApiTest.class);

    @Before
    public void setUp() throws Exception {
        TXISystem.start();
    }

    @Test
    public void xiGetPeriodsData() throws Exception {
//        String queryJsonKeys1 = "{\"COLLECT_TYPE\":\"240\",\"MACHINE_ID\":\"330\""
//                + ",\"ATTR_NAME1\": \"AB\",\"ATTR_NAME2\": \"BBXX_TCMS_ZKDYBB1\"}";
//        logger.info(TXIBDMApi.XiGetPeriodsData("table0519", "240", "330", queryJsonKeys1, "1482212942790",
//                "1482212942792", "3", "1", "1", "2", null));
    }

    @Test
    public void xiAggregation() throws Exception {

    }

    @Test
    public void xiGetRealTimeData() throws Exception {

    }

    @Test
    public void xiGetRealTimeDataByKey() throws Exception {

    }

    @Test
    public void xiSave2Db() throws Exception {
        String s = JSON.toJSONString(toJsonString("199", "80"));
        System.out.println(s);
        try {
            TXIBDMApi.XiSave2Db("table0519", "4000", "240", s, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void xiGetRealTimes() throws Exception {

    }

    @Test
    public void xiSave2Dbs() throws Exception {
    }

    @Test
    public void xiGetMakeupData() throws Exception {

    }

    @Test
    public void xiDelMakeupData() throws Exception {

    }

    @Test
    public void xiDelMakeups() throws Exception {

    }

    @Test
    public void xiCalculation() throws Exception {

        List<String> ms = new ArrayList<>();
        ms.add("336");
        List<Object> keys = new ArrayList<>();
//        List<String> paras = new ArrayList<>();
//        paras.add("ZT108");
//        paras.add("ZT109");
//        paras.add("ZT110");
//        paras.add("ZT111");
//        paras.add("ZT112");
//        paras.add("ZT113");
//        Map<String,Object> map=new HashMap<>();
//        map.put("ZT1+ZT2+ZT3",paras);
//        keys.add(map);
        keys.add("ZT108");
        keys.add("ZT109");
        keys.add("ZT110");
        keys.add("ZT111");
        keys.add("ZT112");
        keys.add("ZT113");

        String shanghai = TXIBDMApi
                .XiGetRealTimeDataByKey("shanghai", JSON.toJSONString(ms), "240", JSON.toJSONString(keys), null);
        logger.info("===返回值信息" + shanghai);

    }

    @Test
    public void testJson() {
        DecimalFormat df = new DecimalFormat("#.00");
        Map<String, String> map = new HashMap<>();
        String json =
                "[{\"zt1+zt2+zt3\":[\"zt1\",\"zt2\",\"zt3\"]},{\"zt4+zt5+zt6\":[\"zt4\",\"zt5\",\"zt6\"]},\"zt7\",\"zt8\"]";
        JSONArray jsonArray = JSON.parseArray(json);
        jsonArray.forEach(j -> {
            if (j instanceof String) {
                map.put(j.toString(), j.toString() + 1);
            } else {
                Map<String, List<String>> para = (Map<String, List<String>>) j;
                Map<String, String> pa = new HashMap<>();
                para.forEach((k, v) -> {
                    logger.info("k==" + k);
                    logger.info("v==" + v);
                    v.forEach(p -> {
                        String s = Math.random() + "";
                        logger.info("随机的数为：" + s);
                        pa.put(p, s);
                    });
                    double v1 = WarningRules.convertToDouble(k, pa);
                    map.put(k, df.format(v1));
                });
            }
        });
        logger.info("最终的map为：" + map.toString());
    }

    @Test
    public void XiGetRealTimes() throws Exception {
        String shanghai = TXIBDMApi
                .XiGetRealTimes("shanghai","240", "[\"330-\",\"33-3\"]", null);
        logger.info("===返回值信息" + shanghai);
    }

    @Test
    public void XiGetRealTimeByLineSum() throws Exception {
        String shanghai = TXIBDMApi
                .XiGetRealTimeByLineSum("shanghai","240", "[\"l01\",\"l02\",\"l03\",\"l04\",\"l05\",\"l06\",\"l07\",\"l08\",\"l09\",\"l10\",\"l11\",\"l12\",\"l17\"]",
                                        "[{\"ZT201+ZT202+ZT203-ZT204\":[\"ZT201\",\"ZT202\",\"ZT203\",\"ZT204\"]},\"ZT200\"]",
                                        null);
        logger.info("===返回值信息" + shanghai);
    }

    @Test
    public void urlTest(){
        String str="ZT1%2BZT2%2BZT3";
        logger.info(URLDecoder.decode(str));
    }

    public Map toJsonString(String wdValue, String sdValue) {
        Map map = new HashMap();
        map.put("wd", wdValue);
        map.put("sd", sdValue);
        map.put("OBJECT_NAME", "collect_data_1220");
        map.put("MACHINE_ID", "33");
        map.put("COLLECT_TYPE", "GPS");
        map.put("COLLECT_TIME", "1477286503525");
//        map.put("COLLECT_TIME", System.currentTimeMillis()+"");
        map.put("RECEIVE_TIME", "123454543543");
        String s = JSON.toJSONString(map);
        return map;
    }

    @Test
    public void testXiUpdatePro() throws Exception {

        TXIBDMApi.XiUpdatePro("status1","pro017",null);

    }

    @Test
    public void testXiGetGroupPeriodsData() throws Exception {

        List<String> list=new ArrayList<>();
        list.add("ZT19");
        list.add("ZT18");
        String str=JSON.toJSONString(list);
        String result=TXIBDMApi.XiGetGroupPeriodsData("tablemachine_time", "240", "333", str, "2017/08/24 09:30:00", "2017/08/24 18:30:03", "-1", "1", "2", "2","100",null);
        System.out.println(result);

    }
}