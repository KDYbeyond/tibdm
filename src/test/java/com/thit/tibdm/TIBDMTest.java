//package com.thit.tibdm;
//
//import com.alibaba.fastjson.JSON;
//import com.datastax.driver.mapping.Mapper;
//import com.datastax.driver.mapping.MappingManager;
//import com.thit.tibdm.api.TXIBDMApi;
//import com.thit.tibdm.calculation.job.CalculationTimer;
//import com.thit.tibdm.calculation.CassandraUtil;
//import com.thit.tibdm.calculation.stru.ResultEveryDay;
//import com.thit.tibdm.calculation.util.CalculationUtil;
//import com.thit.tibdm.db.bridge.api.TXIBDBApi;
//import com.thit.tibdm.db.bridge.impl.TXICassandraV2Impl;
//import com.thit.tibdm.db.bridge.util.CassandraSingleConnect;
//import com.thit.tibdm.imdb.bridge.util.RedisClusterSingleConnect;
//import com.thit.tibdm.imdb.bridge.util.RedisSentinelSingleConnect;
//import com.thit.tibdm.mq.bridge.MqProducer;
//import com.thit.tibdm.util.CSVFileUtil;
//import com.thit.tibdm.util.DateUtil;
//import com.xicrm.common.TXILog;
//import com.xicrm.common.TXISystem;
//import com.xicrm.model.TXIModel;
//import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
//import net.sourceforge.groboutils.junit.v1.TestRunnable;
//import org.apache.commons.lang.StringEscapeUtils;
//import org.apache.commons.lang.time.DateUtils;
//import org.junit.Before;
//import org.junit.Test;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisCluster;
//
//import java.sql.Timestamp;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
///**
// * Created by wanghaoqiang on 2016/10/8.
// */
//public class TIBDMTest {
//
//    @Before
//    public void start(){
//        TXISystem.start();
//    }
//    @Test
//    public void testsss() throws Exception {
//        String queryJsonKeys1 = "{\"COLLECT_TYPE\":\"240\",\"MACHINE_ID\":\"70ce3da12a274ca58440520bdc224069\""
//                + ",\"ATTR_NAME1\": \"AB\",\"ATTR_NAME2\": \"BBXX_TCMS_ZKDYBB1\"}";
//        TXIBDMApi.XiGetPeriodsData("collect_data_1220", "240", "70ce3da12a274ca58440520bdc224069", queryJsonKeys1, "1482212942790",
//                "1482212942792", "3", "1", "1", "2", null);
//    }
//
//    /**
//     * 测试获取实时数据
//     * 测试结果正确:实时数据的获取最多的消耗就是在于连接生成
//     */
//    @org.junit.Test
//    public void testRedis() {
//
//        String s = TXIBDMApi.XiGetRealTimeData("shanghai", "329", "240", null);
//        System.out.println(s);
//    }
//
//    /**
//     * 批量保存数据
//     */
//    @Test
//    public void testSave2DbBatch() {
//        List list = new ArrayList();
//        Map s = toJsonString("199", "8");
//        list.add(s);
//        s = toJsonString("199", "888");
//        list.add(s);
//        String listJson = JSON.toJSONString(list);
//        TXISystem.start();
//        try {
//            TXIBDMApi.XiSave2Dbs("collect_data_1220", "33", "GPS", listJson, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 测试保存数据到数据库
//     * 测试结果:cassandra每一次执行cql大概为不到500ms
//     * redis 生成了连接之后每次需要很短的时间
//     */
//    @Test
//    public void testSave2Db() {
//        String s = JSON.toJSONString(toJsonString("199", "80"));
//        System.out.println(s);
//        try {
//            TXIBDMApi.XiSave2Db("collect_data_1220", "334444", "GPS", s, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 获取批量数据多条
//     */
//    @Test
//    public void testGetRealTimeBatch() throws Exception {
//        TXISystem.start();
//        String[] list = {"0928b87bf0271ab90743e1551a915f9c", "f6420dd76f69a193294e2504dd1020a5"};
//        String s = JSON.toJSONString(list);
//        String s1 = TXIBDMApi.XiGetRealTimes("collect_data_1220", "240", s, null);
//        String s2 = StringEscapeUtils.unescapeJava(s1);
//        System.out.println(s2);
//    }
//
//    /**
//     * 获取分段大数据
//     * V1-VERSION
//     */
//    @Test
//    public void testXiGetPeriodsDataV1() {
//        String opc_data = null;
//        TXIBDMApi txibdmApi = new TXIBDMApi();
//        try {
//            opc_data = txibdmApi.XiGetPeriodsData("opc_data", "", "", "{\"query_attrs\":[{\"TAGNAME\":\"TY_CL_DM.XZGJG_TDM5.SW_ET116_5_EH\",\"ATTR_NAME\":\"F1\"},{\"TAGNAME\":\"TY_BGQ_DM.ZP_TDM16.SW_DL9104_EH\",\"ATTR_NAME\":\"F2\"}]}", "1473008401000", "1473094801000", "3", "1", "1", "1", null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(opc_data);
//    }
//
//    /**
//     * 获取分段大数据
//     * V2-VERSION
//     */
//    @Test
//    public void testXiGetPeriodsDataV2() throws Exception {
//        String filePath = "/Users/rmbp13/Desktop/data_import.csv";
//        mapList = getMapList(filePath);
//        int num = 50;
//        TestRunnable[] trs = new TestRunnable[num];
//        for (int i = 0; i < num; i++) {
//            trs[i] = new ThreadQuery();
//        }
//
//        // 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
//        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
//        // 开发并发执行数组里定义的内容
//        long startTime = System.currentTimeMillis();//获取当前时间
//        try {
//            mttr.runTestRunnables();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("程序执行时间：" + (endTime - startTime) + "ms");
//    }
//
//    /**
//     * 保存补录的数据
//     */
//    @Test
//    public void testMakeupData() throws Exception {
//        TXISystem.start();
//        TXIBDBApi txibdbApi = new TXIBDBApi();
//        txibdbApi.saveMarkupData("object_name", "collect_time", "machine_id", "collect_time");
//    }
//
//    /**
//     * 获取补录的数据
//     *
//     * @throws Exception
//     */
//    @Test
//    public void testGetMakeupData() throws Exception {
//        TXISystem.start();
//        String markupData = TXIBDMApi.XiGetMakeupData("collect_data_1220", "22", "GPS", null);
//        System.out.println(markupData);
//    }
//
//    /**
//     * 测试单条删除
//     */
//    @Test
//    public void testDelMakeupData() throws Exception {
//        TXIBDMApi.XiDelMakeupData("collect_data_1220", "22", "GPS", "1477286503526", null);
//    }
//
//    /**
//     * 测试批量删除
//     */
//    @Test
//    public void testDelMakeups() throws Exception {
//        list.add("1477286503526");
//        list.add("1477286503525");
//        TXIBDMApi.XiDelMakeups("collect_data_1220", "22", "GPS", JSON.toJSONString(list), null);
//    }
//
//    @Test
//    public void testXiConfig() {
//        String str = TXISystem.config.getProperty("Tibdm_RealTimeData_ImplClass");
//        System.out.println("===" + str);
//    }
//
//
//    /**
//     * 多线程插入测试
//     * 指标:
//     * 线程数:500
//     * 并发执行插入4W多条数据
//     * 测试指标:成功数量和插入速度
//     * <p/>
//     * 单独测试redis插入的话:
//     * 执行时间大概在55148ms左右
//     */
//    @Test
//    public void mutiThreadTest() throws Exception {
//        String filePath = "/Users/rmbp13/Desktop/data_import.csv";
//        mapList = getMapList(filePath);
//        int num = 50;
//        TestRunnable[] trs = new TestRunnable[num];
//        for (int i = 0; i < num; i++) {
//            trs[i] = new ThreadInserter();
//        }
//
//        // 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
//        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
//        // 开发并发执行数组里定义的内容
//        long startTime = System.currentTimeMillis();//获取当前时间
//        try {
//            mttr.runTestRunnables();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("程序执行时间：" + (endTime - startTime) + "ms");
//    }
//
//    private Queue mapList;
//
//    private List list = new ArrayList();
//
//    private class ThreadInserter extends TestRunnable {
//        @Override
//        public void runTest() throws Throwable {
//            // 测试内容
////            testMutiThread(0);
//            save2DbMutiThread(0);
//        }
//    }
//
//    private class ThreadQuery extends TestRunnable {
//        @Override
//        public void runTest() throws Throwable {
//            // 测试内容
//            query2DbMutiThread(1);
//        }
//    }
//
//    public void save2DbMutiThread(int type) throws Exception {
//        System.out.println("===" + Thread.currentThread().getId() + "begin to execute save2DbMutiThread");
//        Map peek = (Map) mapList.poll();
//        String s = JSON.toJSONString(peek);
//        if (type == 0) {
//            while (mapList.size() != 0) {
//                TXIBDMApi.XiSave2Db("collect_data_1109", peek.get("COLLECT_TYPE").toString(), peek.get("MECHINE_ID").toString(), s, null);
////                String s1 = TXIBDMApi.XiGetRealTimeData("collect_data_1019", peek.get("COLLECT_TYPE").toString(), peek.get("MECHINE_ID").toString());
//                peek = (Map) mapList.poll();
////                if (s1==null){
////                    System.out.println("为空值");
////                }
//                System.out.println("还剩多少个了=========" + mapList.size());
//            }
//        } else {
//            if (list.size() <= 5) {
//                list.add(s);
//            } else {
//                String jsonList = JSON.toJSONString(list);
//                TXIBDMApi.XiSave2Dbs("collect_data_1019", peek.get("COLLECT_TYPE").toString(), peek.get("MECHINE_ID").toString(), jsonList, null);
//                list.clear();
//                System.out.println("===" + Thread.currentThread().getId() + "抢到了这个数组并发送他,数组的最后大小" + list.size());
//            }
//        }
//        System.out.println("===" + Thread.currentThread().getId() + "end to execute save2DbMutiThread");
//    }
//
//    public void query2DbMutiThread(int type) throws Exception {
//        System.out.println("===" + Thread.currentThread().getId() + "begin to execute query2DbMutiThread");
//        Map peek = (Map) mapList.poll();
//        int i = 50;
//        while (i > 0) {
//            TXIBDMApi txibdmApi = new TXIBDMApi();
//            long startTime = System.currentTimeMillis();//获取当前时间
//
//            String query_json_keys2 = "{\"COLLECT_TYPE\":\"240\",\"COLLECT_TIME\":\"" + startTime + "\",\"MECHINE_ID\":\"" + peek.get("MECHINE_ID").toString() + "\""
//                    + "}";
//
////            String query_json_keys = "{\"COLLECT_TYPE\":\"" + peek.get("COLLECT_TYPE").toString() + "\",\"MECHINE_ID\":\"" + peek.get("MECHINE_ID").toString() + "\"}";
//            txibdmApi.XiGetPeriodsData("collect_data_1109", peek.get("COLLECT_TYPE").toString(), peek.get("MECHINE_ID").toString(), query_json_keys2, "0", startTime + "", "5", "1", "1", "1", null);
//            long endTime = System.currentTimeMillis();
//            System.out.println("cassandra查询程序运行时间：" + (endTime - startTime) + "ms");
//            peek = (Map) mapList.poll();
//            i--;
//        }
//
//        System.out.println("===" + Thread.currentThread().getId() + "end to execute query2DbMutiThread");
//    }
//
//    public Map toJsonString(String wdValue, String sdValue) {
//        Map map = new HashMap();
//        map.put("wd", wdValue);
//        map.put("sd", sdValue);
//        map.put("OBJECT_NAME", "collect_data_1220");
//        map.put("MACHINE_ID", "33");
//        map.put("COLLECT_TYPE", "GPS");
//        map.put("COLLECT_TIME", "1477286503525");
////        map.put("COLLECT_TIME", System.currentTimeMillis()+"");
//        map.put("RECEIVE_TIME", "123454543543");
//        String s = JSON.toJSONString(map);
//        return map;
//    }
//
//    @Test
//    public void testCvs() throws Exception {
//        String filePath = "/Users/rmbp13/Desktop/data_import.csv";
//        //接收到4W条数据,然后开多个线程去插入
//        //用队列来接收这些数据然后挨个推出
//        Queue mapList = getMapList(filePath);
//    }
//
//    @Test
//    public void testLogInfo() throws Exception {
//        String jsonString = "[{\"COLLECT_TYPE\":\"123123\",\"COLLECT_TIME\":\"1459477560000\",\"RECIEVE_TIME\":\"2015/4/8 11:44\",\"MECHINE_ID\":\"003\""
//                + ",\"wd\":\"36\",\"sd\":\"10\",\"fx\":\"north\"},{\"COLLECT_TYPE\":\"123123\",\"COLLECT_TIME\":\"1459477560000\",\"RECIEVE_TIME\":\"2015/4/8 11:44\",\"MECHINE_ID\":\"004\""
//                + ",\"wd\":\"36\",\"sd\":\"10\",\"fx\":\"north\"},{\"COLLECT_TYPE\":\"123123\",\"COLLECT_TIME\":\"1459477560000\",\"RECIEVE_TIME\":\"2015/4/8 11:44\",\"MECHINE_ID\":\"005\""
//                + ",\"wd\":\"36\",\"sd\":\"10\",\"fx\":\"north\"},{\"COLLECT_TYPE\":\"123123\",\"COLLECT_TIME\":\"1459477560000\",\"RECIEVE_TIME\":\"2015/4/8 11:44\",\"MECHINE_ID\":\"006\""
//                + ",\"wd\":\"36\",\"sd\":\"10\",\"fx\":\"north\"}]";
////       CassandraV2CollectInfo ci =null;
////    List<CassandraV2CollectInfo> ciList=JSON.parseArray(jsonString ,CassandraV2CollectInfo.class);
//        List<String> ciList = JSON.parseArray(jsonString, String.class);
//        TXICassandraV2Impl tx = new TXICassandraV2Impl();
//        String object_name = "collect_data_1019";
//        String collect_type = "201610261804";
//        String machine_id = "000245ec99fb98b86743a2ac0f1e361a";
//        String collect_time1 = "1459477560000";
//        String collect_time2 = "1459477570000";
//        String collect_time3 = "1459477580000";
//        String ss = "[\"1459477560000\",\"1459477570000\",\"1459477580000\",]";
//
////        tx.save2BigDBs("collect_data_1019", collect_type, "10311339",jsonString);
////            tx.saveMakeupData("collect_data_1019",collect_type,machine_id,collect_time3);
////        tx.getMakeupData(object_name,"gps","0123");
////    tx.delMakeupData(object_name,collect_type,machine_id,collect_time);
////    tx.delMakeups(object_name,collect_type,machine_id,ss);
//
//
//    }
//
//    public Queue getMapList(String path) throws Exception {
//        //使用一个线程安全的queue来接收csv传入的数据,
//        Queue mapList = new ConcurrentLinkedQueue();
//        CSVFileUtil csvFileUtil = new CSVFileUtil(path);
//        String s = csvFileUtil.readLine();
//        ArrayList keylist = CSVFileUtil.fromCSVLinetoArray(s);
//        //将所有的数据变成一个LIST<MAP>结构来准备插入
//        while (s != null) {
//            Map map = new HashMap();
//            s = csvFileUtil.readLine();
//            ArrayList arrayList = CSVFileUtil.fromCSVLinetoArray(s);
//            //如果key对应的值为空的话则不插入这个key
//            for (Object key : keylist) {
////                System.out.println(key);
//                int i = keylist.indexOf(key.toString());
//                String value;
//                if (arrayList.size() != 0) {
//                    if (key.toString().equals("COLLECT_TIME")) {
//                        value = DateUtil.getTime(arrayList.get(i).toString()) + "";
//                    } else {
//                        value = arrayList.get(i).toString();
//                    }
//                    map.put(key.toString(), value);
//                }
//            }
//            mapList.add(map);
//        }
//        return mapList;
//    }
//
//
//    @Test
//    public void testPeriod() throws Exception {
//        TXISystem.start();
////        for (int i = 0; i < 100; i++) {
//        long startTime = System.currentTimeMillis();//获取当前时间
//        String query_json_keys2 = "{\"COLLECT_TYPE\":\"240\",\"COLLECT_TIME\":\"" + startTime + "\",\"MACHINE_ID\":\"f6420dd76f69a193294e2504dd1020a5\""
//                + "}";
//        String s = TXIBDMApi.XiGetPeriodsData("collect_data_1220", "240", "f6420dd76f69a193294e2504dd1020a5", query_json_keys2, "0", startTime + "", "3", "1", "1", "1", null);
//        System.out.println(s);
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("cassandra查询程序运行时间：" + (endTime - startTime) + "ms");
////        }
//    }
//
//    /**
//     * 大量查询分段大数据并输出时间
//     */
//    public void testPeriods() throws Exception {
//        String filePath = "/Users/rmbp13/Desktop/data_import.csv";
//        Queue mapList = getMapList(filePath);
//
//    }
//
//    @Test
//    public void insetResultEveryDay() {
//        MappingManager manager = new MappingManager(CassandraSingleConnect.INSTANCE.getInstance());
//        Mapper<ResultEveryDay> mapper = manager.mapper(ResultEveryDay.class);
////        mapper.save(new ResultEveryDay("opc_data", "TY_CL_DM.XZGJG_TDM5.SW_ET116_5_EH", Long.parseLong("1473008401000"), "5", "6", "7", "9", "10"));
//    }
//
//    @Test
//    public void testTime() throws ParseException {
//        Map map = CalculationUtil.getStartTime(System.currentTimeMillis() + "", "3");
//        Timestamp start = new Timestamp(Long.parseLong("1482249900000"));
//        Timestamp end = new Timestamp(Long.parseLong(map.get("endTime").toString()));
//        Timestamp now = new Timestamp(DateUtils.addYears(new Date(), -2).getTime());
//        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        String sDate = sdf.format(start);
//        String eDate = sdf.format(end);
//        String nDate = sdf.format(now);
//        System.out.println(sDate + "-------" + eDate);
//        Date parse = sdf.parse("2016/8/01 00:00:00");
//        System.out.println(System.currentTimeMillis());
//    }
//
//    @Test
//    public void testCalucation() throws Exception {
//        TXIModel txiModel = null;
//        String opc_data = TXIBDMApi.XiCalculation("opc_data", "[\"TY_JC_DM.GCC_TDM25.SW_ET112_6_EH\",\"DY_GYPD_K.M102_110KV2.SW_EP1_356_EH\",\"DY_GYPD_K.M102_110KV2.SW_EP1_356_ED\"]", System.currentTimeMillis() + "", 3 + "", txiModel);
//        System.out.println(opc_data);
//    }
//
//    @Test
//    public void testCalcution() {
//        CalculationTimer.calculationLastDay();
//    }
//
//    @Test
//    public void testCuTime() {
//        System.out.println(System.currentTimeMillis());
//        double a = 1.23566;
//        System.out.println(CalculationUtil.getDoubleFormat(a));
//    }
//
//    @Test
//    public void getModel() throws Exception {
//        CSVFileUtil csvFileUtil = new CSVFileUtil("/Users/rmbp13/Desktop/data_value.csv");
//        String s = csvFileUtil.readLine();
//        ArrayList keylist = CSVFileUtil.fromCSVLinetoArray(s);
//        Map map = new HashMap();
//        for (Object key : keylist) {
//            map.put(key.toString(), "${" + key.toString() + "}");
//        }
//        System.out.println(JSON.toJSONString(map));
//    }
//
//    @Test
//    public void testJedisSingle() {
////        JedisCluster instance = RedisClusterSingleConnect.INSTANCE.getInstance();
//        Jedis instance = RedisSentinelSingleConnect.INSTANCE.getInstance().getResource();
//        instance.set("name", "jiaojiao");
//    }
//
//    @Test
//    public void testKafka() throws InterruptedException {
//        for (int i = 0; i < 100; i++) {
////            MqProducer.sendMsg("message" + i);
////            Thread.sleep(1000);
//        }
//    }
//
//
//    @Test
//    public void testMapJson() {
//        Map map = new HashMap<>();
//        String s = JSON.toJSONString(map);
//        System.out.println(s);
//    }
//
//    @Test
//    public void testStart(){
//        String property = TXISystem.config.getProperty("AppSrvName");
//        System.out.println(property);
//    }
//
//
//
//
//}
