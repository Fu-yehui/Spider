package com.roger.spider.spider_common.handler;

import com.roger.spider.spider_common.downloader.Downloader;
import com.roger.spider.spider_common.downloader.DownloaderConfig;
import com.roger.spider.spider_common.downloader.HttpClientDownloader;
import com.roger.spider.spider_common.downloader.provider.ProxyProviderTest;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Response;
import com.roger.spider.spider_common.model.Result;
import com.roger.spider.spider_common.pipeline.ConsolePipeline;
import com.roger.spider.spider_common.pipeline.Pipeline;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class LQPageHandlerTest {

    private PageHandler pageHandler=new LQPageHandler();
    private Downloader downloader=new HttpClientDownloader(DownloaderConfig.builder().setProxyProvider(new ProxyProviderTest()).build());
    private Pipeline pipeline=new ConsolePipeline();

    @Test
    public void process() throws Throwable {
        String url1="https://www.dotcpp.com/oj/problemset.php?page=62";
        String url2="https://www.dotcpp.com/oj/problem2525.html";
        Request request=new Request(url2);
        Response response=downloader.download(request);
        System.out.println(response.getStatusCode());
        System.out.println(Arrays.toString(response.getHeaders()));
        System.out.println("----------------");
        Context context=new Context(request,response);
        Result result=new Result();
        System.out.println(pageHandler.isSupport(context));
        System.out.println(pageHandler.process(context,result).stream().map(req -> req.getUrl()+"\n").collect(Collectors.toList()));
        System.out.println("---------Consume Result--------");
        if(!result.isSkip()){
            pipeline.process(result);
        }
    }
}