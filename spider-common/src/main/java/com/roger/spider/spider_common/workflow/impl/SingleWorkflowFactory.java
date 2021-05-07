package com.roger.spider.spider_common.workflow.impl;

import com.roger.spider.spider_common.SpiderConfig;
import com.roger.spider.spider_common.workflow.WorkflowFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleWorkflowFactory implements WorkflowFactory {
    private final CountDownLatch finalization;
    private final AtomicInteger activeThreadCount;
    private final Object requestArrived;
    private final SpiderConfig config;
    private final AtomicInteger counter;
    private final String namePrefix="Single-workflow-";

    public SingleWorkflowFactory(CountDownLatch finalization, SpiderConfig config) {
        this.finalization = finalization;
        this.config = config;
        this.activeThreadCount=new AtomicInteger(0);
        this.requestArrived=new Object();
        this.counter=new AtomicInteger(1);
    }

    @Override
    public SingleWorkflow newWorkflow() {
        return new SingleWorkflow(finalization,activeThreadCount,requestArrived,namePrefix+counter.getAndIncrement(),config);
    }
}
