package com.thit.tibdm.calculation.job;

import com.thit.tibdm.calculation.CassandraUtil;
import com.thit.tibdm.calculation.DBConnection;
import com.thit.tibdm.calculation.stru.CollectInfo;
import com.thit.tibdm.calculation.stru.ResultEveryDay;
import com.thit.tibdm.calculation.util.CalculationUtil;
import com.thit.tibdm.util.Constants;
import com.thit.tibdm.util.ResourceUtil;
import com.xicrm.common.TXISystem;
import org.apache.commons.lang.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.*;


/**
 * Created by wanghaoqiang on 2016/12/12.
 */
public class CalculationTimer implements Job {
    /**
     * 初始化统计任务,统计最大最小平均等
     * <p>
     * 所有的tagname都缩短成15分钟计算一次
     */
    public static void execTimer() {

        //查询所有的tagname的值
        List<String> tagNameList = DBConnection.getTagNameList();
//        List<String> tagNameList = new ArrayList<>();
//        tagNameList.add("DY_GYPD_K.M101_110KV1.SW_EP1_1003_ED");
        int countTagName = tagNameList.size();
        int countLeave = countTagName;
        long earliest = 0;
        for (String tagName : tagNameList) {
            //判断最新的统计数据是哪天的
            ResultEveryDay latest = CassandraUtil.getLatestByTagName(tagName);
            if (latest != null) {//没有统计过的数据
                earliest = latest.getTime();
            }


            //判断是否需要再去计算
            //计算昨天应该计算的时间
            long l = Long.parseLong(CalculationUtil.getTimeStamp(System.currentTimeMillis() + "", "yyyy-MM-dd"));
            //计算出今天的12点五分应该是啥时间
            long time1 = DateUtils.addMinutes(new Date(l), 5).getTime();


            if (earliest < time1) {
                //统计的最后一天的时间小于昨天,那么是需要计算的
                System.out.println("正在计算:" + tagName + "的所有数据");
                //查询所有的数据根据key
                Map<Long, ResultEveryDay> map = new HashMap<>();
                List<CollectInfo> allDataByKey;
                try {
                    allDataByKey = CassandraUtil.getDataByTime(ResourceUtil.getCassandraProValue(Constants.TABLE_DATA), tagName, earliest, l);
//                    allDataByKey = CassandraUtil.getDataByTime(ResourceUtil.getCassandraProValue(Constants.TABLE_DATA), tagName, 1483545600000L, 14835542400000L);
                } catch (Exception e) {
                    System.out.println(tagName + "查询失败");
                    continue;
                }
                Map<String, ResultEveryDay> group = new HashMap<>();
                for (CollectInfo info : allDataByKey) {
                    //获取这个数据的时间属于那个阶段
                    long start = Long.parseLong(CalculationUtil.getTimeStamp(info.getColumn1() + "", "yyyy-MM-dd"));
                    long end = DateUtils.addDays(new Date(start), 1).getTime();
                    long time = DateUtils.addMinutes(new Date(end), 5).getTime();
                    ResultEveryDay rs = new ResultEveryDay();
                    rs.setTagName(tagName);
                    rs.setObjectName(ResourceUtil.getCassandraProValue(Constants.TABLE_DATA));
                    rs.setTime(time);

                    //判断是否匹配配置文件中得正则表达式
                    List<String> list = CalculationUtil.regularList();
                    boolean regular = CalculationUtil.regular(list, tagName);

                    double v = 0;
                    try {
                        v = Double.parseDouble(info.getValue());

                        if (v != 0) {
                            long insertTime = info.getColumn1();
                            if (!map.containsKey(time)) {
                                //如果以前没有计算过,那么先计算
                                //匹配的话需要使用新的算法
                                if (regular) {
                                    //获取分组结果
                                    group = CalculationUtil.getGroup(group, info);
                                    //计算出今天的结果
                                    rs = CalculationUtil.getResultByDiff(info, rs, group);
                                } else {
                                    rs = CalculationUtil.getResult(v, rs, insertTime);
                                }
                            } else {
                                rs = map.get(time);
                                if (regular) {
                                    //获取分组结果
                                    group = CalculationUtil.getGroup(group, info);
                                    //计算出今天的结果
                                    rs = CalculationUtil.getResultByDiff(info, rs, group);
                                } else {
                                    //如果以前计算过,那么先拿出原来的,再进行计算
                                    rs = CalculationUtil.getResult(v, rs, insertTime);
                                }
                            }
                            rs=CalculationUtil.getZeroNumber(rs);
                            map.put(time, rs);
                        } else {
                            //如果出现解析异常,证明是非法数组,如果已经存在统计结果,那么跳过,如果不存在则初始化
                            System.out.println(info.toString());
                            if (!map.containsKey(time)) {
                                //如果以前没有计算过,那么先计算
                                rs.setSum("0");
                                rs.setCount("0");
                                rs.setAverage("0");
                                rs.setMax("0");
                                rs.setMax("0");
                                rs.setMin("0");
                                rs.setMaxTime(0);
                                rs.setMinTime(0);
                                map.put(time, rs);
                            }
                        }
                    } catch (Exception e) {
                        //如果出现解析异常,证明是非法数组,如果已经存在统计结果,那么跳过,如果不存在则初始化
                        if (!map.containsKey(time)) {
                            //如果以前没有计算过,那么先计算
                            rs.setSum("0");
                            rs.setCount("0");
                            rs.setAverage("0");
                            rs.setMax("0");
                            rs.setMax("0");
                            rs.setMin("0");
                            rs.setMaxTime(0);
                            rs.setMinTime(0);
                            map.put(time, rs);
                        }
                        continue;
                    }


                }
                //将计算完的数据保存下来
                int count = 0;
                for (long time : map.keySet()) {
                    count++;
                    CassandraUtil.saveResult(map.get(time));
                }
                System.out.println("计算完毕:" + tagName + "的所有数据,总共" + count + "天的数据计算完毕");
                countLeave--;
                System.out.println("还剩余" + countLeave + "个tagName未计算。共" + countTagName + "个");
            }
        }

//
    }

    /**
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        TXISystem.start();
        execTimer();
//        calculationLastDay();
//        String s="select * from " + TXISystem.config.getProperty(TXIBigDBV1Names.TIBDM_Obj_Name) + " where key='TY_BGQ_DM.ZP_TDM15.SW_DL9217_EL'";
//        System.out.println(s);
//        int k = 0;
//        for (int i = 0; i < 10; i++) {
//            try {
//                int n = 0;
//                n = 1 / n;
//            } catch (Exception e) {
//                System.out.println("出现异常");
//            }
//            System.out.println("继续运行" + k++);
//        }

    }

    /**
     * 计算十五分钟的差值,输入开始时间和结束时间
     * <p>
     * <p>
     * 应该可以传输吧?传入参数为tagname,
     * @param endTime 结束时间
     * @param startTime 开始时间
     * @param tagName 标签
     * @return calculationInfo 返回
     */

    public static ResultEveryDay calculationFifteenMins(String tagName, long startTime, long endTime) {

        //根据tagName查询出所有的信息
        List<CollectInfo> list = new ArrayList<>();
        //当前时间
//        long timeNow =System.currentTimeMillis();
        try {
            list = CassandraUtil.getDataByTime(ResourceUtil.getCassandraProValue(Constants.TABLE_DATA), tagName, startTime, endTime);
        } catch (Exception e) {
            System.out.println("查询失败");
        }

        Map<String, List<CollectInfo>> stepMap = new HashMap<>();
        //根据start 把数据进行分组
        list.forEach(info -> {
            //过滤非法数据0
            if (!info.getValue().equals("0")) {
                List<CollectInfo> infoList = new ArrayList<CollectInfo>();
                long time = info.getColumn1();
                String start = CalculationUtil.getTimeStamp(time + "", "yyyy-MM-dd HH");
                if (stepMap.containsKey(start)) {
                    infoList = stepMap.get(start);
                }
                infoList.add(info);
                stepMap.put(start, infoList);
            }
        });
        //计算start对应的最大值最小值等信息
        List<ResultEveryDay> rsList = new ArrayList<>();
        stepMap.forEach((k, v) -> {
            ResultEveryDay calculationInfo = new ResultEveryDay();
            calculationInfo = CalculationUtil.getCalculationInfo(v, tagName, ResourceUtil.getCassandraProValue(Constants.TABLE_DATA), k);
            rsList.add(calculationInfo);

        });


        //计算差值的最大值最小值等信息
        List<CollectInfo> result = new ArrayList<>();
        rsList.forEach(rs -> {
            CollectInfo info = new CollectInfo();
            String diff = "";
            if (rs.getMaxTime() > rs.getMinTime()) {
                diff = (Double.parseDouble(rs.getMax()) - Double.parseDouble(rs.getMin())) + "";
            } else {
                diff = (Double.parseDouble(rs.getMin()) - Double.parseDouble(rs.getMax())) + "";
            }
            info.setValue(diff);
            info.setKey(rs.getTagName());
            info.setColumn1(rs.getTime());
            result.add(info);
        });
        //计算差值的最大值最小值
        ResultEveryDay calculationInfo = CalculationUtil.getCalculationInfo(result, tagName, ResourceUtil.getCassandraProValue(Constants.TABLE_DATA), endTime + "");

        return calculationInfo;
    }


    /**
     * 计算前一天的所有数据统计
     */
    public static void calculationLastDay() {
        //查询所有的tagname的值
        List<String> tagNameList = DBConnection.getTagNameList();
        long end = Long.parseLong(CalculationUtil.getTimeStamp(System.currentTimeMillis() + "", "yyyy-MM-dd"));
        long start = DateUtils.addDays(new Date(end), -1).getTime();
        long time = DateUtils.addMinutes(new Date(end), 5).getTime();
        for (String tagName : tagNameList) {
            //现在时间的开始时间
            List valueList = CalculationUtil.getValueList(ResourceUtil.getCassandraProValue(Constants.TABLE_DATA), tagName, start + "", end + "");

            //假设查询出来为0也应该计入算统计过了
            ResultEveryDay rs = CalculationUtil.getCalculationInfo(valueList, tagName, ResourceUtil.getCassandraProValue(Constants.TABLE_DATA), time + "");
            CassandraUtil.saveResult(rs);

        }
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("定时任务统计昨天的数据");
        calculationLastDay();
    }


}
