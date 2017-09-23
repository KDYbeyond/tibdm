package com.thit.tibdm.sparkstream.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从parquet中分析
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-06-03 15:16
 **/
public class SparkSqlParquet {
    /**
     * 日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(SparkSqlParquet.class);

    /**
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local[6]").setAppName("parquet");
        SparkSession session = SparkSession.builder().config(sparkConf).getOrCreate();
        session.read().parquet("hdfs://192.168.8.106:9000/user/machine/").createOrReplaceTempView("machine");
//        session.sqlContext().registerDataFrameAsTable();
        Dataset<Row> sql = session.sql("select max(ZT104) from machine");
        sql.printSchema();
        sql.show();
        session.stop();
    }
}
