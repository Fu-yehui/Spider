package com.roger.spider.spider_common;

import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.handler.PageHandlerChain;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Result;
import com.roger.spider.spider_common.pipeline.ConsolePipeline;
import com.roger.spider.spider_common.pipeline.PipelineChain;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.RequestDispatcher;
import java.util.List;
import java.util.regex.Pattern;

public class IHuanPageHandler implements PageHandler {
    private static final String HEAD_URL="https://ip.ihuan.me/?page=e92k59727";
    private static final String EXTRACT_URL="https://ip.ihuan.me/tqdl.html";

    /**
     * The content of the url is processing by the current PageHandler
     *
     * @param context including current URL and corresponding responsive content
     * @param result  the result that the responsive content is processed by the PageHandler
     * @return URLs that need to be crawled
     */
    @Override
    public List<Request> process(Context context, Result result) throws Throwable {
        String currentUrl=context.getRequest().getUrl();
        if(EXTRACT_URL.equals(currentUrl)){
            String proxies=context.getResponse().getDocument().select(" div.panel-body").get(0).text();
            result.set("proxies",proxies);
        }
        return null;
    }

    /**
     * Whether the PageHandler supports the current URL
     *
     * @param context
     * @return
     */
    @Override
    public boolean isSupport(Context context) {
        return context.getRequest().getUrl().startsWith(HEAD_URL);
    }

    public static void main(String[] args) {
        String url="https://ip.ihuan.me/ti.html";
        Request request=new Request(url,Request.METHOD_POST);

        SpiderConfig config = SpiderConfig.builder()
                .setName("spider")
                .setThreadCount(1)
                .setPageHandlerChain(new PageHandlerChain().addPageHandler(new IHuanPageHandler()))
                .setPipelineChain(new PipelineChain().addPipeline(new ConsolePipeline()))
                .build();

        Spider spider = Spider.create(config).addUrls(url);
        spider.start();
    }
}
