package com.roger.spider.spider_common.scheduler;

import com.roger.spider.spider_common.model.Request;

import java.util.Collection;

public interface Scheduler {

    public Request get();

    /**
     * 将请求经过Filter过滤后压入Scheduler
     * @param request
     */
    public void put(Request request);

    public void put(Request... requests);

    public void put(Collection<Request> requests);

    /**
     * 仅在请求下载出现错误且未达到相应的重试次数时调用,
     * 直接将request压入Scheduler,不需要经过过滤
     * @param request
     */
    public void push(Request request);

    public void push(Request... requests);

    public void push(Collection<Request> requests);
}
