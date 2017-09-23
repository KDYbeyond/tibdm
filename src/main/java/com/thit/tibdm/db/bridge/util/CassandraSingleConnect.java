package com.thit.tibdm.db.bridge.util;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.thit.tibdm.util.ResourceUtil;

/**
 * Created by wanghaoqiang on 2017/1/16.
 */
public enum CassandraSingleConnect {
    /**
     * 实例
     */
    INSTANCE;
    /**
     * 会话
     */
    private Session instance;

    CassandraSingleConnect() {
        QueryOptions options = new QueryOptions();
        options.setConsistencyLevel(ConsistencyLevel.ONE);
        String cassandraProValue = ResourceUtil.getCassandraProValue(CassandraConstant.CASSANDRA);
        String[] split = cassandraProValue.split(",");
        Cluster cluster = Cluster.builder()
                .addContactPoints(split)
                .withQueryOptions(options)
                .build();
        instance = cluster.connect(ResourceUtil.getCassandraProValue(CassandraConstant.KEYSPACE));
    }

    public Session getInstance() {
        return instance;
    }
}
