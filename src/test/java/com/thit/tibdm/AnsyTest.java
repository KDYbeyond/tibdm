package com.thit.tibdm;

import com.thit.tibdm.NettyTransmission.ProtocolAnalysis.WarningRules;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 异步调用测试
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-03-31 11:01
 **/
public class AnsyTest {
    public static Logger logger = LoggerFactory.getLogger(AnsyTest.class);

    @org.junit.Test
    public void testCon(){
        Map<String,String> map=new HashMap<>();
        map.put("x", "10000");
        double v = WarningRules.convertToDouble("(( x * 10 * 2 / 1.05 - 37779 ) / 189) > 0 ? (( x * 10 * 2 / 1.05 - 37779 ) / 189) : 0", map);

        logger.info(v+"");
    }
}
