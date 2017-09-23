package com.thit.tibdm.db.bridge.job;

import com.thit.tibdm.db.bridge.job.util.ReduceUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by dongzhiquan on 2017/6/22.
 */
public class LineTimeJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //有时间限制
        ReduceUtil.saveLineToCassandra("shanghai_time","tableline_time");

    }
}
