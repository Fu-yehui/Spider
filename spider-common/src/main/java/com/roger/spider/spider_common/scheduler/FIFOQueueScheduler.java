package com.roger.spider.spider_common.scheduler;

import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.scheduler.filter.BloomSchedulerFilter;
import com.roger.spider.spider_common.scheduler.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FIFOQueueScheduler implements Scheduler{


    private static final Logger LOGGER = LoggerFactory.getLogger(FIFOQueueScheduler.class);
    private ConcurrentLinkedQueue<Request> queue;
    private Filter filter;

    public FIFOQueueScheduler(){
        queue=new ConcurrentLinkedQueue<>();
        filter=new BloomSchedulerFilter();
    }

    public FIFOQueueScheduler(Filter filter){
        queue=new ConcurrentLinkedQueue<>();
        this.filter=filter;
    }

    @Override
    public Request get() {
        return queue.poll();
    }

    /**
     * 将请求经过Filter过滤后压入Scheduler
     * @param request
     */
    @Override
    public void put(Request request) {
        if(!filter.filter(request)){
            queue.add(request);
        }
    }

    @Override
    public void put(Request... requests) {
        Arrays.stream(requests)
                .forEach(this::put);
    }

    @Override
    public void put(Collection<Request> requests) {
        requests.stream()
                .forEach(this::put);
    }

    /**
     * 仅在请求下载出现错误且未达到相应的重试次数时调用,
     * 直接将request压入Scheduler,不需要经过过滤
     * @param request
     */
    @Override
    public void push(Request request) {
        queue.add(request);
    }

    @Override
    public void push(Request... requests) {
        Arrays.stream(requests)
                .forEach(queue::add);
    }

    @Override
    public void push(Collection<Request> requests) {
        queue.addAll(requests);
    }
}
