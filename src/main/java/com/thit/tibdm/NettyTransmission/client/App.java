package com.thit.tibdm.NettyTransmission.client;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.util.Config;

import java.util.HashMap;

/**
 * Hello world!
 *
 * @author wanghaoqiang
 */
public class App {
    /**
     *
     * @param args 参数
     * @throws InterruptedException 异常
     */
    public static void main(String[] args) throws InterruptedException {
        Config.init();
        for (int i = 0; i < 1000; i++) {
//            String s = "hello" + i;
            String s = getJson();
            TxiConnection.send(s.getBytes());
            Thread.sleep(3000);
        }
    }

    /**
     * @return String
     */
    public static String getJson() {
        String s = "";
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
//        HashMap map3 = new HashMap();
        map1.put("MACHINE_ID", "njnj");
        map1.put("COLLECT_TYPE", "240");
        map1.put("COLLECT_TIME", System.currentTimeMillis() + "");
        map1.put("RECEIVE_TIME", "1459423125123");
        map1.put("AB", "0");
        map1.put("BBXX_TCMS_ZKDYBB1", "28");
        map2.put("MACHINE_ID", "njnj");
        map2.put("COLLECT_TYPE", "240");
        map2.put("json", map1);
//        ArrayList list = new ArrayList();
//        list.add(map1);
//        list.add(map2);
//        list.add(map3);
        s = JSON.toJSONString(map2);
        System.out.println(s);
        return s;
    }
}
