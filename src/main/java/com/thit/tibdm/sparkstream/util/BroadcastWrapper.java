package com.thit.tibdm.sparkstream.util;

import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.bom.entity.WarningProtocolBean;
import com.thit.tibdm.imdb.bridge.util.RedisUtil;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;

/**
 * 更新广播变量
 */
public class BroadcastWrapper extends JedisPubSub {

    public final static Logger LOGGER = LoggerFactory.getLogger(BroadcastWrapper.class);

    private Broadcast<Map<String, MessageProtocol>> broadcastVar;

    private Broadcast<Map<String, String>> broadcastProCh;

    private Map<String, MessageProtocol> proMap;

    private Map<String, String> proChMap;

    private static BroadcastWrapper obj = new BroadcastWrapper();

    private static String PROTOCOL = "protocol";//协议状态表


    private BroadcastWrapper() {
    }


    public static BroadcastWrapper getInstance() {
        return obj;
    }

    public JavaSparkContext getSparkContext(SparkContext sc) {
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);
        return jsc;
    }


    public synchronized void doUpdate(SparkContext sparkContext) {

        LOGGER.error("更新方法在这里");
        Map<String, String> mapStatus = new HashMap<>();
        String change = RedisUtil.hget(PROTOCOL, "change");
        //判断是否有新的协议在redis里面没有值
        ProtocolUtil.checkStatus(PROTOCOL, change);

        //判断之后重新赋值
        change = RedisUtil.hget(PROTOCOL, "change");

        if (broadcastVar == null || broadcastProCh == null) {
            Map<String, Object> protocolAll = ProtocolUtil.getProtocolAll("");
            proMap = ProtocolUtil.getProtocolMap(protocolAll);
            proChMap = ProtocolUtil.getChProtocolMap(protocolAll);
            broadcastVar = getSparkContext(sparkContext).broadcast(proMap);

            //异常广播
            broadWarn(sparkContext);

            broadcastProCh = getSparkContext(sparkContext).broadcast(proChMap);
            RedisUtil.hset(PROTOCOL, "change", "0");
        } else {
            if (change.equals("1")) {

                mapStatus = RedisUtil.getHgetAllCluster(PROTOCOL);

                mapStatus.forEach((k, v) -> {
                    if (v.equals("1") && !k.equals("change")) {

                        try {
                            Map<String, Object> protocolAll = ProtocolUtil.getProtocolAll(k);
                            Map<String, MessageProtocol> protocolMap = ProtocolUtil.getProtocolMap(protocolAll);
                            MessageProtocol messageProtocol = protocolMap.get(k);
                            LOGGER.error("message不为空"+messageProtocol.getProtocolName());

                            proMap.put(k, messageProtocol);
                            broadcastVar.unpersist();
                            broadcastVar = getSparkContext(sparkContext).broadcast(proMap);
                            //发布告警广播
                            broadWarn(sparkContext);

                            Map<String, String> chProtocolMap = ProtocolUtil.getChProtocolMap(protocolAll);

                            chProtocolMap.forEach((x, y) -> {
                                proChMap.put(x, y);
                            });

                            broadcastProCh.unpersist();
                            broadcastProCh = getSparkContext(sparkContext).broadcast(proChMap);
                            ProtocolUtil.updateProOK(k);
                            LOGGER.error(k + " 协议更新成功...");
                            RedisUtil.hset(PROTOCOL, "change", "0");
                            RedisUtil.hset(PROTOCOL, k, "0");
                        } catch (Exception e) {
                            LOGGER.error(k + " 协议更新失败!!");
                            ProtocolUtil.updateProNO(k);
                        }

                    }
                });
            }
        }
    }

    /**
     * 广播告警map
     *
     * @param sparkContext
     */
    private void broadWarn(SparkContext sparkContext) {
        //异常广播
        Map<Integer, WarningProtocolBean> warnMap = new HashMap<>();
        //将异常报警实体广播
        broadcastVar.getValue().forEach((key, value) -> {
            for (WarningProtocolBean w : value.getWarningProtocol()) {
                warnMap.put(w.getSerialNumber(), w);
            }
        });
    }


    /**
     * 更新协议
     *
     * @param sparkContext
     * @return
     */
    public Broadcast<Map<String, MessageProtocol>> updatePro(SparkContext sparkContext) {
        doUpdate(sparkContext);
        return broadcastVar;
    }

    /**
     * 更新协议
     *
     * @param sparkContext
     * @return
     */
    public Broadcast<Map<String, String>> updateChPro(SparkContext sparkContext) {
        doUpdate(sparkContext);
        return broadcastProCh;
    }

}