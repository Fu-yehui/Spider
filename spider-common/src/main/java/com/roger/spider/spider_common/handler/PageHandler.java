package com.roger.spider.spider_common.handler;

import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Result;

import java.util.List;

public interface PageHandler {

    /**
     * The content of the url is processing by the current PageHandler
     * @param context including current URL and corresponding responsive content
     * @param result the result that the responsive content is processed by the PageHandler
     * @return URLs that need to be crawled
     */
    public List<Request> process(Context context, Result result) throws Throwable;

    /**
     * Whether the PageHandler supports the current URL
     * @param context
     * @return
     */
    public boolean isSupport(Context context);
}
