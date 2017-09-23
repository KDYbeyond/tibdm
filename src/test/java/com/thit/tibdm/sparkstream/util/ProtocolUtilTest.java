package com.thit.tibdm.sparkstream.util;

import com.datastax.driver.core.*;
import com.thit.tibdm.bom.entity.MessageProtocol;
import com.thit.tibdm.bom.entity.Variable;
import com.thit.tibdm.db.bridge.util.CassandraSingleConnect;
import com.thit.tibdm.util.ResourceUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author: dongzhiquan  Date: 2017/7/17 Time: 13:50
 */
public class ProtocolUtilTest {

    public final static Logger logger = LoggerFactory.getLogger(ProtocolUtilTest.class);
    private Set<String> protocolSet = new HashSet<String>();

    /**
     * 获取协议
     *
     * @throws Exception
     */
    @Test
    public void testGetProtocolAll() throws Exception {

        logger.error(ProtocolUtil.getProtocolMap("").toString());


    }

    /**
     * 获取车号 协议名
     *
     * @throws Exception
     */
    @Test
    public void testGetChProtocolMap() throws Exception {

        Map<String, String> protocolMap = ProtocolUtil.getChProtocolMap("");
        logger.error(protocolMap.toString());

    }

    @Test
    public void testGetProNames() throws Exception {

        logger.error(ProtocolUtil.getProNames().toString());
    }

    @Test
    public void testCheckStatus() throws Exception {

        ProtocolUtil.checkStatus("hehe", "change");

    }

    @Test
    public void testUpdateOK() throws Exception {
    }

    @Test
    public void testUpdateNO() throws Exception {
    }



    // 获取所有协议
    @Test
    public void updateProtocol() {
        String cql_query = "select * from"+""+" bomkp.tableline_time" +" limit 1";
        List<String> listProtocol = new ArrayList<String>();
        ResultSet resultSet = CassandraSingleConnect.INSTANCE.getInstance().execute(cql_query);
        System.out.println("当前表的字段");
        ColumnDefinitions columnDefinitions = resultSet.getColumnDefinitions();
        for (ColumnDefinitions.Definition column : columnDefinitions) {
            System.out.println(column.getName());
            listProtocol.add(column.getName());
        }
        Map<String, Object> map = ProtocolUtil.getProtocolAll("");
        Map<String, MessageProtocol> messageProtocolMap = ProtocolUtil.getProtocolMap(map);
        System.out.println("variable中的协议");
        for (Map.Entry<String, MessageProtocol> entry : messageProtocolMap.entrySet()) {
            MessageProtocol messageProtocol = entry.getValue();
            List<Variable> variables = messageProtocol.getVariable();
            for (Variable variable : variables) {
                System.out.println(variable.getUniqueCode().toLowerCase());
//                protocolSet.add(variable.getUniqueCode().toLowerCase());
                boolean isContains = listProtocol.contains(variable.getUniqueCode().toLowerCase());
                if (!isContains){
                    String add_column_sql = "ALTER TABLE"+""+" bomkp.tableline_time"+" ADD "+variable.getUniqueCode().toLowerCase()+""+" text";
                    CassandraSingleConnect.INSTANCE.getInstance().execute(add_column_sql);
                }
            }
        }
    }
    @Test
    public void testUpdate(){
        List<String> tableNames = new ArrayList<String>();
        tableNames.add(ResourceUtil.getCassandraProValue("tableline_time"));
        tableNames.add(ResourceUtil.getCassandraProValue("tablemachine_notime"));
        tableNames.add(ResourceUtil.getCassandraProValue("tablemachine_time"));
        tableNames.add(ResourceUtil.getCassandraProValue("tableline_notime"));
        tableNames.add(ResourceUtil.getCassandraProValue("data_all"));
        for (String table:tableNames) {
            updateProtocol(table);
        }
    }
    @Test
    public void updateProtocol(String tableName) {
        String cql_query = "select * from " + tableName + " limit 1";
        List<String> listProtocol = new ArrayList<String>();
        ResultSet resultSet = CassandraSingleConnect.INSTANCE.getInstance().execute(cql_query);
       // LOGGER.info("当前表的字段");
        ColumnDefinitions columnDefinitions = resultSet.getColumnDefinitions();
        for (ColumnDefinitions.Definition column : columnDefinitions) {
            System.out.println(column.getName());
            listProtocol.add(column.getName());
        }
        Map<String, Object> map = ProtocolUtil.getProtocolAll("");
        Map<String, MessageProtocol> messageProtocolMap = ProtocolUtil.getProtocolMap(map);
        //LOGGER.info("variable中的协议");
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
    @Test
    public void getTableNames(){
    }


}