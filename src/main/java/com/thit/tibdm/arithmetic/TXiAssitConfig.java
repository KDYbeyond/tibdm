/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thit.tibdm.arithmetic;

/**
 * @author Administrator
 */

import com.xicrm.common.TXISystem;

import java.io.IOException;
import java.util.Properties;

//import com.xicrm.docConvert.stru.TXIConvertStru;

/**
 * @author wanghaoqiang
 */
public class TXiAssitConfig {
    /**
     * config
     */
    public static final String config = "tibdm_assist.properties";
    /**
     * 配置
     */
    private static Properties prop = new Properties();
    //private static HashMap<String, TXIConvertStru> suffixMap = new HashMap<String, TXIConvertStru>();

    static {
        System.out.println("----YYYYY00000");
        try {
            // prop.load(new FileInputStream(new File("C:\\"+config)));
            prop.load(TXiAssitConfig.class.getClassLoader().getResourceAsStream(
                    config));
            //2016.09 李丽 改进配置结构为自定义结构
           /* String pools = prop.getProperty("convert_pools");
            System.out.println("pools=" + pools);
            //读文档转换配置池
            if (pools != null && pools.trim().length() != 0) {
                String[] pool_list = pools.split(",");
                String temp_sss = "";
                String implClass = "";
                String[] suffix_list;
                TXIConvertStru convert_stru = null;
                //针对每种配置类型读类型配置
                for (int i = 0; i < pool_list.length; i++) {
                    // 拼成列表串
                    temp_sss = prop.getProperty(pool_list[i].trim() + TXIJNDINames.SUFFIX, "").trim().toLowerCase();
                    convert_stru = new TXIConvertStru();
                    convert_stru.setImplClass(prop.getProperty(pool_list[i].trim() + TXIJNDINames.IMPLCLASS));
                    convert_stru.setConvertSuffix(prop.getProperty(pool_list[i].trim() + TXIJNDINames.CONVERTSUFFIX));
                    convert_stru.setClientmultifiles(prop.getProperty(pool_list[i].trim() + TXIJNDINames.CLIENTMULTIFILES));
                    convert_stru.setConvert2dir(prop.getProperty(pool_list[i].trim() + TXIJNDINames.CONVERT2DIR));
System.out.println(convert_stru.toString());
                    suffix_list = temp_sss.split(",");
                    //针对每种后缀记录处理的实现类及相关信息
                    for (int j = 0; j < suffix_list.length; j++) {
                        convert_stru.setSuffix(suffix_list[j]);
                        suffixMap.put(suffix_list[j], convert_stru);
                        System.out.println(convert_stru.toString());
                    }
                }*/
        } catch (IOException e) {
            String errmsg = "tidbm_assist.properties文件不存在！";
            TXISystem.log.error("TXiAssitConfig出错：", errmsg);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param key 关键字
     * @return 返回值
     */
    public static String getString(String key) {
        //System.out.println("----key=" + key + " value=" + prop.getProperty(key));
        return prop.getProperty(key);
    }

    /**
     *
     * @param key 关键字
     * @param default_value 默认值
     * @return 返回值
     */
    public static String getString(String key, String default_value) {
        //System.out.println("key=" + key + " value=" + prop.getProperty(key) + " def=" + default_value);
        return prop.getProperty(key, default_value);
    }

    /*public static TXIConvertStru getConvertStru(String suffix) {
        TXIConvertStru stru = suffixMap.get(suffix);
        if (stru != null) {
            return stru;
        } else {
            //等于null返回缺省实现类
            return getConvertStru(TXIJNDINames.DEFAULT);
        }
    }*/

//    public static void main(String[] args) {
//        System.out.println("开始运行11111112222");
//         System.out.println("jieshu运行11111112222");
//
//    }

}
