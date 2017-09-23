package com.thit.tibdm.base;

import com.xicrm.common.TXISystem;

/**
 * <p>
 * Title: </p>
 * <p>
 * Description: </p>
 * <p>
 * Copyright: Copyright (c) 2003</p>
 * <p>
 * Company: �廪��ѧ</p>
 *
 * @author�� ����
 * @version 1.0
 */


/**
 * @author 李丽
 * @version 1.0
 */
public class InterfaceFactory {
    /**
     * @param className 类名
     * @return dao  InterfaceBase
     * @throws Exception 异常
     */
    public static InterfaceBase getInstance(String className) throws Exception {
        try {
            InterfaceBase dao = (InterfaceBase) Class.forName(className).newInstance();
            return dao;
        } catch (Exception e) {
            e.printStackTrace();
            TXISystem.log.error("InterfaceFactory", e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
    }
}
