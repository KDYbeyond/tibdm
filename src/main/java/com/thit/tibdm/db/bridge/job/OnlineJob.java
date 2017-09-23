package com.thit.tibdm.db.bridge.job;

import com.thit.tibdm.db.bridge.job.util.OnlineUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by dongzhiquan on 2017/6/28.
 */
public class OnlineJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        OnlineUtil.doOnline();
    }
}
