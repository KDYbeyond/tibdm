package com.thit.tibdm.db.bridge.job;

import com.thit.tibdm.db.bridge.job.util.ReduceUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by dongzhiquan on 2017/6/22.
 */
public class MachineNoTimeJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //无时间限制  当前时间的就加上
        ReduceUtil.saveMachineToCassandra("shanghai","tablemachine_notime");
    }
}
