package com.thit.tibdm.imdb.bridge;

import com.thit.tibdm.base.InterfaceFactory;

/**
 * @author wanghaoqiang
 */

public class TXIInMemoryDBFactory extends InterfaceFactory {
    /**
     * @param sss sss
     * @return TXIInMemoryDB DB
     * @throws Exception 异常
     */
    public static TXIInMemoryDB getInst(String sss) throws Exception {
        try {
            return (TXIInMemoryDB) getInstance(sss);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


}