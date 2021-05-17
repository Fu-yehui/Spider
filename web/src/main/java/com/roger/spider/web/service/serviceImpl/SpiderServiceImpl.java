package com.roger.spider.web.service.serviceImpl;

import com.roger.spider.spider_common.Spider;
import com.roger.spider.spider_common.SpiderConfig;
import com.roger.spider.spider_common.downloader.DownloaderConfig;
import com.roger.spider.spider_common.downloader.HttpClientDownloader;
import com.roger.spider.spider_common.downloader.provider.ProxyProvider;
import com.roger.spider.web.dto.Counter;
import com.roger.spider.web.exception.SpiderCloseException;
import com.roger.spider.web.service.SpiderService;
import com.roger.spider.web.spider.HZPageHandler;
import com.roger.spider.web.spider.MyBatisPipeline;
import com.roger.spider.web.spider.LQPageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpiderServiceImpl implements SpiderService {
    private static Logger LOGGER = LoggerFactory.getLogger(SpiderServiceImpl.class);

    private MyBatisPipeline pipeline;
    private LQPageHandler lqPageHandler;
    private HZPageHandler hzPageHandler;
    private ProxyProvider proxyProvider;
    private Spider spider;
    private DownloaderConfig downloaderConfig;
    private int threadCount;
    private int sleepTime;
    private int retryCount;
    private List<String> urls=new ArrayList<String>();
    {
        String lqUrl="https://www.dotcpp.com/oj/problemset.php?page=1";
        urls.add(lqUrl);
        for(int i=1000;i<6938;i++){
           String hzUrl="http://acm.hdu.edu.cn/showproblem.php?pid="+i;
            urls.add(hzUrl);
        }
    }

    public SpiderServiceImpl(){

}
    public SpiderServiceImpl(MyBatisPipeline pipeline, LQPageHandler lqPageHandler,  HZPageHandler hzPageHandler, int threadCount, int sleepTime, int retryCount, boolean useProxy, ProxyProvider proxyProvider){
        this.pipeline=pipeline;
        this.lqPageHandler=lqPageHandler;
        this.hzPageHandler=hzPageHandler;
        this.retryCount=retryCount;
        this.threadCount=threadCount;
        this.sleepTime=sleepTime;
        this.proxyProvider=proxyProvider;
        if(useProxy){
            downloaderConfig=DownloaderConfig.builder().setProxyProvider(proxyProvider).build();
        }else{
            downloaderConfig=DownloaderConfig.DEFAULT_DOWNLOADER_CONFIG;
        }


    }

    /**
     * 开启爬虫,开始爬取题库
     */
    @Override
    public void fetch() {

        SpiderConfig config = SpiderConfig.builder()
                .setName("QuestionBank-spider")
                .setDownloader(new HttpClientDownloader(downloaderConfig))
                .setThreadCount(threadCount)
                .setSleepTime(sleepTime)
                .setRetryCount(retryCount)
                .addPageHandler(lqPageHandler)
                .addPageHandler(hzPageHandler)
                .addPipeline(pipeline)
                .build();

        spider =Spider.create(config).addUrls(urls);
        spider.start();
    }


    /**
     * 使用参数配置并启动爬虫
     *
     * @param threadCount
     * @param retryCount
     * @param sleepTime
     * @param useProxy
     * @param url
     */
    @Override
    public void fetch(int threadCount, int retryCount, int sleepTime, boolean useProxy, String url) {
        if(useProxy){
            downloaderConfig=DownloaderConfig.builder().setProxyProvider(proxyProvider).build();
        }
        SpiderConfig config = SpiderConfig.builder()
                .setName("QuestionBank-spider")
                .setDownloader(new HttpClientDownloader(downloaderConfig))
                .setThreadCount(threadCount)
                .setSleepTime(sleepTime)
                .setRetryCount(retryCount)
                .addPageHandler(lqPageHandler)
                .addPageHandler(hzPageHandler)
                .addPipeline(pipeline)
                .build();

        spider =Spider.create(config).addUrls(url);
        spider.start();
    }

    @Override
    public void stop() {
        if (spider != null) {
            try {
                pipeline.recover();
                spider.stop();
            } catch (Exception e) {
                LOGGER.error("Failed to close [{}]: {}", this.getClass().getSimpleName(), e.getMessage());
                throw new SpiderCloseException("Failed to close ["+this.getClass().getSimpleName()+"]: "+ e.getMessage());
            }
        }
        LOGGER.info("The [{}] has closed successful", this.getClass().getSimpleName());

    }

    @Override
    public Counter count(){
        return new Counter(pipeline.getTotalCounter().get(),pipeline.getNewDataCounter().get());
    }



}