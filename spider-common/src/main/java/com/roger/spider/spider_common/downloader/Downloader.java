package com.roger.spider.spider_common.downloader;

import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Response;

import java.io.IOException;

public interface Downloader {
    /**
     * Download the response page according to the given request
     * and encapsulate it into a response to return
     *
     * @param request
     * @return
     */
    public Response download(Request request) throws IOException;
}
