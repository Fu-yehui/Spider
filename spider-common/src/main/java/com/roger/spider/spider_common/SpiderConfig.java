package com.roger.spider.spider_common;

import com.roger.spider.spider_common.downloader.Downloader;
import com.roger.spider.spider_common.downloader.HttpClientDownloader;
import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.handler.PageHandlerChain;
import com.roger.spider.spider_common.pipeline.ConsolePipeline;
import com.roger.spider.spider_common.pipeline.Pipeline;
import com.roger.spider.spider_common.pipeline.PipelineChain;
import com.roger.spider.spider_common.scheduler.FIFOQueueScheduler;
import com.roger.spider.spider_common.scheduler.Scheduler;
import com.roger.spider.spider_common.utils.ArgUtils;

public class SpiderConfig {
    private String name;
    private long sleepTime;
    private int threadCount;
    private int retryCount;
    private Scheduler scheduler;
    private Downloader downloader;
    private PageHandlerChain pageHandlerChain;
    private PipelineChain pipelineChain;

    public SpiderConfig(String name, long sleepTime, int threadCount, int retryCount, Scheduler scheduler, Downloader downloader, PageHandlerChain pageHandlerChain, PipelineChain pipelineChain) {
        this.name = name;
        this.sleepTime = sleepTime;
        this.threadCount = threadCount;
        this.retryCount = retryCount;
        this.scheduler = scheduler;
        this.downloader = downloader;
        this.pageHandlerChain = pageHandlerChain;
        this.pipelineChain = pipelineChain;
    }

    public void check(){
        ArgUtils.notEmpty(name,"SpiderName");
        ArgUtils.check(sleepTime >= 0,"sleepTime must be greater than or equal to 0");
        ArgUtils.check(threadCount >= 1,"threadCount must be greater than or equal to 1");
        ArgUtils.check(retryCount >= 0,"retryCount must be greater than or equal to 0");
        ArgUtils.notNull(downloader);
        ArgUtils.notNull(scheduler);
        ArgUtils.notNull(pageHandlerChain);
        ArgUtils.notNull(pipelineChain);

    }

    public String getName() {
        return name;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public Downloader getDownloader() {
        return downloader;
    }

    public PageHandlerChain getPageHandlerChain() {
        return pageHandlerChain;
    }

    public PipelineChain getPipelineChain() {
        return pipelineChain;
    }

    public static SpiderConfigBuilder builder(){
        return new SpiderConfigBuilder();
    }
    public static class SpiderConfigBuilder{
        private String name="spider";
        private long sleepTime=0;
        private int threadCount=Runtime.getRuntime().availableProcessors();
        private int retryCount=3;
        private Scheduler scheduler=new FIFOQueueScheduler();
        private Downloader downloader=new HttpClientDownloader();
        private PageHandlerChain pageHandlerChain=new PageHandlerChain();
        private PipelineChain pipelineChain=new PipelineChain(new ConsolePipeline());

        public SpiderConfigBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public SpiderConfigBuilder setSleepTime(long sleepTime) {
            this.sleepTime = sleepTime;
            return this;
        }

        public SpiderConfigBuilder setThreadCount(int threadCount) {
            this.threadCount = threadCount;
            return this;
        }

        public SpiderConfigBuilder setRetryCount(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public SpiderConfigBuilder setScheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        public SpiderConfigBuilder setDownloader(Downloader downloader) {
            this.downloader = downloader;
            return this;
        }

        public SpiderConfigBuilder setPageHandlerChain(PageHandlerChain pageHandlerChain) {
            this.pageHandlerChain = pageHandlerChain;
            return this;
        }

        public SpiderConfigBuilder setPipelineChain(PipelineChain pipelineChain) {
            this.pipelineChain = pipelineChain;
            return this;
        }
        public SpiderConfigBuilder addPageHandler(PageHandler pageHandler){
            pageHandlerChain.addPageHandler(pageHandler);
            return this;
        }
        public SpiderConfigBuilder addPipeline(Pipeline pipeline){
            pipelineChain.addPipeline(pipeline);
            return this;
        }

        public SpiderConfig build() {
            return new SpiderConfig(this.name, this.sleepTime, this.threadCount, this.retryCount, this.scheduler, this.downloader, this.pageHandlerChain, this.pipelineChain);
        }

    }
}
