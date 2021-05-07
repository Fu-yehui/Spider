package com.roger.spider.spider_questionbank.service.serviceImpl;

import com.roger.spider.spider_questionbank.dao.QuestionDao;
import com.roger.spider.spider_questionbank.dto.Counter;
import com.roger.spider.spider_questionbank.exception.SpiderCloseException;
import com.roger.spider.spider_questionbank.exception.SpiderException;
import com.roger.spider.spider_questionbank.service.SpiderService;
import com.roger.spider.spider_questionbank.spider.MyBatisConsumer;
import com.roger.spider.spider_questionbank.spider.QuestionBankPageHandler;


import com.roger.spider_proxy.fetch.downloader.ProxyDownloader;
import com.xiepuhuan.reptile.Reptile;
import com.xiepuhuan.reptile.config.DownloaderConfig;
import com.xiepuhuan.reptile.config.PoolConfig;
import com.xiepuhuan.reptile.config.ReptileConfig;
import com.xiepuhuan.reptile.constants.DeploymentModeEnum;
import com.xiepuhuan.reptile.consumer.impl.ConsoleConsumer;
import com.xiepuhuan.reptile.consumer.impl.ConsumerChain;
import com.xiepuhuan.reptile.downloader.constants.SelectionStrategyEnum;
import com.xiepuhuan.reptile.downloader.constants.UserAgentEnum;
import com.xiepuhuan.reptile.downloader.impl.HttpClientDownloader;
import com.xiepuhuan.reptile.handler.impl.ResponseHandlerChain;
import com.xiepuhuan.reptile.scheduler.impl.FIFOQueueScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.util.ArrayList;
import java.util.List;

public class SpiderServiceImpl implements SpiderService {
    private static Logger LOGGER = LoggerFactory.getLogger(SpiderServiceImpl.class);

    private MyBatisConsumer consumer;
    private QuestionBankPageHandler pageHandler;


    private Reptile reptile;
    private DownloaderConfig downloaderConfig;
    private String url="https://www.dotcpp.com/oj/problemset.php?page=1";
    private int threadCount;
    private int sleepTime;
    private int retryCount;
    private boolean useProxy;

    public SpiderServiceImpl(){

}
    public SpiderServiceImpl(MyBatisConsumer consumer,QuestionBankPageHandler pageHandler,int threadCount,int sleepTime, int retryCount,boolean useProxy){
        this.consumer=consumer;
        this.pageHandler=pageHandler;
        List<String> userAgentPool = UserAgentEnum.all();
        downloaderConfig = DownloaderConfig.builder()
                .enableUserAgentPoolConfig(true)
                .userAgentPoolConfig(new PoolConfig<>(userAgentPool, SelectionStrategyEnum.ORDER))
                .build();
        this.retryCount=retryCount;
        this.threadCount=threadCount;
        this.sleepTime=sleepTime;
        this.useProxy=useProxy;


    }

    /**
     * 开启爬虫,开始爬取题库
     */
    @Override
    public void fetch()  {
        ReptileConfig.ReptileConfigBuilder builder=ReptileConfig
                .builder()
                .name("reptile");
        if(useProxy){
            builder.downloader(new ProxyDownloader(downloaderConfig));
        }else{
            builder.downloader(new HttpClientDownloader(downloaderConfig));
        }
        if(threadCount==0){
            threadCount=1;
        }
        ReptileConfig config =builder
                .asynRun(false)
                .threadCount(threadCount)
                .sleepTime(sleepTime)
                .retryCount(retryCount)
                .responseHandlerChain(ResponseHandlerChain.create().addResponseHandler(pageHandler))
                .deploymentMode(DeploymentModeEnum.SINGLE)
                .scheduler(new FIFOQueueScheduler())
                .consumerChain(new ConsumerChain().create().addConsumer(new ConsoleConsumer()).addConsumer(consumer))
                .build();

        reptile = Reptile.create(config).addUrls(url);
        reptile.start();
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
        ReptileConfig.ReptileConfigBuilder builder=ReptileConfig
                .builder()
                .name("reptile");
        if(useProxy){
            builder.downloader(new ProxyDownloader(downloaderConfig));
        }else{
            builder.downloader(new HttpClientDownloader(downloaderConfig));
        }
        if(threadCount==0){
            threadCount=1;
        }
        ReptileConfig config =builder
                .asynRun(false)
                .threadCount(threadCount)
                .sleepTime(sleepTime)
                .retryCount(retryCount)
                .responseHandlerChain(ResponseHandlerChain.create().addResponseHandler(pageHandler))
                .deploymentMode(DeploymentModeEnum.SINGLE)
                .scheduler(new FIFOQueueScheduler())
                .consumerChain(new ConsumerChain().create().addConsumer(new ConsoleConsumer()).addConsumer(consumer))
                .build();

        reptile = Reptile.create(config).addUrls(url);
        reptile.start();
    }

    @Override
    public void stop() {
        if (reptile != null) {
            try {
                consumer.recover();
                reptile.stop();
            } catch (Exception e) {
                LOGGER.error("Failed to close [{}]: {}", this.getClass().getSimpleName(), e.getMessage());
                throw new SpiderCloseException("Failed to close ["+this.getClass().getSimpleName()+"]: "+ e.getMessage());
            }
        }
        LOGGER.info("The [{}] has closed successful", this.getClass().getSimpleName());

    }

    @Override
    public Counter count(){
        return new Counter(consumer.getTotalCounter().get(),consumer.getNewDataCounter().get());
    }



}