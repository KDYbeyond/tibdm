package com.thit.tibdm.util;

import com.alibaba.fastjson.JSONArray;
import com.thit.tibdm.NettyTransmission.ProtocolAnalysis.WarningRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wanghaoqiang
 */
public class TXIFilterStepsUtil {
    /**
     * 日志
     */
    public static final Logger logger = LoggerFactory.getLogger(TXIFilterStepsUtil.class);

    /**
     * 得到过滤后的步长List
     *
     * @param fromList 列表
     * @param max_num 字数
     * @param filter_mode 过滤
     * @return 返回值
     */
    public static List getFilterStepsList(List fromList, String max_num,
                                          String filter_mode) {
        List toList = new ArrayList();
        // 处理分段数据
        int i_max_num = Integer.parseInt(max_num);

        // 如果小于最大数，返回，否则要做相应处理
        if (fromList.size() <= i_max_num || i_max_num == -1) {
            return fromList;
        } else {
            // 通过一定的策略进行处理
            int mode = Integer.parseInt(filter_mode);
            switch (mode) {
                case 1:
                    // 按照平均步长取值
                    int distance = 0;
                    float f_step = fromList.size() / i_max_num;
                    for (int i = 0; i < i_max_num; i++) {
                        distance = (int) Math.floor(i * f_step);
                        toList.add(fromList.get(distance));
                    }

                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }

        }
        return toList;
    }

    /**
     * [{"zt1+zt2+zt3":["zt1","zt2","zt3"]},{"zt4+zt5+zt6":["zt4","zt5","zt6"]},"zt7","zt8"]
     *
     * @param map map
     * @param keys 关键字
     * @return 返回值
     */
    public static Map<String, String> filterMap(Map<String, String> map, JSONArray keys) {
        DecimalFormat df = new DecimalFormat("#.00");
        Map<String, String> pa = new HashMap();
        Map<String, String> result = new HashMap<>();
        keys.forEach(j -> {
            if (j instanceof String) {
                if (map.containsKey(j)) {
                    result.put(j.toString(), map.get(j));
                }
            } else {
                Map<String, List<String>> para = (Map<String, List<String>>) j;
                para.forEach((k, v) -> {
//                    logger.info("k==" + k);
//                    logger.info("v==" + v);
                    v.forEach(p -> pa.put(p, map.get(p)));
                    double v1 = WarningRules.convertToDouble(k.toString(), pa);
                    result.put(k, df.format(v1));
                });
            }
        });
        return result;
    }

}
