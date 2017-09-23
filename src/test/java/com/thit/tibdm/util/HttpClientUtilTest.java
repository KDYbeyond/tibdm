package com.thit.tibdm.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by wanghaoqiang on 2017/6/5.
 */
public class HttpClientUtilTest {
    public final static Logger LOGGER = LoggerFactory.getLogger(HttpClientUtilTest.class);
    @Test
    public void sendHttpPost() throws Exception {
        LOGGER.info(HttpClientUtil.sendHttpPost("http://192.168.8.109:8090/tibdm/txieasyui",
                "taskFramePN=GetBigData&command=Get_RealTimeByLineSum&colname=json_ajax&colname1={dataform:\"eui_form_data" +
                "\"}&object_name=shanghai&collect_type=240&lineList=[\"l01\",\"l02\",\"l03\",\"l04\",\"l05\",\"l06\",\"l07\",\"l08\",\"l09\",\"l10\",\"l11\",\"l12\"," +
                        "\"l17\"]&keys=[{\"ZT201%2BZT202%2BZT203-ZT204\":[\"ZT201\",\"ZT202\",\"ZT203\",\"zt204\"]},\"ZT200\"]"));
    }

    @Test
    public void sendGet() throws IOException {
        String str="[{\"BOM_TRAINNUMBER\":\"335\",\"BOM_CODE\":\"9012\",\"BOM_HANDLE\":\"1\"}]";

        HttpClientKeepSession.pushMsg("WARN",str);
    }

}