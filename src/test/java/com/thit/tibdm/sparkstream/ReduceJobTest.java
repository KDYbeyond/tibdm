package com.thit.tibdm.sparkstream;

import com.xicrm.common.TXISystem;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dongzhiquan on 2017/6/27.
 */
public class ReduceJobTest {

    @Test
    public void testGo() throws Exception {

        TXISystem.start();

        ReduceJob reduceJob=new ReduceJob();

        reduceJob.go();

    }
}