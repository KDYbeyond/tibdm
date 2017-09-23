package com.thit.tibdm.server;

import com.alibaba.fastjson.JSON;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.bom.entity.Variable;
import com.thit.tibdm.bom.entity.WarningProtocolBean;
import com.thit.tibdm.db.bridge.util.CassandraSingleConnect;
import com.thit.tibdm.imdb.bridge.util.RedisUtil;
import com.thit.tibdm.sparkstream.util.ProtocolUtil;
import com.thit.tibdm.util.HttpClientKeepSession;
import com.thit.tibdm.util.ProtocolConstants;
import com.thit.tibdm.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计警告
 * <p>
 * 主要通过redis的发布订阅来完成
 *
 * @author wanghaoqiang
 * @version 1.0
 * @time 2017-07-31 15:17
 **/
public class StatisticsWarn {
    /**
     * 前缀
     */
    public static final String PREFIX = "shanghai";
    /**
     * 分割符
     */
    public static final String SPLIT = "_";
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsWarn.class);
    /**
     * 更新通道
     */
    private static final String UPDATE_CHANNEL = "update_status";
    /**
     * 告警通道
     */
    private static final String STATUS_CHANNEL = "warn_status";
    /**
     * 协议map
     */
    private static Map<String, MessageProtocol> proMap = new HashMap<>();
    /**
     * 异常map
     */
    private static Map<String, WarningProtocolBean> warnMap = new HashMap<>();
    /**
     * 车号对应的协议map
     */
    private static Map<String, String> proChMap = new HashMap<>();
    /**
     * 最大保留天数
     */
    private static Integer MAXCOUNT = 100;
    /**
     * 历史数据表
     */
    private static Map<String, List<Integer>> HIS_LIST = new HashMap<>();


    /**
     * 启动两个订阅，更新协议
     */
    public static void init() {
        LOGGER.info("历史数据表：" + HIS_LIST.toString());
        LOGGER.info("===载入协议更新模块===");
        loadProInfo("");
        new Thread(() -> {
            JedisPubSub sub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    loadProInfo(message);
                    List<String> tableNames=getTableNames();
                    for (String table:tableNames) {
                        updateProtocol(table);
                    }
                }
            };
            RedisUtil.sub(sub, UPDATE_CHANNEL);
        }).start();

        new Thread(() -> {
            JedisPubSub sub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                   String keys=ProtocolUtil.getReduceKeys(ResourceUtil.getProValueByNameAndKey("cassandra-db","reducetable"));
                   LOGGER.trace(message);
                    if (message.equals(ProtocolConstants.UPDATE_PRO)){
                        LOGGER.error("降频降频开始了");
                        LOGGER.trace("降频更新模块.....");
                    }
                    RedisUtil.hset(ProtocolConstants.REDUCE_TABLE,ProtocolConstants.REDUCE_KEYS,keys);
                }
            };
            RedisUtil.sub(sub, UPDATE_CHANNEL);
        }).start();
        LOGGER.info("===开始载入异常数据接收模块===");
        //往list里面增加状态
        new Thread(() -> RedisUtil.sub(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                execWarn(message);
            }

            private void execWarn(String message) {
                List<Map<String, String>> collect = (List<Map<String, String>>) JSON.parse(message);
                for (int i = collect.size() - 1; i >= 0; i--) {
                    Map<String, String> parse = collect.get(i);
                    String number = parse.get("number");
                    String ch = parse.get("ch");
                    String isWarn = parse.get("isWarn");
                    List<Integer> list = new ArrayList<>();
                    if (HIS_LIST.containsKey(number + SPLIT + ch)) {
                        list = HIS_LIST.get(number + SPLIT + ch);
                    }
                    if (list.size() > 1000) {
                        list = list.subList(900, list.size() - 1);
                    }
                    //将数据放入其中
                    WarningProtocolBean bean = warnMap.get(number);
                    //持续时间
                    int timeDuration = bean.getTimeDuration();
                    //时间跨度多少秒
                    int timespan = bean.getTimespan();

                    int times = bean.getFrequency();
                    LOGGER.debug("接收到的信息：" + message);
                    if (timespan != 0 && timeDuration != 0 && times != 0) {

                        //发生次数
                        //增长记录数量
                        Integer last = 0;
                        if (list.size() != 0) {
                            last = list.get(list.size() - 1);
                        }
                        if (isWarn.equals("2")) {
                            list.add(last);
                        } else {
                            list.add(Integer.parseInt(isWarn));
                        }
                        LOGGER.debug("结果列表：" + list.toString());
                        HIS_LIST.put(number + SPLIT + ch, list);
                        Integer sum = -1;
                        if (list.size() >= timeDuration) {
                            sum = list.subList(list.size() - timeDuration, list.size()).stream().reduce(0, (x, y) -> x + y);
                        }
                        LOGGER.debug("最后N个的和：" + sum);
                        //发生异常的持续次数和设置的持续时间一样多，则可能为异常
                        /**
                         * 判断上一次的的状态，
                         * 上次为0非故障，那么这次就是故障，上次为故障，这次还为故障，就不算作一次
                         * 9019_336 为上次的状态
                         * 为null 或者0 表示上次无故障
                         *
                         * 最后发生次数
                         * 存储redis key:[时间]
                         */
                        String hisMem = PREFIX + SPLIT + number + SPLIT + ch + SPLIT + "HIS";
                        String lastMem = PREFIX + SPLIT + number + SPLIT + ch + SPLIT + "LAST";

                        //获取上一次的状态
                        String lastStatus = RedisUtil.get(lastMem);

                        //当第一次相等时候
                        LOGGER.debug("上一次的状态：" + lastStatus);
                        LOGGER.debug("sum" + sum);
                        if (lastStatus == null) {
                            LOGGER.debug("初始化状态");
                            RedisUtil.set(lastMem, "0");
                            lastStatus = "0";
                        }
                        if (sum == timeDuration) {
                            if (lastStatus.equals("0")) {
                                //将上一次的状态设置为发生
                                RedisUtil.set(lastMem, "1");
                                LOGGER.debug("========故障发生一次！！！" + number + "==" + ch + "==");
                                //增加一次数量
                                RedisUtil.zadd(hisMem, System.currentTimeMillis(), System.currentTimeMillis() + "");
                                long now = System.currentTimeMillis();
                                long zcount = RedisUtil.zcount(hisMem, now - timespan * 1000, now);
                                LOGGER.debug("总共发生的次数：" + zcount);
                                if (zcount > times) {
                                    LOGGER.info("发送一次请求");
                                    //将发送请求添加进去
                                    List<Map<String, String>> rlist = new ArrayList();
                                    HashMap<String, String> result = new HashMap<>();
                                    result.put("BOM_CODE", number);
                                    result.put("BOM_TRAINNUMBER", ch);
                                    result.put("BOM_HANDLE", "1");
                                    rlist.add(result);
                                    try {
                                        HttpClientKeepSession.pushMsg("WARN", JSON.toJSONString(rlist));
                                    } catch (Exception e) {
                                        LOGGER.error("出现错误了");
                                    }
                                    //发送一次告警同时检查redis过多的话需要删除
                                    if (zcount > 100000) {
                                        LOGGER.debug("需要清除数据，无用数据过多");
                                        RedisUtil.zremrangeByScore(hisMem, 0, now - MAXCOUNT * 1000);
                                    }
                                }
                            }
                        } else if (sum == 0) {
                            if (lastStatus.equals("1")) {
                                LOGGER.info("故障解除一次！！！" + number);
                                RedisUtil.set(lastMem, "0");
                            }
                        } else {
                            LOGGER.debug("不发生改变");
                        }

                        LOGGER.debug(HIS_LIST.toString());
                    }
                }
            }


        }, STATUS_CHANNEL)).start();
    }

    /**
     * 载入协议信息
     *
     * @param proId 根据协议ID
     */
    private static synchronized void loadProInfo(String proId) {
        LOGGER.info("===开始载入协议===");
        Map<String, Object> protocolAll = ProtocolUtil.getProtocolAll(proId);
        proMap.putAll(ProtocolUtil.getProtocolMap(protocolAll));
        proChMap.putAll(ProtocolUtil.getChProtocolMap(protocolAll));
        LOGGER.info(JSON.toJSONString(proChMap));
        //将数据映射为9018->异常实体类
        proMap.keySet().forEach(p -> proMap.get(p).getWarningProtocol().forEach(w -> {
            if (w.getTimespan() > MAXCOUNT) {
                MAXCOUNT = w.getTimespan();
            }
            warnMap.put(w.getSerialNumber() + "", w);
        }));
        LOGGER.info("载入协议：" + warnMap + "");
    }

    /**
     * @param tableName 要进行更新的表名
     *                  表更新
     * @author kuangdongyang
     */
    public static void updateProtocol(String tableName) {
        String cql_query = "select * from " + tableName + " limit 1";
        List<String> listProtocol = new ArrayList<String>();
        ResultSet resultSet = CassandraSingleConnect.INSTANCE.getInstance().execute(cql_query);
        LOGGER.info("当前表的字段");
        ColumnDefinitions columnDefinitions = resultSet.getColumnDefinitions();
        for (ColumnDefinitions.Definition column : columnDefinitions) {
            System.out.println(column.getName());
            listProtocol.add(column.getName());
        }
        Map<String, Object> map = ProtocolUtil.getProtocolAll("");
        Map<String, MessageProtocol> messageProtocolMap = ProtocolUtil.getProtocolMap(map);
        LOGGER.info("variable中的协议");
        for (Map.Entry<String, MessageProtocol> entry : messageProtocolMap.entrySet()) {
            MessageProtocol messageProtocol = entry.getValue();
            List<Variable> variables = messageProtocol.getVariable();
            for (Variable variable : variables) {
                System.out.println(variable.getUniqueCode().toLowerCase());
                boolean isContains = listProtocol.contains(variable.getUniqueCode().toLowerCase());
                if (!isContains) {
                    String add_column_sql = "ALTER TABLE " + tableName + " ADD " + variable.getUniqueCode().toLowerCase() + " text";
                    CassandraSingleConnect.INSTANCE.getInstance().execute(add_column_sql);
                }
            }
        }
    }
    /**
     * 获取要更新的表名
     */
    public static List<String> getTableNames(){
        List<String> tableLists = new ArrayList<String>();
        String tables=ResourceUtil.getCassandraProValue("tableName");
        String keySpace=ResourceUtil.getCassandraProValue("keySpaceName");
        if (tables!=null){
           String[] tableStrArray=tables.split(",");
            for (String str:tableStrArray) {
                tableLists.add(keySpace+"."+str);
                LOGGER.info(str);
            }
        }
        return tableLists;
    }
}
