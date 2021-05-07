package com.roger.spider.spider_common.scheduler.filter;

import com.roger.spider.spider_common.model.Request;

/**
 * Deduplication filter
 */
public interface Filter {

    /**
     *
     * @param request
     * @return Return true when the request is repeated
     */
    public boolean filter(Request request);
}

