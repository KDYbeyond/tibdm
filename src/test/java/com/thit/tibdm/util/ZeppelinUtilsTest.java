package com.thit.tibdm.util;

import net.sf.json.JSONObject;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author 匡东洋 Email：473948143@qq.com
 * @version 1.0
 * @create 2017/8/28 9:22
 * Description:
 */
public class ZeppelinUtilsTest {
    /**
     * 打印日志
     */
    public static final  org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ZeppelinUtils.class);
    public  String noteBookString = "http://192.168.8.106:8080/api/notebook";
    public  String text = "%spark\nimport org.apache.spark.sql.SparkSession\nval spark = SparkSession\n" +
            "  .builder()\n" +
            "  .appName(\"cassandra\")\n" +
            "  .config(\"spark.cassandra.connection.host\", \"192.168.8.108\")\n" +
            "  .getOrCreate()\nval cassandraDF = spark.read\n" +
            "  .format(\"org.apache.spark.sql.cassandra\")\n" +
            "  .option(\"keyspace\", \"bomkp\")\n" +
            "  .option(\"table\", \"tablemachine_time\")\n" +
            "  .load()\n" +
            "  .toDF()\ncassandraDF.printSchema()\ncassandraDF.createOrReplaceTempView(\"shanghai\")\n";
    public String sqlText = "%sql\nselect count(*),machine_id from shanghai group by machine_id";
    public String paragraphStatus;
    @Test
    public void testSendJson() throws IOException, InterruptedException {
        // 创建notebook
        JSONObject noteBook = new JSONObject();
        noteBook.put("name","spark");
        String noteBookJsonString = ZeppelinUtils.sendPostJson(noteBookString, noteBook);
        JSONObject noteBookJson = JSONObject.fromObject(noteBookJsonString);
        String noteBookID= (String) noteBookJson.get("body");
        System.out.println(noteBookID);
        // 创建spark paragraph
        JSONObject sparkObject = new JSONObject();
        sparkObject.put("title","spark");
        sparkObject.put("text",text);
        String responseJson = ZeppelinUtils.sendPostJson(noteBookString + "/"+noteBookID + "/paragraph", sparkObject);
        JSONObject.fromObject(responseJson);
        // 创建sql paragraph
        JSONObject sqlObject = new JSONObject();
        sqlObject.put("title","sql");
        sqlObject.put("text",sqlText);
        String sqlParagraphString = ZeppelinUtils.sendPostJson(noteBookString+ "/" + noteBookID + "/paragraph", sqlObject);
        JSONObject sqlParagraphJson = JSONObject.fromObject(sqlParagraphString);
        String sqlParagraphID = (String) sqlParagraphJson.get("body");
       // 执行所有的paragraph
        ZeppelinUtils.sendPostJson(noteBookString+"/job/"+noteBookID,null);
       // 获得sqlPragraph的执行结果
        paragraphStatus = noteBookString+"/job/"+noteBookID+"/"+sqlParagraphID;
        JSONObject statusJson = null;
        statusJson = JSONObject.fromObject(ZeppelinUtils.sendGetJson(paragraphStatus));
        if(statusJson!=null) {
            while (!"FINISHED".equals(JSONObject.fromObject(statusJson.get("body")).get("status"))) {
                statusJson = JSONObject.fromObject(ZeppelinUtils.sendGetJson(paragraphStatus));
                System.out.println("status:" + JSONObject.fromObject(statusJson.get("body")).get("status"));
                Thread.sleep(2000);
            }
        }
        String sqlResult = ZeppelinUtils.sendGetJson(noteBookString+ "/" + noteBookID + "/paragraph/" + sqlParagraphID);
        System.out.println(sqlResult);
        JSONObject resultJsonObject = JSONObject.fromObject(sqlResult);
        System.out.println(resultJsonObject.getJSONObject("body").getJSONObject("results").getJSONArray("msg").getJSONObject(0).get("data"));
    }

    @Test
    public void testSQL() throws IOException {
        JSONObject jsonObject = new JSONObject();
        String text = "%sql\nselect count(*),machine_id from shanghai group by machine_id";
        jsonObject.put("title","sql");
        jsonObject.put("text",text);
        String responseJson = ZeppelinUtils.sendPostJson("http://192.168.8.106:8080/api/notebook/2CTSRSY8A/paragraph", jsonObject);
        System.out.println("ResponseJson:"+responseJson);
    }

    @Test
    public void testAll() throws IOException, InterruptedException {
        testSendJson();
        testSQL();
        ZeppelinUtils.sendPostJson("http://192.168.8.106:8080/api/notebook/job/2CTSRSY8A", null);
    }
    @Test
    public void testGetResult() throws IOException {
        ZeppelinUtils.sendPostJson("http://192.168.8.106:8080/api/notebook/2CTSRSY8A/paragraph/",null);
    }
    @Test
    public void testGetSend() throws IOException {
        String sendGetJson = ZeppelinUtils.sendGetJson("http://192.168.8.106:8080/api/notebook/20170828-132806_1132438389");
        System.out.println("ResponseJson:"+sendGetJson);
    }
    @Test
    public void testCreateNoteBook() throws IOException {
        String path = ResourceUtil.getProValueByNameAndKey("zeppelin","urlPath");
        String noteBookID = ZeppelinUtils.createNoteBook(path,"noteBookTest");
        LOGGER.info(noteBookID);
    }
    @Test
    public void testCreateParagraph() throws IOException {
        String path = ResourceUtil.getProValueByNameAndKey("zeppelin","urlPath");
        String noteBookID = ZeppelinUtils.createNoteBook(path,"noteBookTest");
        LOGGER.info(noteBookID);
        String pragraphString = ZeppelinUtils.createPragraph(path, noteBookID, "Spark", text);
        LOGGER.info(pragraphString);
    }
    @Test
    public void testExeParagraphAndGetSqlExeResult() throws IOException, InterruptedException {
        String path = ResourceUtil.getProValueByNameAndKey("zeppelin","urlPath");
        String noteBookID = ZeppelinUtils.createNoteBook(path,"noteBookTest");
        String pragraphStringID = ZeppelinUtils.createPragraph(path, noteBookID, "Spark", text);
        String sqlPragraphStringID = ZeppelinUtils.createPragraph(path,noteBookID,"sql",sqlText);
        ZeppelinUtils.exeAllPragraph(path,noteBookID);
        String sqlParagraphStr = ZeppelinUtils.getParagraphExeResult(path,noteBookID,sqlPragraphStringID);
        LOGGER.info(sqlParagraphStr);
    }
    @Test
    public void testDeleteNoteBook() throws IOException {
        String path = ResourceUtil.getProValueByNameAndKey("zeppelin","urlPath");
        ZeppelinUtils.deleteNoteBook(path,"2CRARH8BG");
    }
    @Test
    public void testDeleteParagraph() throws IOException {
        String path = ResourceUtil.getProValueByNameAndKey("zeppelin","urlPath");
        ZeppelinUtils.deletePragraph(path,"2CT2PYDSN","20170829-154904_134075994");
    }

    @Test
    public void testGetAllNotebooks() throws IOException {
        String path = ResourceUtil.getProValueByNameAndKey("zeppelin","urlPath");
        ZeppelinUtils.getAllNotebooks(path);
    }
    @Test
    public void testGetAllParagraphStatus() throws IOException {
        String path = ResourceUtil.getProValueByNameAndKey("zeppelin","urlPath");
        ZeppelinUtils.getAllParagraphStatus(path,"2CTF9YEW5");
    }
}
