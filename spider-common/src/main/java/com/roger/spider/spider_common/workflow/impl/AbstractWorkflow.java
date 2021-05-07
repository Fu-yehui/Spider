package com.roger.spider.spider_common.workflow.impl;

import com.roger.spider.spider_common.SpiderConfig;
import com.roger.spider.spider_common.downloader.Downloader;
import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.handler.PageHandlerChain;
import com.roger.spider.spider_common.pipeline.Pipeline;
import com.roger.spider.spider_common.pipeline.PipelineChain;
import com.roger.spider.spider_common.scheduler.Scheduler;
import com.roger.spider.spider_common.workflow.Workflow;

public abstract class AbstractWorkflow implements Workflow {
    private final String name;
    private final Scheduler scheduler;
    private final Downloader downloader;
    private final PageHandler pageHandler;
    private final Pipeline pipeline;
    private final long sleepTime;
    private final int retryCount;

    public AbstractWorkflow(String name, SpiderConfig config) {
        this.name = name;
        this.downloader=config.getDownloader();
        this.scheduler=config.getScheduler();
        this.pageHandler=config.getPageHandlerChain();
        this.pipeline=config.getPipelineChain();
        this.sleepTime=config.getSleepTime();
        this.retryCount=config.getRetryCount();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public Downloader getDownloader() {
        return downloader;
    }

    @Override
    public PageHandler getPageHandler() {
        return pageHandler;
    }

    @Override
    public Pipeline getPipeline() {
        return pipeline;
    }

    @Override
    public long getSleepTime() {
        return sleepTime;
    }

    @Override
    public int getRetryCount() {
        return retryCount;
    }
}
