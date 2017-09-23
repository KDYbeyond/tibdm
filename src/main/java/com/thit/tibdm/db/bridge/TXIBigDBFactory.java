package com.thit.tibdm.db.bridge;

import com.thit.tibdm.base.InterfaceFactory;

/**
 * @author lili
 */

public class TXIBigDBFactory extends InterfaceFactory {

    /**
     * @param sss sss
     * @return TXIBigDB DB
     * @throws Exception 异常
     */
    public static TXIBigDB getInst(String sss) throws Exception {
        try {
            return (TXIBigDB) getInstance(sss);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


}