package com.roger.spider.spider_common.workflow;

import com.roger.spider.spider_common.downloader.Downloader;
import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.handler.PageHandlerChain;
import com.roger.spider.spider_common.pipeline.Pipeline;
import com.roger.spider.spider_common.pipeline.PipelineChain;
import com.roger.spider.spider_common.scheduler.Scheduler;

public interface Workflow extends Runnable {

    String getName();

    Scheduler getScheduler();

    Downloader getDownloader();

    PageHandler getPageHandler();

    Pipeline getPipeline();

    long getSleepTime();

    int getRetryCount();
}
