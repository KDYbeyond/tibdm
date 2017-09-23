package com.thit.tibdm.NettyTransmission.ProtocolAnalysis;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 警告规则解析
 *
 * @author wanghaoqiang
 * @version 1.0
 * @create 2017-04-11 14:04
 **/
public class WarningRules {
    /**
     * 日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(WarningRules.class);

    /**
     * 用来判断返回布尔类型的表达式引擎方法
     *@param ch  String
     * @param jexlExp jexlExp
     * @param map map
     * @return boolean 布尔值
     */
    public static boolean convertToCode(String jexlExp, Map<String, String> map,String ch) {
//        if (ch.equals("330")){
//            LOGGER.error(map.toString());
//        }
        JexlEngine jexl = new JexlEngine();
        Expression e = jexl.createExpression(jexlExp);
        JexlContext jc = new MapContext();
        for (String key : map.keySet()) {
            jc.set(key, Double.parseDouble(map.get(key)));
        }
        if (null == e.evaluate(jc)) {
            return false;
        }
//        Object evaluate = e.evaluate(jc);
//        logger.error(evaluate.toString());
        return (boolean) e.evaluate(jc);
    }

    /**
     * 用来计算表达式
     *
     * @param jexlExp jexlExp
     * @param map map
     *
     * @return double 数字
     */
    public static double convertToDouble(String jexlExp, Map<String, String> map) {
        JexlEngine jexl = new JexlEngine();
        Expression e = jexl.createExpression(jexlExp);
        JexlContext jc = new MapContext();
        for (String key : map.keySet()) {
            jc.set(key, map.get(key));
        }
        Object evaluate = e.evaluate(jc);
        if (null == evaluate) {
            return 0;
        } else if (evaluate instanceof Double) {
            return (double) evaluate;
        } else {
            return Double.parseDouble(evaluate.toString()+"");
        }

    }
}
