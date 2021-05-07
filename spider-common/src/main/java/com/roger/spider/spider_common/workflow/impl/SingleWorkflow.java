package com.roger.spider.spider_common.workflow.impl;

import com.roger.spider.spider_common.SpiderConfig;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Response;
import com.roger.spider.spider_common.model.Result;
import com.roger.spider.spider_common.utils.ArgUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleWorkflow extends AbstractWorkflow {

    private final Logger LOGGER= LoggerFactory.getLogger(SingleWorkflow.class);
    private final AtomicInteger activeThreadCount;
    private final Object requestArrived;
    private final CountDownLatch finalization;

    public SingleWorkflow(CountDownLatch finalization, AtomicInteger activeThreadCount, Object requestArrived, String name, SpiderConfig config) {
        super(name,config);
        this.activeThreadCount=activeThreadCount;
        this.requestArrived=requestArrived;
        this.finalization=finalization;
    }


    @Override
    public void run() {
        LOGGER.info("SingleWorkflow [{}] start up",getName());
        activeThreadCount.incrementAndGet();
        while(!Thread.interrupted()) {
            Request request=null;
            try {
                request=getScheduler().get();
                if(request == null){
                    if(activeThreadCount.decrementAndGet()==0){
                        synchronized (requestArrived){
                            //唤醒其余的workflow,使其逐个结束
                            requestArrived.notify();
                        }
                        LOGGER.info("ALL requests have been handled and workflow [{}] exits",getName());
                        finalization.countDown();
                        return;
                    }else{
                        synchronized (requestArrived){
                            requestArrived.wait();
                        }
                        activeThreadCount.incrementAndGet();
                        continue;
                    }
                }
                Response response=null;
                try {
                    response=getDownloader().download(request);
                } catch (IOException e) {
                    LOGGER.warn("Failed to download response about request [{}] : {}",request,e.getMessage());
                    if(getRetryCount() > 0) {
                        Object retryCount = request.getAttribution(Request.RETRY_COUNT);
                        if (retryCount == null) {
                            request.setAttribution(Request.RETRY_COUNT, 1);
                            getScheduler().push(request);
                            LOGGER.info("Request [{}] reentry to scheduler",request.toString());
                        }else{
                            if(retryCount instanceof Integer){
                                int retryCountInt=(Integer)retryCount;
                                if(retryCountInt < getRetryCount()){
                                    request.setAttribution(Request.RETRY_COUNT,retryCountInt+1);
                                    getScheduler().push(request);
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
                    requests = getPageHandler().process(context, result);
                }catch (Throwable throwable){
                    LOGGER.warn("Failed to handle response, [{}],request: [{}],response: [{}]",throwable.getMessage(),request,response);
                    continue;
                }
                if(!ArgUtils.isEmpty(requests)){
                    getScheduler().put(requests);
                    synchronized (requestArrived) {
                        requestArrived.notifyAll();
                    }
                }
                if(!result.isSkip()) {
                    try {
                        getPipeline().process(result);
                    } catch (Throwable throwable) {
                        LOGGER.warn("Failed to pipeline result: [{}], result: [{}]",throwable.getMessage(),result);
                    }
                }
                if(getSleepTime() > 0){
                    Thread.sleep(getSleepTime());
                }
            }catch (InterruptedException e){
                break;
            }
        }
        finalization.countDown();
        LOGGER.info("Thread [{}] is interrupted to exit workflow [{}]",Thread.currentThread().getName(),getName());
    }
}
