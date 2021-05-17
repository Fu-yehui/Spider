package com.roger.spider_proxy.fetch;

import com.roger.spider.spider_common.Spider;
import com.roger.spider.spider_common.SpiderConfig;
import com.roger.spider.spider_common.handler.PageHandlerChain;
import com.roger.spider.spider_common.pipeline.ConsolePipeline;
import com.roger.spider.spider_common.pipeline.PipelineChain;
import com.roger.spider_proxy.dao.RedisDao;
import com.roger.spider_proxy.fetch.pageHandler.*;
import com.roger.spider_proxy.fetch.pipeline.RedisPipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Fetcher implements Closeable {
    private static Logger LOGGER= LoggerFactory.getLogger(Fetcher.class);

    @Autowired
    private RedisPipeline redisPipeline;
    @Autowired
    private ProxyPageHandler proxyPageHandler;
    @Autowired
    private FanqiePageHandler fanqiePageHandler;
    @Autowired
    private ProxyListPlusPageHandler proxyListPlusPageHandler;
    @Autowired
    private ShenJiPageHandler shenJiPageHandler;
    @Autowired
    private IpPageHandler ipPageHandler;

    private Spider spider;


    public void fetch() throws IOException {

        SpiderConfig config = SpiderConfig.builder()
                .setName("proxy-spider")
                .setThreadCount(5)
                .setSleepTime(5000)
                .setRetryCount(1)
                .setPageHandlerChain(new PageHandlerChain().addPageHandler(fanqiePageHandler).addPageHandler(proxyPageHandler).addPageHandler(proxyListPlusPageHandler).addPageHandler(shenJiPageHandler).addPageHandler(ipPageHandler))
                .setPipelineChain(new PipelineChain().addPipeline(redisPipeline).addPipeline(new ConsolePipeline()))
                .build();
        List<String> urls=new ArrayList<>();
//        for (int i = 1; i <= 66; i++) {
//            urls.add("https://www.kuaidaili.com/free/intr/"+ i +"/");
//            urls.add("https://www.kuaidaili.com/free/inha/" + i + "/");//高匿
//        }
        for(int i=1;i<10;i++){
            urls.add("http://x.fanqieip.com/index.php?s=/Api/IpManager/adminFetchFreeIpRegionInfoList&uid=15075&ukey=24896d3c212f5b27ea1a6b4f62977b5b&limit=20&format=1&page="+i);
        }
        for(int i=1;i<6;i++){
            urls.add("https://list.proxylistplus.com/Fresh-HTTP-Proxy-List-"+i);
        }
        urls.add("http://www.shenjidaili.com/product/open/");
        for(int i=1;i<76;i++){
            urls.add("https://www.89ip.cn/index_"+i+".html");
        }
        spider = Spider.create(config).addUrls(urls);
        spider.start();


    }

    @Override
    public void close() {
        if(spider!=null) {
            try {
                spider.stop();
            } catch (Exception e) {
                LOGGER.error("Failed to close [{}]: {}",this.getClass().getSimpleName(),e.getMessage());;
            }
        }
        LOGGER.info("The [{}] has closed successful",this.getClass().getSimpleName());

    }



}
