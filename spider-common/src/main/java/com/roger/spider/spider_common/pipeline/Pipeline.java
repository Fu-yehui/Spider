package com.roger.spider.spider_common.pipeline;

import com.roger.spider.spider_common.model.Result;

public interface Pipeline {

    /**
     * Process the results
     * @param result
     */
    public void process(Result result) throws Throwable;
}
