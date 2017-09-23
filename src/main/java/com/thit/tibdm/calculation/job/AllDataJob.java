package com.thit.tibdm.calculation.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by wanghaoqiang on 2016/12/16.
 *
 * @author wanghaoqiang
 */
public class AllDataJob implements Job {
    /**
     * 定时任务类.
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //System.out.println("统计全部的数据任务进行中");
        CalculationTimer.execTimer();
    }
}
