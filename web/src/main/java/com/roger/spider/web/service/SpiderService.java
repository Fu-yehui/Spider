package com.roger.spider.web.service;


import com.roger.spider.web.dto.Counter;

public interface SpiderService {
    /**
     * 开启爬虫,开始爬去题库
     */
    void fetch();

    /**
     * 停止爬虫
     */
    void stop();

    /**
     * 返回爬虫爬取的总数据数量，和新数据数量
     * @return
     */
    Counter count();

    /**
     * 使用参数配置并启动爬虫
     * @param threadCount
     * @param retryCount
     * @param sleepTime
     * @param useProxy
     * @param url
     */
    void fetch(int threadCount, int retryCount, int sleepTime, boolean useProxy, String url);
}
