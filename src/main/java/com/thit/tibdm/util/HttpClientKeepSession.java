package com.thit.tibdm.util;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 保持同一session的HttpClient工具类
 *
 * @author zhangwenchao
 */
public class HttpClientKeepSession {
    /**
     * 日志
     */
    public static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);
    /**
     * 客户端
     */
    public static CloseableHttpClient httpClient = null;
    /**
     * 客户端Context
     */
    public static HttpClientContext context = null;
    /**
     * 存储Cookie
     */
    public static CookieStore cookieStore = null;
    /**
     * 请求配置
     */
    public static RequestConfig requestConfig = null;

    static {
        init();
    }

    /**
     *
     */
    private static void init() {
        context = HttpClientContext.create();
        cookieStore = new BasicCookieStore();
        // 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
        requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
                .setConnectionRequestTimeout(60000).build();
        // 设置默认跳转以及存储cookie
        httpClient = HttpClientBuilder.create()
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore).build();
    }

    /**
     * http get
     *
     * @param url 路径
     * @return response 应答
     * @throws ClientProtocolException 异常
     * @throws IOException 异常
     */
    public static CloseableHttpResponse get(String url) throws ClientProtocolException, IOException {
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpget, context);
//        try {
        cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            LOG.debug("key:" + cookie.getName() + "  value:" + cookie.getValue());
        }
//        } finally {
//            response.close();
//        }
        return response;
    }

    /**
     * http post
     *
     * @param url 路径
     * @param parameters form表单
     * @return response
     * @throws ClientProtocolException 异常
     * @throws IOException 异常
     */
    public static CloseableHttpResponse post(String url, String parameters)
            throws IOException {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = toNameValuePairList(parameters);
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost, context);
        try {
            cookieStore = context.getCookieStore();
            List<Cookie> cookies = cookieStore.getCookies();
            for (Cookie cookie : cookies) {
                LOG.error("key:" + cookie.getName() + "  value:" + cookie.getValue());
            }
        } finally {
            response.close();
        }
        return response;

    }

    /**
     * @param url        路径
     * @param parameters 参数
     * @return 返回
     * @throws IOException 异常
     */
    public static CloseableHttpResponse postPro(String url, String parameters)
            throws IOException {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = toNameValuePairListForAnd(parameters);
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost, context);
        cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            LOG.error("key:" + cookie.getName() + "  value:" + cookie.getValue());
        }

        return response;

    }



    /**
     * @param parameters 参数
     * @return 列表
     */
    private static List<NameValuePair> toNameValuePairList(String parameters) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        String[] paramList = parameters.split("&");
        for (String parm : paramList) {
            int index = -1;
            for (int i = 0; i < parm.length(); i++) {
                index = parm.indexOf("=");
                break;
            }
            String key = parm.substring(0, index);
            String value = parm.substring(++index, parm.length());
            nvps.add(new BasicNameValuePair(key, value));
        }
        System.out.println(nvps.toString());
        return nvps;
    }


    @SuppressWarnings("unused")
    private static List<NameValuePair> toNameValuePairListForAnd(String parameters) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        String[] paramList = parameters.split("&");
        for (String parm : paramList) {
            int index = -1;
            for (int i = 0; i < parm.length(); i++) {
                index = parm.indexOf("=");
                break;
            }
            //处理&符号
            if (parm.indexOf("%26") != -1) {
                parm = parm.replace("%26", "&");
            }
            String key = parm.substring(0, index);
            String value = parm.substring(++index, parm.length());
            nvps.add(new BasicNameValuePair(key, value));
        }
        System.out.println(nvps.toString());
        return nvps;
    }

    /**
     * 把结果console出来
     *
     * @param httpResponse 应答
     * @throws ParseException 异常
     * @throws IOException    异常
     */
    public static void printResponse(HttpResponse httpResponse) throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        System.out.println("status:" + httpResponse.getStatusLine());
        System.out.println("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            System.out.println("\t" + iterator.next());
        }
        // 判断响应实体是否为空
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            System.out.println("response length:" + responseString.length());
            System.out.println("response content:" + responseString.replace("\r\n", ""));
        }
        System.out.println(
                "------------------------------------------------------------------------------------------\r\n");
    }
//
//    /**
//     * 把当前cookie从控制台输出出来
//     */
//    public static void printCookies() {
//        System.out.println("headers:");
//        cookieStore = context.getCookieStore();
//        List<Cookie> cookies = cookieStore.getCookies();
//        for (Cookie cookie : cookies) {
//            System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
//        }
//    }

//    /**
//     * 检查cookie的键值是否包含传参
//     *
//     * @param key 关键字
//     * @return 返回
//     */
//    public static boolean checkCookie(String key) {
//        cookieStore = context.getCookieStore();
//        List<Cookie> cookies = cookieStore.getCookies();
//        boolean res = false;
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals(key)) {
//                res = true;
//                break;
//            }
//        }
//        return res;
//    }

    /**
     * 直接把Response内的Entity内容转换成String
     *
     * @param httpResponse 应答
     * @return 返回
     * @throws ParseException 异常
     * @throws IOException    异常
     */
    public static String toString(CloseableHttpResponse httpResponse) throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity);
        } else {
            return null;
        }
    }


    /**
     * 发送故障或者异常代码到应用服务器
     *
     * @param msg 参数
     * @return 返回
     * @throws Exception 异常
     */
    public static String pushMsg(String flag, String msg) throws IOException {

        String address = ResourceUtil.getProValueByNameAndKey("httpclient", "address");
        String user = ResourceUtil.getProValueByNameAndKey("httpclient", "user");
        String passwd = ResourceUtil.getProValueByNameAndKey("httpclient", "passwd");
        String urlsend = ResourceUtil.getProValueByNameAndKey("httpclient", "urlsend");
        LOG.info("发送的IP地址以及内容为：" + address + "==" + urlsend + "==" + msg + "==" + flag);
        /**
         * 登录系统
         */
        CloseableHttpResponse closeableHttpResponse = HttpClientKeepSession.post("http://" + address + ":8083/txieasyui",
                "taskFramePN=AccessCtrl&colname=json_ajax&command=Login&colname=json&colname1={\"dataform" +
                        "\":\"eui_form_data\"}&refresh=0.6679650753829471&loginname=" + user
                        + "&loginpass=" + passwd);
        /**
         * 发送消息
         */
        CloseableHttpResponse response1 = HttpClientKeepSession.post("http://" + address + ":8083/txieasyui", urlsend + "flag=" + flag + "&troubleArr=" + msg);
        return "";
    }

    /**
     * @param machine_ids 机器ID
     * @param vehicleArr  Ve
     * @return 字符
     * @throws IOException 异常
     */
    public static String pushOnline(String machine_ids, String vehicleArr) throws IOException {

        String address = ResourceUtil.getProValueByNameAndKey("httpclient", "address");
        String user = ResourceUtil.getProValueByNameAndKey("httpclient", "user");
        String passwd = ResourceUtil.getProValueByNameAndKey("httpclient", "passwd");
        String urlonline = ResourceUtil.getProValueByNameAndKey("httpclient", "urlonline");
        LOG.info("发送的IP地址以及内容为：" + address + "==" + urlonline + "==" + machine_ids + "==" + vehicleArr);
        /**
         * 登录系统
         */
        CloseableHttpResponse closeableHttpResponse = HttpClientKeepSession.post("http://" + address + ":8083/txieasyui",
                "taskFramePN=AccessCtrl&colname=json_ajax&command=Login&colname=json&colname1={\"dataform" +
                        "\":\"eui_form_data\"}&refresh=0.6679650753829471&loginname=" + user
                        + "&loginpass=" + passwd);
        /**
         * 发送消息
         */
        CloseableHttpResponse response1 = HttpClientKeepSession.post("http://" + address + ":8083/txieasyui", urlonline + "trainNumberStr=" + machine_ids + "&vehicleArr=" + vehicleArr);
        return "";
    }

    /**
     * @param protocolCode 协议码
     * @return 返回
     * @throws IOException 异常
     */

    public static String getProtocal(String protocolCode) throws IOException {

        if (protocolCode.indexOf("&") != -1) {
            protocolCode = protocolCode.replace("&", "%26");
        }

        String address = ResourceUtil.getProValueByNameAndKey("httpclient", "address");
        String user = ResourceUtil.getProValueByNameAndKey("httpclient", "user");
        String passwd = ResourceUtil.getProValueByNameAndKey("httpclient", "passwd");
        String urlonline = ResourceUtil.getProValueByNameAndKey("httpclient", "urlonline");
//        LOG.info("发送的IP地址以及内容为：" + address + "==" + urlonline + "==" + machine_ids + "==" + vehicleArr);
        /**
         * 登录系统
         */
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpResponse = HttpClientKeepSession.post("http://" + address + ":8083/txieasyui",
                    "taskFramePN=AccessCtrl&colname=json_ajax&command=Login&colname=json&colname1={\"dataform" +
                            "\":\"eui_form_data\"}&refresh=0.6679650753829471&loginname=" + user
                            + "&loginpass=" + passwd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CloseableHttpResponse response1 = HttpClientKeepSession.postPro("http://" + address + ":8083/txieasyui", "taskFramePN=BFBigData&colname=json_ajax&command=PublishProtocolForBigData&colname1={dataform:'eui_form_data',variable:'data'}&protocolCode=" + protocolCode);

        String s = toString(response1);
        closeableHttpResponse.close();
        response1.close();
        return s;
    }

    /**
     * @param address 地址
     * @param user    用户
     * @param passwd  密码
     * @throws IOException 异常
     */
    public static void login(String address, String user, String passwd) throws IOException {
        CloseableHttpResponse closeableHttpResponse = HttpClientKeepSession.post("http://" + address + ":8083/txieasyui",
                "taskFramePN=AccessCtrl&colname=json_ajax&command=Login&colname=json&colname1={\"dataform" +
                        "\":\"eui_form_data\"}&refresh=0.6679650753829471&loginname=" + user
                        + "&loginpass=" + passwd);

    }


}
