package com.roger.spider_proxy.fetch.pageHandler;

import com.xiepuhuan.reptile.Reptile;
import com.xiepuhuan.reptile.config.DownloaderConfig;
import com.xiepuhuan.reptile.config.PoolConfig;
import com.xiepuhuan.reptile.config.ReptileConfig;
import com.xiepuhuan.reptile.constants.DeploymentModeEnum;
import com.xiepuhuan.reptile.consumer.impl.ConsoleConsumer;
import com.xiepuhuan.reptile.consumer.impl.ConsumerChain;
import com.xiepuhuan.reptile.consumer.impl.FileConsumer;
import com.xiepuhuan.reptile.consumer.impl.JsonFileConsumer;
import com.xiepuhuan.reptile.downloader.constants.ProxyPool;
import com.xiepuhuan.reptile.downloader.constants.SelectionStrategyEnum;
import com.xiepuhuan.reptile.downloader.constants.UserAgentEnum;
import com.xiepuhuan.reptile.downloader.impl.HttpClientDownloader;
import com.xiepuhuan.reptile.downloader.model.Proxy;
import com.xiepuhuan.reptile.handler.ResponseHandler;
import com.xiepuhuan.reptile.handler.impl.ResponseHandlerChain;
import com.xiepuhuan.reptile.model.Request;
import com.xiepuhuan.reptile.model.ResponseContext;
import com.xiepuhuan.reptile.model.Result;
import com.xiepuhuan.reptile.scheduler.impl.FIFOQueueScheduler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class ProxyPageHandler implements ResponseHandler {
    //https://www.kuaidaili.com/free/inha/8/
    //https://www.kuaidaili.com/free/intr/8/
    private static final String LIST_URL="https://www\\.kuaidaili\\.com/free/in\\w{2}/\\d+/";
    private static final Pattern listPattern= Pattern.compile(LIST_URL);
    private static final String urlHeader="https://www.kuaidaili.com";
    /**
     * 处理响应内容
     *
     * @param responseContext 下载器爬取解析生成的响应内容上下文
     * @param result          保存此次处理后的结果
     * @return 从响应内容解析出新的爬取请求
     * @throws Throwable
     */
    @Override
    public List<Request> handle(ResponseContext responseContext, Result result) throws Throwable {
        String currentUrl=responseContext.getRequest().getUrl();
        Document document=responseContext.getResponse().getContent().getHtmlContent();
        List<Proxy> list=new ArrayList<>();
        List<Request> nexts=new ArrayList<>();
        if(listPattern.matcher(currentUrl).matches()){
            Elements proxies=document.select("div.body div#content div.con-body div div#list table.table.table-bordered.table-striped tbody tr");
            for(Element proxy:proxies){
                Proxy p=new Proxy(proxy.select("td[data-title='IP']").text(),Integer.parseInt(proxy.select("td[data-title='PORT']").text()));
//                ProxyPool.proxyQueue.add(p);
                list.add(p);
            }
        }
        Elements links=document.getElementsByTag("a");
        for(Element link:links){
            String url=urlHeader+link.attr("href");
            if(listPattern.matcher(url).matches()){
                nexts.add(new Request(url));
            }
        }
        result.setResult("proxies",list);

        return nexts;
    }

    /**
     * 判断是否支持处理该响应内容
     *
     * @param responseContext
     * @return 支不支持处理
     */
    @Override
    public boolean isSupport(ResponseContext responseContext) {
        String currentUrl=responseContext.getRequest().getUrl();
        return currentUrl.startsWith(urlHeader);
    }

    public static void main(String[] args) throws IOException {
        List<String> userAgentPool = UserAgentEnum.all();
        DownloaderConfig downloaderConfig= DownloaderConfig.builder()
                .enableUserAgentPoolConfig(true)
                .userAgentPoolConfig(new PoolConfig<>(userAgentPool, SelectionStrategyEnum.ORDER))
//                .generalProxy(new Proxy("49.86.176.79",9999))
                .build();
        ReptileConfig config = ReptileConfig.builder()
                .name("reptile")
                .downloader(new HttpClientDownloader(downloaderConfig))
                .asynRun(false)
                .threadCount(1)
                .sleepTime(2000)
                .retryCount(3)
                .responseHandlerChain(ResponseHandlerChain.create().addResponseHandler(new ProxyPageHandler()))
                .deploymentMode(DeploymentModeEnum.SINGLE)
                .scheduler(new FIFOQueueScheduler())
                .consumerChain(new ConsumerChain().create().addConsumer(new FileConsumer("proxy")).addConsumer(new ConsoleConsumer()).addConsumer(new JsonFileConsumer("proxy")))
                .build();
//        String[] urls={"https://www.kuaidaili.com/free/intr/1/","https://www.kuaidaili.com/free/inha/1/"};
//        String[] urls={"https://www.kuaidaili.com/free/inha/1/"};
        List<String> urls=new ArrayList<>();
        for (int i = 1; i <= 66; i++) {
            urls.add("https://www.kuaidaili.com/free/intr/"+ i +"/");
            urls.add("https://www.kuaidaili.com/free/inha/" + i + "/");//高匿
        }
        Reptile reptile = Reptile.create(config).addUrls(urls);
        reptile.start();
    }
}

