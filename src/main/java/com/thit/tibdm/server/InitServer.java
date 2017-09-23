package com.thit.tibdm.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * 启动非web类任务
 *
 * @author wanghaoqiang
 * @version 1.0
 * @time 2017-07-31 15:19
 **/
public class InitServer  extends HttpServlet {
    public static final Logger LOGGER = LoggerFactory.getLogger(InitServer.class);


    @Override
    public void init(ServletConfig cfg) throws ServletException {
        initModule();
    }

    public static void initModule() throws ServletException {
        //启动各种自定义的方法

        //启动计算异常服务
        StatisticsWarn.init();
    }
}
