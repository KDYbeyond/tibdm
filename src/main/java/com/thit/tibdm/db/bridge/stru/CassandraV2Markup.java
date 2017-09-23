package com.thit.tibdm.db.bridge.stru;

/**
 * Created by wanghaoqiang on 2016/10/24.
 */
public class CassandraV2Markup {
    /**
     *
     */
    private String object_name;
    /**
     *
     */
    private String collect_type;
    /**
     *
     */
    private String machine_id;
    /**
     *
     */
    private Long collect_time;

    public CassandraV2Markup(String object_name, String collect_type, String machine_id, Long collect_time) {
        this.object_name = object_name;
        this.collect_type = collect_type;
        this.machine_id = machine_id;
        this.collect_time = collect_time;
    }


    public Long getCollect_time() {
        return collect_time;
    }

    public void setCollect_time(Long collect_time) {
        this.collect_time = collect_time;
    }

    public CassandraV2Markup() {
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

    public String getCollect_type() {
        return collect_type;
    }

    public void setCollect_type(String collect_type) {
        this.collect_type = collect_type;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }


    @Override
    public String toString() {
        return "CassandraV2Markup{" +
                "object_name='" + object_name + '\'' +
                ", collect_type='" + collect_type + '\'' +
                ", machine_id='" + machine_id + '\'' +
                ", collect_time='" + collect_time + '\'' +
                '}';
    }
}
