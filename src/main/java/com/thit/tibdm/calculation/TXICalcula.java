package com.thit.tibdm.calculation;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.calculation.job.CalculationTimer;
import com.thit.tibdm.calculation.stru.CollectInfo;
import com.thit.tibdm.calculation.stru.ResultEveryDay;
import com.thit.tibdm.calculation.util.CalculationUtil;

import java.util.*;

/**
 * Created by wanghaoqiang on 2016/12/20.
 */
public class TXICalcula {
    /**
     * 获得大数据统计结果
     *
     * @param objectName  对象名
     * @param tagNameList tagname列表
     * @param time        需要的时间
     * @param valueType   统计的类型
     * @return 返回值
     */
    public String XIGetCalculationResult(String objectName, String tagNameList, String time, String valueType) {
        List result = new ArrayList();
        //tagNameList变为数组
        List<String> list = (List) JSON.parse(tagNameList);
        for (String tagName : list) {
            //首先查询开始时间结束时间
            Map<String, String> timeInfo = CalculationUtil.getStartTime(time, valueType);
            System.out.println(timeInfo.toString());
            //通过最后的今日时间获取数据列表
            List<CollectInfo> valueList = CalculationUtil.getValueList(objectName, tagName, timeInfo.get("endTime").toString(), time);

            //判断是否匹配配置文件中得正则表达式
            List<String> reguList = CalculationUtil.regularList();
            boolean regular = CalculationUtil.regular(list, tagName);
            ResultEveryDay calculationInfo = new ResultEveryDay();
            if (regular) {
                //计算出当天的数据,的差值的最大最小
                calculationInfo = CalculationTimer.calculationFifteenMins(tagName, Long.parseLong(timeInfo.get("startTime")), Long.parseLong(time));
            } else {
                //计算今日的结果
                calculationInfo = CalculationUtil.getCalculationInfo(valueList, tagName, objectName, time);
            }
            //查询以前的结果
            List<ResultEveryDay> countedData = CalculationUtil.getData4Db(objectName, tagName, timeInfo.get("startTime").toString(), timeInfo.get("endTime").toString());
            System.out.println(countedData.toString());
            //将今天的计算结果添加到数组里面
            countedData.add(calculationInfo);
            //计算最终结果
            Map resultData = CalculationUtil.getResultData(countedData);
            //汇总MAP
            Map tagData = new HashMap();
            tagData.put(tagName, resultData);
            result.add(tagData);
        }
        return JSON.toJSONString(result);
    }
}
