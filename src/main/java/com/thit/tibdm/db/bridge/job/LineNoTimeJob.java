package com.thit.tibdm.db.bridge.job;

import com.thit.tibdm.db.bridge.job.util.ReduceUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by dongzhiquan on 2017/6/22.
 */
public class LineNoTimeJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //无时间类型参数  累加上一条记录  Cassandra表为
        ReduceUtil.saveLineToCassandra("shanghai","tableline_notime");

    }
}
