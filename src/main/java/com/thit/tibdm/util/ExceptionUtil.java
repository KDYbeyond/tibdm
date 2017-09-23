package com.thit.tibdm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 堆栈详情
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-07-05 18:13
 **/
public class ExceptionUtil {
    /**
     * 日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(ExceptionUtil.class);

    /**
     *
     * @param e 异常
     * @return 返回
     */
    public static String getMsgExce(Exception e) {
        StringBuffer msg = new StringBuffer();
        int length = e.getStackTrace().length;
        String message = e.toString();
        if (length > 0) {
            msg.append(message + "\n");
            for (int i = 0; i < length; i++) {
                msg.append("\t" + e.getStackTrace()[i] + "\n");
            }
        } else {
            msg.append(message);
        }
        return msg.toString();
    }
}
