package com.thit.tibdm.db.bridge.util;

/**
 * 供运维人员使用的建表程序  待开发...
 * Created by dongzhiquan on 2017/4/5.
 *
 * @author wanghaoqiang
 */
public class CreateTableUtil {
    /**
     * @param  keyspace 关键字空
     * @param  category 仓库
     * @param  replication_factor 返回因素
     */
    public static void createKeySpace(String keyspace, String category, int replication_factor) {
        if (category == null) {
            category = "SimpleStrategy";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE KEYSPACE " + keyspace);
        sb.append(" WITH replication={'class':'" + category + "',");
        sb.append("'replication_factor':" + replication_factor + "};");
        System.out.println(sb.toString());
        CassandraSingleConnect.INSTANCE.getInstance().execute(sb.toString());
    }
//
//    public static void createTable(String keyspace, String tableName, LinkedHashMap<String, String> parameter){
//        StringBuffer sb=new StringBuffer();
//        sb.append("CREATE TABLE "+keyspace+"."+tableName);
//        sb.append(doArr(parameter));
//        sb.append("primary key(");
//        sb.append(doArr(parameter));
//        sb.append("));");
//
//    }

    /**
     *读取配置文件
     * @param
     */

    //属性和类型解析

    //拼字符串

    //读配置文件  有的话读 没有 通知并停止程序getParm
    //拿到类型

//
//    public static String doArr( LinkedHashMap<String, String> parameter ){
//        StringBuffer sb1=new StringBuffer();
//
//        Iterator iterator=parameter.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry entry= (Map.Entry) iterator.next();
//            String key= (String) entry.getKey();
//            String value= (String) entry.getValue();
//            getParams(key);
//
//            sb1.append(key+" "+value+",");
//        }
//
//        return sb1.toString();
//    }

//    public static String getParams(String propertity){
//        Properties prop = new Properties();
//        //读取属性文件a.properties
//
//        InputStream in = null;
//        try {
//            in = new BufferedInputStream(new FileInputStream("d:/project/tibdm/target/classes/"+propertity+".properties"));
//            prop.load(in);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String ss=null;
//        if (prop.getProperty(propertity)!=null){
//              ss =prop.getProperty(propertity);
//        }else {
//            ss="配置文件中没有此项！请重新配置!";
//        }
//        return ss;
//    }

//    public static void main(String[] args){
////        createKeySpace("ss",null,2);
//        getParams("cassandra-db");
//    }
}
