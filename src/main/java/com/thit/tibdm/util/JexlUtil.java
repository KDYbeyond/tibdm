package com.thit.tibdm.util;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * 运算表达式
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-07-14 17:53
 **/
public class JexlUtil {
    /**
     * log日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(JexlUtil.class);

    /**
     * jexl引擎
     */
    public static final JexlEngine JEXL = new JexlEngine();

    /**
     * 用来计算表达式
     *
     * @param jexlExp 表达式
     * @param map     值的map
     * @param decimal 保留小数点后多少位
     * @return double 数字
     */
    public static String execJexl(String jexlExp, Map<String, String> map, int decimal) {
        StringBuffer sb = new StringBuffer();
        sb.append("#.");
        for (int i = 0; i < decimal; i++) {
            sb.append("0");
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        String result;
        Expression e = JEXL.createExpression(jexlExp);
        JexlContext jc = new MapContext();
        for (String key : map.keySet()) {
            jc.set(key, map.get(key));
        }
        Object evaluate = e.evaluate(jc);
        if (evaluate instanceof Boolean) {
            Boolean bool = (Boolean) evaluate;
            if (bool) {
                result = "0";
            } else {
                result = "1";
            }
        } else {
            double f = Double.parseDouble(evaluate.toString());
            if (decimal == 0) {
                result = (int) f + "";
            } else {
                result = df.format(f);
            }
        }
        return result;
    }

}
