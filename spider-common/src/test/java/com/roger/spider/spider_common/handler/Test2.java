package com.roger.spider.spider_common.handler;

import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Result;

import java.util.List;

public class Test2 implements PageHandler {
    /**
     * The content of the url is processing by the current PageHandler
     *
     * @param context including current URL and corresponding responsive content
     * @param result  the result that the responsive content is processed by the PageHandler
     * @return URLs that need to be crawled
     */
    @Override
    public List<Request> process(Context context, Result result) throws Throwable {
        System.out.println("test1");
        return null;
    }

    /**
     * Whether the PageHandler supports the current URL
     *
     * @param context
     * @return
     */
    @Override
    public boolean isSupport(Context context) {
        return true;
    }
}
