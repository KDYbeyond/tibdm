package com.thit.tibdm.db.bridge.job.util;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.NettyTransmission.util.ResourceUtil;
import com.thit.tibdm.api.TXIBDMApi;
import com.thit.tibdm.calculation.DBConnection;
import com.thit.tibdm.imdb.bridge.util.RedisUtil;
import com.thit.tibdm.util.HttpClientKeepSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by dongzhiquan on 2017/6/28.
 */
public class OnlineUtil {
    /**
     *
     */
    private static final String ONLINE = "online";
    /**
     *
     */
    private static final String OFFLINE = "offline";
    /**
     *
     */
    private static final String TABLE = "shanghai_online";
    /**
     *
     */
    private static Logger logger = LoggerFactory.getLogger(OnlineUtil.class);

    /**
     * 判断在线离线状态
     */
    public static void doOnline() {
        logger.info("开始执行定时任务==========");
        String keys = "[\"COLLECT_TIME\"]";
        String chsSql = ResourceUtil.getProValueByNameAndKey("cassandra-db", "chs_sql");
        logger.info("车号的cql为：" + chsSql.toString());
        List<String> chList = DBConnection.getListBySql(chsSql);
        logger.info(chList.toString());
//        String str = JSON.toJSONString(chList.get(0));
        String str = chList.get(0);
        String[] strArry = str.split(",");
        List<String> idsList = Arrays.asList(strArry);
        logger.info("车号列表为：" + idsList.toString());
        String machineIds = JSON.toJSONString(idsList);
        String map = "";
        Map<String, Map<String, String>> mapAll = new HashMap<>();
        try {
            map = TXIBDMApi.XiGetRealTimeDataByKey("shanghai", machineIds, "240", keys, null);

            mapAll = (Map<String, Map<String, String>>) JSON.parse(map);
            long l = System.currentTimeMillis();
            Map<String, String> map1 = new HashMap<>();

            //开始就设置成offline  是为了防止有些车一直不发数据
            idsList.forEach(id -> {
                String status = RedisUtil.getOnlineStatus(TABLE, id);
                if (status == null) {
                    RedisUtil.setOnlineStatus(TABLE, id, OFFLINE);
                }
                map1.put(id, OFFLINE);
            });

            //判断在线离线
            mapAll.forEach((k, v) -> {
                if (Math.abs(l - Long.parseLong(v.get("COLLECT_TIME"))) > Long.parseLong(ResourceUtil.getProValueByNameAndKey("redis-db", "offlinetime"))) {
                    map1.put(k, OFFLINE);
                } else {
                    map1.put(k, ONLINE);
                }
            });

            Map<String, String> map2 = new HashMap<>();

            String trainNumberStr = "";
            List<String> list1 = new ArrayList<>();
            //判断和上一次状态是否改变
            map1.forEach((k, v) -> {
                String status = RedisUtil.getOnlineStatus(TABLE, k);
                logger.info("键值对：" + TABLE + "==" + k + "==" + v);
                if (!status.equals(v)) {
                    list1.add(k);
                    map2.put(k, v);
                    logger.info("需要更新的是：" + TABLE + "==" + k + "==" + v);
                    RedisUtil.setOnlineStatus(TABLE, k, v);
                }
            });
            if (list1.size() != 0) {
                trainNumberStr = JSON.toJSONString(list1);
                trainNumberStr = trainNumberStr.replace("\"", "\'");
                trainNumberStr = trainNumberStr.substring(1, trainNumberStr.length() - 1);
            }
            logger.info("发送的json为：" + JSON.toJSONString(map2));
            if (map2.size() != 0) {
                try {
                    HttpClientKeepSession.pushOnline(trainNumberStr, JSON.toJSONString(map2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                System.out.println(trainNumberStr+"//"+JSON.toJSONString(map2));
                logger.info(trainNumberStr + "//" + JSON.toJSONString(map2));
            }
//            System.out.println(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
