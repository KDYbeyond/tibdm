package com.thit.tibdm.sparkstream;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.NettyTransmission.ProtocolAnalysis.WarningRules;
import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.bom.entity.Variable;
import com.thit.tibdm.bom.entity.WarningProtocolBean;
import com.thit.tibdm.db.bridge.util.CassandraV2Table;
import com.thit.tibdm.imdb.bridge.util.RedisUtil;
import com.thit.tibdm.mq.bridge.KafkaConstant;
import com.thit.tibdm.sparkstream.util.BroadcastWrapper;
import com.thit.tibdm.sparkstream.util.ParseUtil;
import com.thit.tibdm.util.ExceptionUtil;
import com.thit.tibdm.util.HttpClientKeepSession;
import com.thit.tibdm.util.ResourceUtil;
import kafka.serializer.DefaultDecoder;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import scala.Tuple2;

import java.util.*;

/**
 * 将所有的任务聚合到一起
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-06-19 13:35
 **/
public class MergetJob {
    /**
     * 日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(MergetJob.class);

    /**
     * @param args 参数
     * @throws InterruptedException 异常
     */
    public static void main(String[] args) throws InterruptedException {
        //协议的集合
        SparkConf sparkConf = new SparkConf().setAppName("Merget");
        // 1秒钟计算一次
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(1000));

        /**
         * 广播变量对所有的运算节点广播List
         */
        //跟分区数量相等
        String topicparse = ResourceUtil.getProValueByNameAndKey(KafkaConstant.FILE_NAME, KafkaConstant.TOPIC1);

        /**
         * 从kafka里面读取数据
         */

        String group = ResourceUtil.getProValueByNameAndKey(KafkaConstant.FILE_NAME, "group_parse");

        Map<String, String> map = new HashMap<>();
        map.put("metadata.broker.list",
                ResourceUtil.getProValueByNameAndKey(KafkaConstant.FILE_NAME, KafkaConstant.KAFKA_IP));
        map.put("group.id", group);

        Set<String> topicsSet = new HashSet<>(Arrays.asList(topicparse.split(",")));

        JavaPairInputDStream<String, byte[]> kafkaBytes = KafkaUtils
                .createDirectStream(jssc, String.class, byte[].class, StringDecoder.class, DefaultDecoder.class, map,
                        topicsSet);

        /**
         * 转变成spark straming的形式
         *
         * 将数据解析为map形式
         * 同时过滤掉协议中不存在的数据
         * (!(f == null || f.size() == 0))
         * f == null || f.size() == 0 则为车号在映射关系中不存在，然后取反，表示过滤掉
         */

        JavaDStream<Map> lines = kafkaBytes.transform(v1 -> {


            Broadcast<Map<String, MessageProtocol>> broProMap1 = BroadcastWrapper.getInstance().updatePro(jssc.sparkContext().sc());
            Broadcast<Map<String, String>> broProChMap1 = BroadcastWrapper.getInstance().updateChPro(jssc.sparkContext().sc());

            return v1.map(msg -> ParseUtil.parseMsgMap(msg._2(), broProMap1.getValue(), broProChMap1.getValue())).filter(f -> (!(f == null || f.size() == 0)));
        });

//        lines.count().print();
        /**
         * 将MAP转化为 车号-MAP(map里面的参数为纯的参数)
         */
        JavaPairDStream<String, Map> pairs =
                lines.mapToPair(m -> {
                    Map json = (Map) m.get("JSON");
                    return new Tuple2<>(json.get("CH").toString(), json);
                });

        //=======================================================//
        /**
         * 全量数据保存
         */

//        JavaDStream<String> all = kafkaBytes.transform(v1 -> {
//
//
//            Broadcast<Map<String, MessageProtocol>> broProMap1 = BroadcastWrapper.getInstance().updatePro(jssc.sparkContext().sc());
//            Broadcast<Map<String, String>> broProChMap1 = BroadcastWrapper.getInstance().updateChPro(jssc.sparkContext().sc());
//
//            return v1.map(msg -> ParseUtil.parseMsgAll(msg._2(), broProMap1.getValue(), broProChMap1.getValue())).filter(f -> (!(f == null || f.equals(""))));
//        });
//
//
//        all.foreachRDD(stringMapJavaPairRDD -> stringMapJavaPairRDD.foreachPartition(s->{
//
//            while (s.hasNext()){
//
//                String next = s.next();
//                ReduceUtil.save2BigDB(ResourceUtil.getProValueByNameAndKey("cassandra-db","tableall"),next);
//            }
//
//        }));

        //=======================================================//

        /**
         * 按照车号分组
         */
        JavaPairDStream<String, Iterable<Map>> pairDStream = pairs.groupByKey();

        pairDStream.cache();

        //=============================================================//
        //存储redis
        /**
         * 将数据转化为车号-单个MAP(MAP为时间最大的一个)
         */
        lines = pairDStream.map(p -> getMap(p));

        /**
         * 将得到的这个车号最大的MAP存放到REDIS中去
         */
        lines.foreachRDD(rdd -> rdd.foreachPartition(MergetJob::saveToRedis));

        //=============================================================//
        /**
         * 报警的,
         * 获取自定义的预警--对应的车号的pair
         */
        JavaPairDStream<WarningProtocolBean, Iterable<Map>> rulesData = pairDStream.transformToPair(v1 -> {

            Broadcast<Map<String, MessageProtocol>> broProMap1 = BroadcastWrapper.getInstance().updatePro(jssc.sparkContext().sc());
            Broadcast<Map<String, String>> broProChMap1 = BroadcastWrapper.getInstance().updateChPro(jssc.sparkContext().sc());
            return v1.flatMapToPair(jpd -> {
                List<Tuple2<WarningProtocolBean, Iterable<Map>>> tuples = new ArrayList<>();
                /**
                 * 获取车对应的协议的编号
                 */
                String proid = broProChMap1.getValue().get(jpd._1());
                /**
                 * 根据协议编号获取报警规则
                 */
                MessageProtocol messageProtocol = broProMap1.getValue().get(proid);

                List<WarningProtocolBean> maps = messageProtocol.getWarningProtocol();
                if (maps != null && maps.size() != 0) {
                    maps.forEach(m -> {
                        Tuple2<WarningProtocolBean, Iterable<Map>> mapIterableTuple2 = new Tuple2<>(m, jpd._2());
                        tuples.add(mapIterableTuple2);
                    });
                }
                return tuples.iterator();

            }).filter(f -> {
                if (f._1().getTimeDuration() == 0 || f._1().getTimespan() == 0 || f._1().getFrequency() == 0) {
                    return false;
                } else {
                    return true;
                }
            });
        });

        /**
         * 将报警ID和车号是否进行报警转换：
         * 报警ID：车号-true|false
         * 报警ID：车号-0，1，2 2为不需要改变数据，0，1分别为改变为故障或者非故障
         */
        JavaPairDStream<Integer, Tuple2<String, Integer>> warn = rulesData.mapToPair(r -> {
            //大数据代码
            int warnID = r._1().getSerialNumber();
            //车号
            String ch = "";
            //故障解除还是发生
            Integer isWarn = 0;
            //报警规则
            WarningProtocolBean warningProtocolBean = r._1();
            HashSet<Boolean> booleanSet = new HashSet<>();
            //从协议中获取需要使用的参数
            List<String> para = warningProtocolBean.getParam();
            String jexl = warningProtocolBean.getJexl();
            //存放参数的map
            Map<String, String> pmap = new HashMap<>();
            //这个规则对应的全部数据
            Iterator<Map> iterator = r._2().iterator();

            //查询上一次的状态
//            Jedis jedis = RedisUtil.getJedisConnect();
//            String lastWarn = null;

            //循环所有,判断是否都是故障数据
            while (iterator.hasNext()) {
                Map next = iterator.next();
                if (ch.equals("")) {
                    ch = next.get("CH").toString();
                }
                //获取需要计算的数据
                para.forEach(p -> pmap.put(p, (String) next.get(p)));
                //计算出来的结果
                boolean b = WarningRules.convertToCode(jexl, pmap, ch);

                /**
                 * 如果上一次为0，那么这一次只要第一条数据为0，那么后面的就不需要做判断，因为后面要么全为0，既全部数据相同
                 * 或者又有0又有1。这两种情况呢恰巧都不需要做数据的改变。
                 * 上一次为1同理
                 */

                //将计算结果添加进去
                booleanSet.add(b);
                //判断这个set里面是如何的一种结果，如果全部为异常消除，或者全部为异常发生，那么就会发生变化剩下的不做处理跳出
                if (booleanSet.contains(true) && booleanSet.contains(false)) {
                    //包含两种情况则直接跳出，不往下做判断
                    isWarn = 2;
                    break;
                }
            }

            if (booleanSet.size() == 1) {
                Boolean next = booleanSet.iterator().next();
                if (next) {
                    //全部是故障
                    isWarn = 1;
                } else {
                    isWarn = 0;
                }
            }
            return new Tuple2<>(warnID, new Tuple2<>(ch, isWarn));
        });

        warn.foreachRDD(stringJavaRDD -> stringJavaRDD.foreachPartition(partitionOfRecords -> {
            ArrayList<Map<String, String>> list = new ArrayList<>();
            while (partitionOfRecords.hasNext()) {
                Tuple2<Integer, Tuple2<String, Integer>> f = partitionOfRecords.next();
                int isWarn = f._2()._2();
                //全部都是故障
                //报警ID
                Integer integer = f._1();
                //报警的数据
                String ch = f._2()._1();
                Map<String, String> pmap = new HashMap();
                pmap.put("number", "" + integer);
                pmap.put("ch", ch);
                pmap.put("isWarn", "" + isWarn);
                list.add(pmap);
            }
            if (list.size() > 0) {
//                LOGGER.error(list.toString());
                RedisUtil.pub("warn_status", JSON.toJSONString(list));
            }
        }));
        /**
         * 告警结束
         */
        //=============================================================//
        //自定义时间，
//        int batchGz = Integer.parseInt(ResourceUtil.getProValueByNameAndKey("kafka-mq", "batchGz"));
        /**
         * 故障持续了6秒才算做故障
         */
//        JavaPairDStream<String, Iterable<Map>> faultWindow = pairDStream.window(Durations.seconds(batchGz), Durations.seconds(batchGz));
        /**
         * 计算故障
         * 得到车号
         * 其中的0表示不需要报警，1表示需要报警，2表示不需要做出改变
         * 300：{G1:0,G2:1,G3:2}
         */

        JavaPairDStream<String, Map<String, String>> fault = pairDStream.transformToPair(v1 -> {

            Broadcast<Map<String, MessageProtocol>> broProMap1 = BroadcastWrapper.getInstance().updatePro(jssc.sparkContext().sc());
            Broadcast<Map<String, String>> broProChMap1 = BroadcastWrapper.getInstance().updateChPro(jssc.sparkContext().sc());


            return v1.mapToPair(r -> {

                /**r
                 * 获取车对应的协议的编号
                 */
                String proid = broProChMap1.getValue().get(r._1());

                /**
                 * 上一次的状态列表
                 * 有可能为空
                 */
                Map<String, String> status = getStatus(r._1());
                /**
                 * 根据协议编号获取报警属性
                 */
                MessageProtocol messageProtocol = broProMap1.getValue().get(proid);
                List<Variable> list = messageProtocol.getVariable();

                Map<String, String> result = new HashMap<>();
                /**
                 * 对每一个故障来进行判断最后得出一个结果
                 */
                list.forEach(l -> {
                    //数据列表
                    Iterator<Map> iterator = r._2().iterator();
                    //是否是故障属性
                    String type = l.getType();
                    if (type.equals("TROUBLE")) {
                        while (iterator.hasNext()) {
                            Map<String, String> next = iterator.next();
                            HashSet<Boolean> booleanSet = new HashSet<>();
                            //获取上一次的状态
                            String lastStatus = status.get(l.getUniqueCode());
                            String statusThis = next.get(l.getUniqueCode());
                            if (statusThis.equals("-1")) {
                                break;
                            }
                            if (statusThis.equals("0")) {
                                booleanSet.add(false);
                            } else if (statusThis.equals("1")) {
                                booleanSet.add(true);
                            } else {
                                break;
                            }
                            if (lastStatus != null && (!lastStatus.equals(""))) {
                                //上次的状态不为空并且和这次的数据相同则跳出不修改结果
                                if (lastStatus.equals(statusThis)) {
                                    break;
                                }
                                if (booleanSet.contains(true) && booleanSet.contains(false)) {
                                    //包含两种情况则直接跳出，不往下做判断,不需要做出改变
                                    break;
                                }
                            } else {
                                //如果是第一次的话，只要其中有一个0就直接跳出
                                if (booleanSet.contains(false)) {
                                    result.put(l.getUniqueCode(), "0");
                                    break;
                                }

                            }
                            if (booleanSet.iterator().next()) {
                                result.put(l.getUniqueCode(), "1");
                            } else {
                                result.put(l.getUniqueCode(), "0");
                            }
                        }
                    }
                });
                return new Tuple2<>(r._1(), result);
            });
        });

//        /**
//         * 合并起来
//         */
//        fault = fault.reduceByKey((m1, m2) -> {
//            Map<String, String> result = new HashMap<>();
//            //随便拿一个map就可以，一个是故障需要再所有的记录里面均存在那么才为一次故障
//            m1.forEach((k, v) -> {
//                //m1.m2均存在这个情况
//                if (m2.containsKey(k)) {
//                    String s = m2.get(k);
//                    if (s.equals(v)) {
//                        result.put(k, v);
//                    }
//                }
//            });
//            return result;
//        });

        /**
         * 将得出的数据放入redis中去
         */
        fault.foreachRDD(stringJavaRDD -> stringJavaRDD.foreachPartition(partitionOfRecords -> {
            List<Map<String, String>> faultList = new ArrayList();
            Jedis jedis = RedisUtil.getJedisConnect();
            Map<String, Map<String, HashSet<Boolean>>> count = new HashMap<>();

            while (partitionOfRecords.hasNext()) {
                //车号--对应的需要修改的map对
                Tuple2<String, Map<String, String>> next = partitionOfRecords.next();
                Map<String, String> map1 = next._2();
                String ch = next._1();
                //如果结果集里面有数据才会进行下面的操作
                if (map1.size() != 0) {
                    //将这个车的所有信息存入库中
                    Boolean exists = jedis.exists(ch);
                    //假设存在的话就解除报警，或者报警都通知
                    if (exists) {
                        map1.forEach((k, v) -> {
                            Map<String, String> m = getMap(ch, k, v);
                            faultList.add(m);
                        });
                    } else {
                        //不存在的话，如果是故障的话是需要报警的
                        map1.forEach((k, v) -> {
                            if (!v.equals("0")) {
                                Map<String, String> m = getMap(ch, k, v);
                                faultList.add(m);
                            }
                        });
                    }
                    String result = JSON.toJSONString(faultList);
                    try {
                        HttpClientKeepSession.pushMsg("TROUBLE", result);
                        LOGGER.error("调故障接口result==" + result);
                    } catch (Exception e) {
                        LOGGER.error("发送接口出现错误！！！" + "HttpClientKeepSession.pushMsg()");
                        LOGGER.error("发送的数据是" + result);
                    }
                    jedis.hmset(ch, map1);
                }
            }
            jedis.close();
        }));
        /**
         * 故障结束
         */
        //=============================================================//

        jssc.start();
        jssc.awaitTermination();

    }

    /**
     * 存储redis
     *
     * @param partitionOfRecords 分片之后的数据
     */
    private static void saveToRedis(Iterator<Map> partitionOfRecords) {
        Jedis jedis = RedisUtil.getJedisConnect();
        String table = ResourceUtil.getProValueByNameAndKey("redis-db", "tablerealtime");
        Map<String, String> last = new HashMap<>();
        while (partitionOfRecords.hasNext()) {
            Map<String, String> next = partitionOfRecords.next();
            if (last.size() == 0) {
                String hget = jedis.hget(table, next.get("CH") + "-" + next.get(CassandraV2Table.collect_type));
                if (hget != null && !hget.equals("")) {
                    last = (Map<String, String>) JSON.parse(hget);
                } else {
                    last.put(CassandraV2Table.collect_time, "0");
                }
            }
            if (Long.parseLong(next.get(CassandraV2Table.collect_time)) > Long.parseLong(last.get(CassandraV2Table.collect_time))) {
                try {
                    jedis.hset(table, next.get("CH") + "-" +
                            next.get(CassandraV2Table.collect_type), JSON.toJSONString(next));
                } catch (Exception e) {
                    LOGGER.error(ExceptionUtil.getMsgExce(e));
                    e.printStackTrace();
                }
            }
        }
        jedis.close();
    }

    /**
     * 获取时间最大的一条数据
     *
     * @param p 车号对应的数据集合
     * @return 时间最大的数据
     */
    private static Map getMap(Tuple2<String, Iterable<Map>> p) {
        Map maxTime = new HashMap();
        Map tmp = new HashMap();
        tmp.put(CassandraV2Table.collect_time, 0);
        for (Map pair : p._2()) {
            long l = Long.parseLong(pair.get(CassandraV2Table.collect_type).toString());
            if (maxTime.containsKey(CassandraV2Table.collect_time)) {
                String collectTime = maxTime.get(CassandraV2Table.collect_time).toString();
                long l1 = Long.parseLong(collectTime);
                if (l > l1) {
                    maxTime = pair;
                }
            } else {
                maxTime = pair;
            }
        }
        return maxTime;
    }

    /**
     * 转型
     *
     * @param ch 车号
     * @param k  key
     * @param v  value
     * @return 返回格式化后的数据
     */
    private static Map<String, String> getMap(String ch, String k, String v) {
        Map<String, String> m = new HashMap<>();
        m.put("BOM_TRAINNUMBER", ch);
        m.put("BOM_CODE", k);
        m.put("BOM_HANDLE", v);
        return m;
    }

    /**
     * 获取状态
     *
     * @param ch 车号
     * @return 返回状态集合
     */
    public static Map<String, String> getStatus(String ch) {
        Jedis jedis = RedisUtil.getJedisConnect();
        Map<String, String> map = jedis.hgetAll(ch);
        if (map == null | map.size() == 0) {
            map = new HashMap<>();
        }
        jedis.close();
        return map;
    }

}
