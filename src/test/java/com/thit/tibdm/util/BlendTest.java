package com.thit.tibdm.util;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.arithmetic.TXIFormatConvert;
import com.thit.tibdm.calculation.util.SingleConnectOral;
import com.thit.tibdm.sparkstream.util.ParseUtil;
import com.xicrm.common.TXISystem;
import com.xicrm.exception.TXIException;
import com.xicrm.model.TXIModel;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 混合测试
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-06-05 15:22
 **/
public class BlendTest {
    public final static Logger LOGGER = LoggerFactory.getLogger(BlendTest.class);

    @Before
    public void start(){
        TXISystem.start();
    }

    @Test
    public void testCsv() throws Exception {
//        String filePath = "/Users/rmbp13/Desktop/data_import.csv";
//        CSVFileUtil csvFileUtil = new CSVFileUtil(filePath);
//        String s = csvFileUtil.readLine();
//        ArrayList keylist = CSVFileUtil.fromCSVLinetoArray(s);
//        LOGGER.info(keylist.toString());
    }

    @Test
    public void testConnect() throws SQLException {
        Connection connection1 = SingleConnectOral.INSTANCE.getInstance();
        Connection connection2 = SingleConnectOral.INSTANCE.getInstance();
        if (connection1==connection2){
            LOGGER.info("两个连接是否相等");
        }

        connection1.close();
        connection2.close();
    }

    @Test
    public void testSparkW() throws InterruptedException {
//        CassandraSQL.main(new String[]{});
    }
    @Test
    public void testSparkX() throws InterruptedException {
//        SparkSqlParquet.main(new String[]{});
    }

    @Test
    public void testJexl(){
        String jexlExp="x*0.1";
        Map map=new HashMap<>();
        map.put("x","-20");
        System.out.println(ParseUtil.doConvert(jexlExp,map));
    }

    @Test
    public void testHex(){
//        String sa="0a121";
//
//        byte[] bytes= BomClientUtil.byte16(sa);
//        String s="";
//        String ss="";
//        for (int i=0;i<bytes.length;i++){
//            s=HexUtil.byteTo2(bytes[i]);
//            ss=ss+s;
//        }
//        System.out.println(ss);
    }

    @Test
    public void testPer() throws TXIException {
        TXIModel model = new TXIModel(null);
        TXIFormatConvert test = new TXIFormatConvert();
        String query_json_keys_str = "{\"query_attrs\":["
                + "{\"TAGNAME\":\"F1\",\"GROUP1\":\"A电压谐波频谱\",\"GROUP2\":\"1\"},"
                + "{\"TAGNAME\":\"F2\",\"GROUP1\":\"A电压谐波频谱\",\"GROUP2\":\"2\"},"
                + "{\"TAGNAME\":\"F3\",\"GROUP1\":\"A电压谐波频谱\",\"GROUP2\":\"3\"},"
                + "{\"TAGNAME\":\"F4\",\"GROUP1\":\"B电压谐波频谱\",\"GROUP2\":\"1\"},"
                + "{\"TAGNAME\":\"F5\",\"GROUP1\":\"B电压谐波频谱\",\"GROUP2\":\"2\"},"
                + "{\"TAGNAME\":\"F6\",\"GROUP1\":\"B电压谐波频谱\",\"GROUP2\":\"3\"},"
                + "{\"TAGNAME\":\"F7\",\"GROUP1\":\"C电压谐波频谱\",\"GROUP2\":\"1\"},"
                + "{\"TAGNAME\":\"F8\",\"GROUP1\":\"C电压谐波频谱\",\"GROUP2\":\"2\"},"
                + "{\"TAGNAME\":\"F9\",\"GROUP1\":\"C电压谐波频谱\",\"GROUP2\":\"3\"}"
                + "]}";
        String tempdata_json_str="{F1:\"1\",F2:\"2\",F3:\"3\",F4:\"4\",F5:\"5\",F6:\"6\",F7:\"7\",F8:\"8\",F9:\"9\"}";
        String out=test.XiFormatConvert(query_json_keys_str, tempdata_json_str, model);
        System.out.println("out="+out);
    }


    @Test
    public void testAddZero(){
        String addZero = DateUtil.getAddZero(9);
        LOGGER.info(addZero);
    }

    @Test
    public void createMap(){
        String rdb = ResourceUtil.getProValueByNameAndKey("rdb", "ch-pro-t");
        List<Map<String, String>> parse = (List<Map<String, String>>) JSON.parse(rdb);
        Map<String,String> result=new HashMap<>();
        parse.forEach(p-> result.put(p.get("BOM_TRAINNUMBER"),p.get("BOM_BELONG_PRO")));
        LOGGER.info(JSON.toJSONString(result));
    }
}
