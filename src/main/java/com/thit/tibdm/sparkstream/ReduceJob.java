package com.thit.tibdm.sparkstream;

import com.thit.tibdm.db.bridge.job.MachineTimeJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by dongzhiquan on 2017/6/7.
 */
public class ReduceJob {
    /**
     * @throws Exception 异常
     */
    public static void go() throws Exception {
        // 首先，必需要取得一个Scheduler的引用
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        //jobs可以在scheduled的sched.start()方法前被调用

        //job 1将每隔20秒执行一次
        JobDetail job = newJob(MachineTimeJob.class).withIdentity("reducejob", "reducegroup").build();
        CronTrigger trigger = newTrigger().withIdentity("reducetrigger", "reducegroup").withSchedule(cronSchedule("0/05 * * * * ?")).build();
        Date ft = sched.scheduleJob(job, trigger);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        System.out.println(job.getKey() + " 已被安排执行于: " + sdf.format(ft) + "，并且以如下重复规则重复执行: " + trigger.getCronExpression());


        // 开始执行，start()方法被调用后，计时器就开始工作，计时调度中允许放入N个Job
        sched.start();
        try {
            //主线程等待一分钟
            Thread.sleep(30L * 1000L);
        } catch (Exception e) {
        }
        //关闭定时调度，定时器不再工作
//        sched.shutdown(true);
    }
}
