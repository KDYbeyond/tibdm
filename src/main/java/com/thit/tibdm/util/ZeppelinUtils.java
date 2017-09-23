package com.thit.tibdm.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author 匡东洋 Email：473948143@qq.com
 * @version 1.0
 * @create 2017/8/25 17:35
 * Description:
 */
public class ZeppelinUtils {
    /**
     * 打印日志
     */
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ZeppelinUtils.class);

    /**
     * @param path       路径
     * @param jsonObject Json实体
     * @return 服务器反馈
     * @throws IOException 异常
     */
    public static String sendPostJson(String path, JSONObject jsonObject) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();
        // 发送POST请求
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        if (jsonObject != null) {
            dataOutputStream.writeBytes(jsonObject.toString());
        }
        dataOutputStream.flush();
        dataOutputStream.close();
        // 读取响应
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines;
        StringBuffer stringBuffer = new StringBuffer();
        while ((lines = bufferedReader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            stringBuffer.append(lines);
        }
        bufferedReader.close();
        connection.disconnect();
        return stringBuffer.toString();
    }

    /**
     * @param path 路径
     * @return 返回值
     * @throws IOException 异常
     */
    public static String sendGetJson(String path) throws IOException {
        StringBuffer result = new StringBuffer();
        BufferedReader bufferedReader = null;
        URL url = new URL(path);
        URLConnection urlConnection = url.openConnection();
        // 设置通用的请求属性
        urlConnection.setRequestProperty("accept", "*/*");
        // 建立实际的连接
        urlConnection.connect();
        // 定义 BufferedReader输入流来读取URL的响应
        InputStream urlInputStream = urlConnection.getInputStream();
        InputStreamReader urlInputStreamReader = new InputStreamReader(urlInputStream);
        bufferedReader = new BufferedReader(urlInputStreamReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        if (bufferedReader != null) {
            bufferedReader.close();
        }
        if (urlInputStreamReader != null) {
            urlInputStreamReader.close();
        }
        if (urlInputStream != null) {
            urlInputStream.close();
        }
        return result.toString();
    }

    /**
     * 发送"DELETE"请求
     * @param urlPath 路径
     * @return 执行反馈
     * @throws IOException 异常
     */
    public static String sendDeleteRequest(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("content-type", "text/html");
        connection.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        StringBuffer stringBuffer = new StringBuffer();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }
        bufferedReader.close();
        return stringBuffer.toString();
    }

    /**
     * 创建noteBook
     * @param urlPath  note路径
     * @param noteName note名称
     * @return 创建noteBook的ID
     * @throws IOException 异常
     */
    public static String createNoteBook(String urlPath, String noteName) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", noteName);
        String noteBookJsonString = sendPostJson(urlPath, jsonObject);
        JSONObject noteBookJson = JSONObject.fromObject(noteBookJsonString);
        String noteBookID = (String) noteBookJson.get("body");
        return noteBookID;
    }

    /**
     * 创建段落
     *
     * @param urlPath       api路径 从配置文件中读取
     * @param noteBookID    notebook的ID
     * @param paragraphName 段落名字
     * @param text          段落的内容
     * @return 段落ID
     * @throws IOException 异常
     */
    public static String createPragraph(String urlPath, String noteBookID, String paragraphName, String text) throws IOException {
        String path = urlPath + "/" + noteBookID + "/paragraph";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", paragraphName);
        jsonObject.put("text", text);
        String paragraphJsonStr = sendPostJson(path, jsonObject);
        JSONObject paragraphJson = JSONObject.fromObject(paragraphJsonStr);
        String paragraphID = (String) paragraphJson.get("body");
        return paragraphID;
    }

    /**
     * 执行某个noteBook所有的paragraph
     *
     * @param urlPath 路径
     * @param noteBookID noteBookID
     * @throws IOException 异常
     */
    public static void exeAllPragraph(String urlPath, String noteBookID) throws IOException {
        String path = urlPath + "/job/" + noteBookID;
        sendPostJson(path, null);
    }

    /**
     * 获取sqlParagraph的执行结果
     *
     * @param urlPath     路径
     * @param noteBookID  noteBook的ID
     * @param paragraphID 段落的ID
     * @return 返回执行结果
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    public static String getParagraphExeResult(String urlPath, String noteBookID, String paragraphID) throws IOException, InterruptedException {
        String paragraphStatus = urlPath + "/job/" + noteBookID + "/" + paragraphID;
        String paragraphInfo = urlPath + "/" + noteBookID + "/paragraph/" + paragraphID;
        JSONObject statusJson = null;
        statusJson = JSONObject.fromObject(sendGetJson(paragraphStatus));
        if (statusJson != null) {
            while (!"FINISHED".equals(JSONObject.fromObject(statusJson.get("body")).get("status"))) {
                statusJson = JSONObject.fromObject(ZeppelinUtils.sendGetJson(paragraphStatus));
                LOGGER.info("status:" + JSONObject.fromObject(statusJson.get("body")).get("status"));
                Thread.sleep(2000);
            }
        }
        String sqlResult = sendGetJson(paragraphInfo);
        JSONObject resultJsonObject = JSONObject.fromObject(sqlResult);
        String tableResult = (String) resultJsonObject.getJSONObject("body").getJSONObject("results").getJSONArray("msg").getJSONObject(0).get("data");
        LOGGER.info(tableResult);
        return tableResult;
    }

    /**
     *
     * @param urlPath 路径
     * @param noteBookID noteBookID
     * @throws IOException 异常
     */
    public static void deleteNoteBook(String urlPath, String noteBookID) throws IOException {
        String path = urlPath + "/" + noteBookID;
        sendDeleteRequest(path);
    }

    /**
     * 删除段落
     * @param urlPath 路径
     * @param noteBookID noteBookID
     * @param paragraphID paragraphID
     * @throws IOException 异常
     */
    public static void deletePragraph(String urlPath,String noteBookID,String paragraphID) throws IOException {
        String path = urlPath+"/" + noteBookID+"/paragraph/"+paragraphID;
        sendDeleteRequest(path);
    }

    /**
     * 查询所有的noteBook
     * @param urlPath 路径
     * @return 所有NoteBook
     * @throws IOException 异常
     */
    public static String getAllNotebooks(String urlPath) throws IOException {
        String result = sendGetJson(urlPath);
        JSONObject jsonObject = JSONObject.fromObject(result);
        JSONArray jsonArray =  jsonObject.getJSONArray("body");
        StringBuffer stringBuffer = new StringBuffer();
        for (Object object:jsonArray) {
            LOGGER.info(((JSONObject)object).get("name").toString()+"\t");
            stringBuffer.append(((JSONObject)object).get("name").toString()+"\t");
            LOGGER.info(((JSONObject)object).get("id").toString()+"\n");
            stringBuffer.append(((JSONObject)object).get("id").toString()+"\n");
        }
        return stringBuffer.toString();
    }

    /**
     * 获取某个noteBook的所有paragraph
     * @param urlPath 路径
     * @param noteBookID notebookID
     * @return 返回结果
     * @throws IOException 异常
     */
    public static String getAllParagraphStatus(String urlPath,String noteBookID) throws IOException {
        String path = urlPath+"/job/"+noteBookID;
        String result = sendGetJson(path);
        JSONObject jsonObject = JSONObject.fromObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("body");
        StringBuffer stringBuffer = new StringBuffer();
        for (Object object:jsonArray) {
            LOGGER.info(((JSONObject)object).get("id").toString()+"\t");
            stringBuffer.append(((JSONObject)object).get("id").toString()+"\t");
            LOGGER.info(((JSONObject)object).get("status").toString()+"\n");
            stringBuffer.append(((JSONObject)object).get("status").toString()+"\n");
        }
        return stringBuffer.toString();
    }
}
