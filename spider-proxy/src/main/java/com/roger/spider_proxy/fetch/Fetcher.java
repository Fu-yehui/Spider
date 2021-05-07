package com.roger.spider_proxy.fetch;

import com.roger.spider_proxy.dao.RedisDao;
import com.roger.spider_proxy.fetch.consumer.RedisConsumer;
import com.roger.spider_proxy.fetch.downloader.ProxyDownloader;
import com.roger.spider_proxy.fetch.pageHandler.FanqiePageHandler;
import com.roger.spider_proxy.fetch.pageHandler.ProxyPageHandler;

import com.xiepuhuan.reptile.Reptile;
import com.xiepuhuan.reptile.config.DownloaderConfig;
import com.xiepuhuan.reptile.config.PoolConfig;
import com.xiepuhuan.reptile.config.ReptileConfig;
import com.xiepuhuan.reptile.constants.DeploymentModeEnum;
import com.xiepuhuan.reptile.consumer.impl.ConsoleConsumer;
import com.xiepuhuan.reptile.consumer.impl.ConsumerChain;
import com.xiepuhuan.reptile.consumer.impl.FileConsumer;
import com.xiepuhuan.reptile.consumer.impl.JsonFileConsumer;
import com.xiepuhuan.reptile.downloader.constants.SelectionStrategyEnum;
import com.xiepuhuan.reptile.downloader.constants.UserAgentEnum;
import com.xiepuhuan.reptile.downloader.impl.HttpClientDownloader;
import com.xiepuhuan.reptile.downloader.model.Proxy;
import com.xiepuhuan.reptile.handler.impl.ResponseHandlerChain;
import com.xiepuhuan.reptile.scheduler.impl.FIFOQueueScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Fetcher implements Closeable {
    private static Logger LOGGER= LoggerFactory.getLogger(Fetcher.class);

    @Autowired
    private RedisConsumer redisConsumer;
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private ProxyPageHandler proxyPageHandler;

    private Reptile reptile;


    public void fetch() throws IOException {
        List<String> userAgentPool = UserAgentEnum.all();
//        List<Proxy> proxyPool=redisDao.proxyPool().stream().map(proxy -> {
//            return new Proxy(proxy.getIp(),proxy.getPort());
//        }).collect(Collectors.toCollection(ArrayList::new));

        DownloaderConfig downloaderConfig= DownloaderConfig.builder()
                .enableUserAgentPoolConfig(true)
                .userAgentPoolConfig(new PoolConfig<>(userAgentPool, SelectionStrategyEnum.ORDER))
//                .generalProxy(new Proxy())
//                .enableProxyPoolConfig(true)
//                .proxyPoolConfig(new PoolConfig<>(proxyPool,SelectionStrategyEnum.ORDER))
                .build();
        ReptileConfig config = ReptileConfig.builder()
                .name("reptile")
                .downloader(new ProxyDownloader(downloaderConfig))
//                .downloader(new HttpClientDownloader(downloaderConfig))
                .asynRun(false)
                .threadCount(5)
                .sleepTime(5000)
                .retryCount(1)
                .responseHandlerChain(ResponseHandlerChain.create().addResponseHandler(new FanqiePageHandler()).addResponseHandler(new ProxyPageHandler()))
                .deploymentMode(DeploymentModeEnum.SINGLE)
                .scheduler(new FIFOQueueScheduler())
                .consumerChain(new ConsumerChain().create().addConsumer(new FileConsumer("proxy")).addConsumer(new ConsoleConsumer()).addConsumer(new JsonFileConsumer("proxy")).addConsumer(redisConsumer))
                .build();
//        String[] urls={"https://www.kuaidaili.com/free/intr/1/","https://www.kuaidaili.com/free/inha/1/"};
//        String[] urls={"https://www.kuaidaili.com/free/inha/1/"};
        List<String> urls=new ArrayList<>();

        for (int i = 1; i <= 66; i++) {
            urls.add("https://www.kuaidaili.com/free/intr/"+ i +"/");
            urls.add("https://www.kuaidaili.com/free/inha/" + i + "/");//高匿
        }
        for(int i=1;i<10;i++){
            urls.add("http://x.fanqieip.com/index.php?s=/Api/IpManager/adminFetchFreeIpRegionInfoList&uid=15075&ukey=24896d3c212f5b27ea1a6b4f62977b5b&limit=20&format=1&page="+i);
        }
        reptile = Reptile.create(config).addUrls(urls);
        reptile.start();


    }

    @Override
    public void close() {
        if(reptile!=null) {
            try {
                reptile.stop();
            } catch (Exception e) {
                LOGGER.error("Failed to close [{}]: {}",this.getClass().getSimpleName(),e.getMessage());;
            }
        }
        LOGGER.info("The [{}] has closed successful",this.getClass().getSimpleName());

    }


//    public static void main(String[] args) throws IOException {
//        List<String> userAgentPool = UserAgentEnum.all();
//        DownloaderConfig downloaderConfig= DownloaderConfig.builder()
//                .enableUserAgentPoolConfig(true)
//                .userAgentPoolConfig(new PoolConfig<>(userAgentPool, SelectionStrategyEnum.ORDER))
////                .generalProxy(new Proxy("49.86.176.79",9999))
//                .build();
//        ReptileConfig config = ReptileConfig.builder()
//                .name("reptile")
//                .downloader(new HttpClientDownloader(downloaderConfig))
//                .asynRun(false)
//                .threadCount(1)
//                .sleepTime(2000)
//                .retryCount(3)
//                .responseHandlerChain(ResponseHandlerChain.create().addResponseHandler(new ProxyPageHandler()))
//                .deploymentMode(DeploymentModeEnum.SINGLE)
//                .scheduler(new FIFOQueueScheduler())
//                .consumerChain(new ConsumerChain().create().addConsumer(new FileConsumer("proxy")).addConsumer(new ConsoleConsumer()).addConsumer(new JsonFileConsumer("proxy")).addConsumer(new RedisConsumer(new RedisDao("8.129.23.141",6379))))
//                .build();
////        String[] urls={"https://www.kuaidaili.com/free/intr/1/","https://www.kuaidaili.com/free/inha/1/"};
////        String[] urls={"https://www.kuaidaili.com/free/inha/1/"};
//        List<String> urls=new ArrayList<>();
//        for (int i = 1; i <= 66; i++) {
//            urls.add("https://www.kuaidaili.com/free/intr/"+ i +"/");
//            urls.add("https://www.kuaidaili.com/free/inha/" + i + "/");//高匿
//        }
//        Reptile reptile = Reptile.create(config).addUrls(urls);
//        reptile.start();
//    }


}
