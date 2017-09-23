package com.thit.tibdm.db.bridge.job.util;

import com.xicrm.common.TXISystem;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dongzhiquan on 2017/6/27.
 */
public class ReduceUtilTest {

    @Test
    public void testSaveLineToCassandra() throws Exception {

        TXISystem.start();
        ReduceUtil.saveLineToCassandra("shanghai","tableline_time");

    }

    @Test
    public void testSaveMachineToCassandra() throws Exception {
        TXISystem.start();
        ReduceUtil.saveMachineToCassandra("shanghai_time","tablemachine_time");

    }
}