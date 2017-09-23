package com.thit.tibdm.mq.bridge.util;

import com.googlecode.asyn4j.core.handler.CacheAsynWorkHandler;
import com.googlecode.asyn4j.core.handler.DefaultErrorAsynWorkHandler;
import com.googlecode.asyn4j.core.handler.FileAsynServiceHandler;
import com.googlecode.asyn4j.service.AsynService;
import com.googlecode.asyn4j.service.AsynServiceImpl;

/**
 * Created by dongzhiquan on 2017/3/31.
 *
 * @author wanghaoqiang
 */
public class AsyncUtil {

    /**
     * 初始化AsynService
     *
     * @return AsynService
     */
    public static AsynService getAsynService() {
        // 初始化异步工作服务
        AsynService asynService = AsynServiceImpl.getService(300, 3000L, 100, 100, 1000);
        //异步工作缓冲处理器
        asynService.setWorkQueueFullHandler(new CacheAsynWorkHandler(100));
        //服务启动和关闭处理器
        asynService.setServiceHandler(new FileAsynServiceHandler());
        //异步工作执行异常处理器
        asynService.setErrorAsynWorkHandler(new DefaultErrorAsynWorkHandler());
        // 启动服务
        asynService.init();
        return asynService;
    }
}
