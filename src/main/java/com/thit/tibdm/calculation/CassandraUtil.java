package com.thit.tibdm.calculation;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.thit.tibdm.calculation.stru.CollectInfo;
import com.thit.tibdm.calculation.stru.ResultEveryDay;
import com.thit.tibdm.db.bridge.util.CassandraSingleConnect;
import com.thit.tibdm.util.Constants;
import com.thit.tibdm.util.ResourceUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by wanghaoqiang on 2016/12/9.
 */
public class CassandraUtil {

    /**
     * 测试查询
     *
     * @param args
     */
//    public static void main(String[] args) {
//        TXISystem.start();
//        List<CollectInfo> opc_data = getDataByTime("opc_data", "TY_BGQ_DM.ZP_TDM15.SW_DL9217_EL", 0, System.currentTimeMillis());
//        System.out.println(opc_data.size());

//        System.out.println(System.currentTimeMillis());
//    }

    /**
     * 输入tagName,起始时间,结束时间来查询数据
     *
     * @param objectName 对象名字
     * @param tagName    采集点名称
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return list
     */
    public static List<CollectInfo> getDataByTime(String objectName, String tagName, long startTime, long endTime) {
        List list = new ArrayList();
        String cql = "SELECT * FROM " + objectName + " where KEY = '" + tagName + "' and column1>" + startTime + " and column1<" + endTime;
        ResultSet rs = CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
        Iterator<Row> iterator = rs.iterator();
        while (iterator.hasNext()) {
            CollectInfo re = new CollectInfo();
            Row row = iterator.next();
            re.setKey(row.getString("key"));
            re.setColumn1(row.getLong("column1"));
            re.setValue(row.getString("value"));
            list.add(re);
        }
        return list;
    }


    /**
     * 输入tagName,起始时间,结束时间来查询以前统计过得数据
     *
     * @param objectName 对象名字
     * @param tagName    采集点名称
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return list
     */
    public static List getCountedData(String objectName, String tagName, long startTime, long endTime) {
        List list = new ArrayList();
        String cql = "select * from " + ResourceUtil.getCassandraProValue(Constants.TABLE_RESULT)
                + " where objectName = '" + objectName + "' and tagName = '" + tagName
                + "' and time > " + startTime + " and time < " + endTime;
        System.out.println(cql);
        ResultSet rs = CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
        Iterator<Row> iterator = rs.iterator();
        while (iterator.hasNext()) {
            ResultEveryDay re = new ResultEveryDay();
            Row row = iterator.next();
            re = toResultStru(re, row);
            list.add(re);
        }

        return list;
    }


    /**
     * 保存计算结果
     *
     * @param rs ResultEveryDay
     */
    public static void saveResult(ResultEveryDay rs) {
        String cql = "INSERT INTO " + ResourceUtil.getCassandraProValue(Constants.TABLE_RESULT) + " (objectname, tagname , time , average , \"count\", max , min , maxtime , mintime , sum ) VALUES ( '"
                + rs.getObjectName() + "', '" + rs.getTagName() + "', " + rs.getTime() + ", '" + rs.getAverage() + "', '" + rs.getCount()
                + "', '" + rs.getMax() + "', '" + rs.getMin() + "', " + rs.getMaxTime() + ", " + rs.getMinTime() + ", '" + rs.getSum() + "')";
        System.out.println(cql);
        CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
    }

    /**
     * 根据tagName查询出上一次统计的时间
     *
     * @param tagName 标签
     * @return  ResultEveryDay
     */
    public static ResultEveryDay getLatestByTagName(String tagName) {
        String cql = "select * from " + ResourceUtil.getCassandraProValue(Constants.TABLE_RESULT) + " WHERE objectName = '"
                + ResourceUtil.getCassandraProValue(Constants.TABLE_DATA) + "' and tagName = '" + tagName + "'";
        ResultSet rs = CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
        Iterator<Row> iterator = rs.iterator();
        ResultEveryDay tmp = null;
        while (iterator.hasNext()) {
            ResultEveryDay re = new ResultEveryDay();
            Row row = iterator.next();
            re = toResultStru(re, row);
            if (tmp == null) {
                tmp = re;
            } else {
                //求出最大的time
                if (tmp.getTime() < re.getTime()) {
                    tmp = re;
                }
            }
        }

        return tmp;
    }

    /**
     * 转换映射
     *
     * @param re ResultEveryDay
     * @param row row行
     * @return  ResultEveryDay
     */
    private static ResultEveryDay toResultStru(ResultEveryDay re, Row row) {
        re.setMax(row.getString("max"));
        re.setMin(row.getString("min"));
        re.setAverage(row.getString("average"));
        re.setSum(row.getString("sum"));
        re.setTime(row.getLong("time"));
        re.setCount(row.getString("count"));
        re.setTagName(row.getString("tagname"));
        re.setObjectName(row.getString("objectname"));
        re.setMinTime(row.getLong("mintime"));
        re.setMaxTime(row.getLong("maxtime"));
        return re;
    }

    /**
     *
     * @param tagNameList 参数
     * @return 返回
     */
    public static List<String> getColumn1List(List<String> tagNameList) {
        long endTime = System.currentTimeMillis();
        List list = new ArrayList();
        for (String tagName : tagNameList) {
            String cql = "select * from " + ResourceUtil.getCassandraProValue(Constants.TABLE_RESULT)
                    + " where objectName = '" + ResourceUtil.getCassandraProValue(Constants.TABLE_DATA) + "' and tagName = '" + tagName
                    + " and time < " + endTime;
            ResultSet rs = CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
            Iterator<Row> iterator = rs.iterator();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                list.add(row.getString("COLUMN1"));
            }
        }
        return list;
    }

    /**
     * 输入cql然后执行cql
     *
     * @param cql 语句
     * @return ResultSet
     */
    public static ResultSet execCql(String cql) {
        ResultSet rs = CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
        return rs;
    }
}
