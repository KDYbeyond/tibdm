package com.thit.tibdm.calculation.util;

import com.thit.tibdm.calculation.CassandraUtil;
import com.thit.tibdm.calculation.DBConnection;
import com.thit.tibdm.calculation.stru.CollectInfo;
import com.thit.tibdm.calculation.stru.ResultEveryDay;
import com.thit.tibdm.util.ResourceUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wanghaoqiang on 2016/12/9.
 */
public class CalculationUtil {

    /**
     * 获取这段数据的平均值,最大值,最小值
     *
     * @param tagList 输入tagList[1,2,3,4,5,6,7,8,2,3,4,5]
     * @param tagName 标签
     * @param objectName 对象名
     * @param time 时间
     * @return map {"max":1,"min":2,"average":3,"count",99}
     *
     */
    public static ResultEveryDay getCalculationInfo(List<CollectInfo> tagList, String tagName, String objectName, String time) {
        double max = 0;
        double min = 0;
        double average;
        int count = 0;
        double sum = 0;
        long minTime = 0;
        long maxTime = 0;
        for (CollectInfo value : tagList) {
            double v;
            try {
                v = Double.parseDouble(value.getValue());
                long l = value.getColumn1();
                if (count == 0) {
                    max = v;
                    min = v;
                    maxTime = l;
                    minTime = l;
                } else {
                    if (v > max) {
                        max = v;
                        maxTime = l;
                    }
                    if (v < min) {
                        min = v;
                        minTime = l;
                    }
                }
                sum += v;
                count++;
            } catch (Exception e) {
                //如果数据是无效的,那么什么都不干
                continue;
            }

        }
        if (count == 0) {
            average = 0;
        } else {
            average = sum / count;
        }
        ResultEveryDay rs = new ResultEveryDay();
        rs.setTagName(tagName);
        rs.setObjectName(objectName);
        rs.setMax(max + "");
        rs.setMin(min + "");
        rs.setAverage(average + "");
        rs.setSum(sum + "");
        rs.setTime(Long.parseLong(time));
        rs.setCount(count + "");
        rs.setMaxTime(maxTime);
        rs.setMinTime(minTime);
        return rs;
    }

    /**
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        boolean matche = false;
        String reg = "_EH$";
        String value = "DY_GYPD_K.M102_110KV2.SW_EP1_356_EH";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            matche = true;
            break;
        }
        System.out.println(matche);
    }

    /**
     * 通过输入的tagName,起始时间和结束时间,object_name,来获取List数据
     *
     * @param tagName   采集点名字
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @param objectName 对象名
     * @return list 序列
     */
    public static List<CollectInfo> getValueList(String objectName, String tagName, String startTime, String endTime) {
        List<CollectInfo> list = CassandraUtil.getDataByTime(objectName, tagName, Long.parseLong(startTime), Long.parseLong(endTime));
        return list;
    }

    /**
     * 获取准确的起始时间和本日的起始时间
     *
     * @param time      最后的时间
     * @param valueType 年月日类型:1-日,2-月,3-年
     * @return map
     */
    public static Map getStartTime(String time, String valueType) {
        Map map = new HashMap();
        String startTime = null;
        String endTime;
        //100000  2010---1111
        if (valueType.equals("1")) {
            startTime = getTimeStamp(time, "yyyy-MM-dd");
        } else if (valueType.equals("2")) {
            startTime = getTimeStamp(time, "yyyy-MM");
        } else if (valueType.equals("3")) {
            startTime = getTimeStamp(time, "yyyy");
        }
        endTime = getTimeStamp(time, "yyyy-MM-dd");
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return map;
    }

    /**
     * 从cassandra查询之前的数据,获得之前的最大,最小平均等
     *
     * @param tagName   采集点名称
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @param objectName 对象名
     * @return countedData 计数
     */
    public static List<ResultEveryDay> getData4Db(String objectName, String tagName, String startTime, String endTime) {
        List<ResultEveryDay> countedData = CassandraUtil.getCountedData(objectName, tagName, Long.parseLong(startTime), Long.parseLong(endTime));
        return countedData;
    }

    /**
     * 获取最终的统计结果
     *
     * @param countedData 之前计算的结果+上今天的计算结果
     * @return map
     */
    public static Map getResultData(List<ResultEveryDay> countedData) {
        double max = Double.parseDouble(countedData.get(0).getMax());
        double min = Double.parseDouble(countedData.get(0).getMin());
        double average;
        int count = 0;
        double sum = 0;
        long maxTime = countedData.get(0).getMaxTime();
        long minTime = countedData.get(0).getMinTime();
        Map map = new HashMap();
        //将以前的数据全部循环
        for (ResultEveryDay rs : countedData) {
            count += Long.parseLong(rs.getCount());
            sum += Double.parseDouble(rs.getSum());
            double maxTmp = Double.parseDouble(rs.getMax());
            double minTmp = Double.parseDouble(rs.getMin());
            if (maxTmp > max) {
                max = maxTmp;
                maxTime = rs.getMaxTime();
            }
            if (minTmp < min) {
                min = minTmp;
                minTime = rs.getMinTime();
            }
        }
        if (count != 0) {
            average = sum / count;
        } else {
            average = 0;
        }
        map.put("max", getDoubleFormat(max));
        map.put("maxTime", maxTime);
        System.out.println("输出最小值" + min);
        map.put("min", getDoubleFormat(min));
        map.put("minTime", minTime);
        map.put("average", getDoubleFormat(average));
        return map;
    }

    /**
     * 根据传入的时间以及时间格式来获取到本日的起始时间以及最初的起始时间
     * 新增功能,返回刻钟的起始时间例如:2017-12-12 12:33:00 返回:2017-12-12 12:30:00
     * 的时间戳
     *
     * @param time        现在时间
     * @param timeFormat1 时间格式
     * @return timeStemp
     */
    public static String getTimeStamp(String time, String timeFormat1) {
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat1);
        String dateStr = sdf.format(date);
        //时间戳转字符串
        if (timeFormat1.equals("yyyy-MM-dd")) {
            dateStr = dateStr + " 00:00:00";
        } else if (timeFormat1.equals("yyyy-MM")) {
            dateStr = dateStr + "-01 00:00:00";
        } else if (timeFormat1.equals("yyyy")) {
            dateStr = dateStr + "-01-01 00:00:00";
        } else if (timeFormat1.equals("yyyy-MM-dd HH")) {
            /**
             * 分别返回每一个刻钟的
             */
            SimpleDateFormat mm = new SimpleDateFormat("mm");
            //得出分钟数
            String format = mm.format(date);
            int i = Integer.parseInt(format);

            if (i >= 0 && i < 15) {
                dateStr = dateStr + ":00:00";
            } else if (i >= 15 && i < 30) {
                dateStr = dateStr + ":15:00";
            } else if (i >= 30 && i < 45) {
                dateStr = dateStr + ":30:00";
            } else {
                dateStr = dateStr + ":45:00";
            }
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf1.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeStemp = date.getTime() + "";
        //字符串转时间戳
        return timeStemp;
    }


    /**
     * 输入一个结果
     *@param rs ResultEveryDay
     * @param value 值
     * @param insertTime 插入时间
     * @return rs 返回
     */
    public static ResultEveryDay getResult(Double value, ResultEveryDay rs, long insertTime) {
        if (rs.getCount() == null) {
            rs.setCount("1");
            rs.setAverage(value + "");
            rs.setMax(value + "");
            rs.setMaxTime(insertTime);
            rs.setMin(value + "");
            rs.setMinTime(insertTime);
            rs.setSum(value + "");
        } else if (!rs.getCount().equals("0")) {//对于所有的count 为零的结果跳过不计入
            int i = Integer.parseInt(rs.getCount());
            i++;
            if (Double.parseDouble(rs.getMax()) < value) {
                rs.setMax(value + "");
                rs.setMaxTime(insertTime);
            }
            if (Double.parseDouble(rs.getMin()) > value) {
                rs.setMin(value + "");
                rs.setMinTime(insertTime);
            }

            rs.setSum((Double.parseDouble(rs.getSum()) + value) + "");
            double average = Double.parseDouble(rs.getSum()) / i;
            rs.setAverage(average + "");
            rs.setCount(i + "");
        }
        return rs;
    }

    /**
     * 将负值处理为0
     * @param rs ResultEveryDay
     * @return rs ResultEveryDay
     */

    public static ResultEveryDay getZeroNumber(ResultEveryDay rs){

        if (Integer.parseInt(rs.getAverage())<0){
            rs.setAverage(0+"");
        }

        if (Integer.parseInt(rs.getMax())<0){
            rs.setMax(0+"");
        }
        if (Integer.parseInt(rs.getMin())<0){
            rs.setMin(0+"");
        }
        if (Integer.parseInt(rs.getSum())<0){
            rs.setSum(0+"");
        }

        return rs;

    }


    /**
     * 格式化输出
     *
     * @param f 参数
     * @return f
     */
    public static double getDoubleFormat(double f) {
        if (f != 0) {
            BigDecimal bg = new BigDecimal(f);
            f = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return f;
    }

    /**
     * 获取配置文件里的正则表达式
     * @return list
     */

    public static List<String> regularList() {
        List<String> list = new ArrayList<>();
        String regular = ResourceUtil.getProValueByNameAndKey("cassandra-db", "regular");
        String[] regulars = regular.split(",");
        for (int i = 0; i < regulars.length; i++) {
            list.add(regulars[i]);
        }
        return list;
    }

    /**
     * 输入正则表达式,输入字符串,判断是否符合
     *
     * @param list 字符队列
     * @param value 值
     * @return matche 布尔值
     */
    public static boolean regular(List<String> list, String value) {
        boolean matche = false;
        for (String s : list) {
            Pattern pattern = Pattern.compile(s);
            Matcher matcher = pattern.matcher(value);
            while (matcher.find()) {
                matche = true;
            }
        }
        return matche;
    }

    /**
     * 获得所有的15分钟的起始点
     *
     * @param column1List  列表
     * @return startList 开始列
     */
    public static List<Long> getStartTimeList(List<String> column1List) {
        List<Long> startList = new ArrayList<>();

        for (String column1 : column1List) {
            long start = Long.parseLong(CalculationUtil.getTimeStamp(column1, "yyyy-MM-dd HH"));
            startList.add(start);
        }
        return startList;

    }

    /**
     *
     * @param startList 开始列
     * @param column1List 数列
     * @return map 集合
     */
    public static Map<Long, List<CollectInfo>> getStartGroup(List<Long> startList, List<String> column1List) {

        Map<Long, List<CollectInfo>> map = new HashMap<>();
        List<CollectInfo> collectInfoList = new ArrayList<>();
        for (Long start : startList) {
            for (String column1 : column1List) {
                Long start1 = Long.parseLong(CalculationUtil.getTimeStamp(column1, "yyyy-MM-dd HH"));
                if (start1.longValue() == start.longValue()) {
                    CollectInfo collectInfo = DBConnection.getCollectInfoByColumn1(column1);
                    collectInfoList.add(collectInfo);
                }
            }
            map.put(start, collectInfoList);
        }

        return map;

    }


    /**
     * 输入一个info计算出今天的最大差值最小差值
     *
     * @param info 内容
     * @param rs   每一天的结果
     * @param map  包含这个数据所属时段的最大最小值
     * @return 返回
     */
    public static ResultEveryDay getResultByDiff(CollectInfo info, ResultEveryDay rs, Map<String, ResultEveryDay> map) {
        //获取采集时间
        long column1 = info.getColumn1();
        //数据那个时间段
        String timeStamp = CalculationUtil.getTimeStamp(column1 + "", "yyyy-MM-dd HH");
        //获取所属时间段的最大最小值
        ResultEveryDay resultEveryDay = map.get(timeStamp);
        //将数据输入进去,然后计算最大最小值

        //求出时间段之内的最大差值
        String diff = "";
        if (rs.getMaxTime() >= rs.getMinTime()) {
            diff = (Double.parseDouble(resultEveryDay.getMax()) - Double.parseDouble(resultEveryDay.getMin())) + "";
        } else {
            diff = (Double.parseDouble(resultEveryDay.getMin()) - Double.parseDouble(resultEveryDay.getMax())) + "";
        }
        //判断已有的结果集里面和这个差值最比较,求出这段的结果,即为今天的差值的最大最小值
        rs = CalculationUtil.getResult(Double.parseDouble(diff), rs, Long.parseLong(timeStamp));
        return rs;
    }


    /**
     * 将输入的数据分组,并求出最大最小
     *
     * @param map 集合
     * @param info 信息
     * @return 返回
     */
    public static Map<String, ResultEveryDay> getGroup(Map<String, ResultEveryDay> map, CollectInfo info) {
        //获取采集时间
        long column1 = info.getColumn1();
        //数据那个时间段
        String timeStamp = CalculationUtil.getTimeStamp(column1 + "", "yyyy-MM-dd HH");
        //获取所属时间段的最大最小值
        ResultEveryDay resultEveryDay = map.get(timeStamp);
        if (resultEveryDay == null) {
            resultEveryDay = new ResultEveryDay();
        }
        //将获取的结果输入加上新的信息输入进去获取新的结果
        resultEveryDay = CalculationUtil.getResult(Double.parseDouble(info.getValue()), resultEveryDay, column1);
        map.put(timeStamp, resultEveryDay);
        return map;
    }
}
