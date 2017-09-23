package com.thit.tibdm.db.bridge.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dongzhiquan on 2017/6/27.
 */
public enum ExecutorSingle {

    /**
     *
     */
    INSTANCE;
    /**
     *
     */
    private ExecutorService instance;

    ExecutorSingle() {
        instance= Executors.newCachedThreadPool();
    }

    public ExecutorService getInstance(){
        return instance;
    }
}
