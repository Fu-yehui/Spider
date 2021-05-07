package com.roger.spider.spider_common.workflow.impl;

import com.roger.spider.spider_common.downloader.Downloader;
import com.roger.spider.spider_common.downloader.DownloaderConfig;
import com.roger.spider.spider_common.downloader.HttpClientDownloader;
import com.roger.spider.spider_common.downloader.provider.ProxyProviderTest;
import com.roger.spider.spider_common.handler.LQPageHandler;
import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.handler.PageHandlerChain;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Response;
import com.roger.spider.spider_common.model.Result;
import com.roger.spider.spider_common.pipeline.ConsolePipeline;
import com.roger.spider.spider_common.pipeline.Pipeline;
import com.roger.spider.spider_common.pipeline.PipelineChain;
import com.roger.spider.spider_common.scheduler.FIFOQueueScheduler;
import com.roger.spider.spider_common.scheduler.Scheduler;
import com.roger.spider.spider_common.utils.ArgUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class RetryTest {
    private static Logger LOGGER = LoggerFactory.getLogger(RetryTest.class);

    private PageHandler pageHandler=new PageHandlerChain().addPageHandler(new LQPageHandler());
    private Downloader downloader=new HttpClientDownloader();
    private Pipeline pipeline=new PipelineChain().addPipeline(new ConsolePipeline());
    private Scheduler scheduler=new FIFOQueueScheduler();
    private int sleepTime=1000;
    private int retry=2;
    @Test
    public void run() {
        String url="https://wpp.com/oj/probleset.php?page=62";
        scheduler.put(new Request(url));
        while(!Thread.interrupted()) {
            Request request=null;
            try {
                request=scheduler.get();
                if(request == null){
                    System.out.println("ALL requests have been handled and workflow exits");
                    return;
                }
                Response response=null;
                try {
                    response=downloader.download(request);
                } catch (IOException e) {
                    LOGGER.warn("Failed to download response about request [{}] : {}",request,e.getMessage());
                    if(retry > 0) {
                        Object retryCount = request.getAttribution(Request.RETRY_COUNT);
                        if (retryCount == null) {
                            request.setAttribution(Request.RETRY_COUNT, 1);
                            scheduler.push(request);
                            LOGGER.info("Request [{}] reentry to scheduler",request.toString());
                        }else{
                            if(retryCount instanceof Integer){
                                int retryCountInt=(Integer)retryCount;
                                if(retryCountInt < retry){
                                    request.setAttribution(Request.RETRY_COUNT,retryCountInt+1);
                                    scheduler.push(request);
                                    LOGGER.info("Request [{}] reentry to scheduler",request.toString());
                                }
                            }
                        }
                    }
                    continue;
                }
                Context context=new Context(request,response);
                Result result=new Result();
                List<Request> requests=null;
                try {
                    requests = pageHandler.process(context, result);
                }catch (Throwable throwable){
                    LOGGER.warn("Failed to handle response, [{}],request: [{}],response: [{}]",throwable.getMessage(),request,response);
                    continue;
                }
                if(!ArgUtils.isEmpty(requests)){
                    scheduler.put(requests);
                }
                if(!result.isSkip()) {
                    try {
                        pipeline.process(result);
                    } catch (Throwable throwable) {
                        LOGGER.warn("Failed to pipeline result: [{}], result: [{}]",throwable.getMessage(),result);
                    }
                }
                if(sleepTime > 0){
                    Thread.sleep(sleepTime);
                }
            }catch (InterruptedException e){
                break;
            }
        }


    }


}