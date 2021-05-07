package com.roger.spider.spider_common;

import com.roger.spider.spider_common.downloader.DownloaderConfig;
import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.handler.PageHandlerChain;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Result;

import com.roger.spider.spider_common.pipeline.ConsolePipeline;
import com.roger.spider.spider_common.pipeline.PipelineChain;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LQPageHandler implements PageHandler {
    private static final String HEAD_URL="https://www.dotcpp.com";
    private static final String LIST_URL="https://www\\.dotcpp.com/oj/problemset\\.php\\?page=\\d+";
    private static final Pattern LIST_URL_PATTERN=Pattern.compile(LIST_URL);
    private static final String DETAIL_URL="https://www\\.dotcpp\\.com/oj/problem\\d+\\.html";
    private static final Pattern DETAIL_URL_PATTERN=Pattern.compile(DETAIL_URL);

    @Override
    public List<Request> process(Context context, Result result)  {


        String currentUrl=context.getRequest().getUrl();
        Document document=context.getResponse().getDocument();
        if(DETAIL_URL_PATTERN.matcher(currentUrl).matches()){
            String title=document.select("h4.head_box_text_w").first().text();
//            System.out.println(title);
            result.set("title",title);
//            System.out.println("------");
            Elements prob=document.getElementsByClass("panel_prob");
            for(Element element:prob){
                String head=element.getElementsByClass("panel_prob_head").get(0).text();
                String body=element.getElementsByClass("panel_prob_body").get(0).text();
//                System.out.println(head);
//                System.out.println(body);
                result.set(head,body);
//                System.out.println("------");

            }

        }

        List<Request> nexts = new ArrayList<>();
        if(LIST_URL_PATTERN.matcher(currentUrl).matches()){
//            System.out.println(currentUrl);
            Elements links=document.getElementsByTag("a");
            for(Element link:links){
                String href=link.attr("href");
//                System.out.println(HEAD_URL+href);
                String next=HEAD_URL+href;
                if(LIST_URL_PATTERN.matcher(next).matches() || DETAIL_URL_PATTERN.matcher(next).matches()) {
                    nexts.add(new Request(HEAD_URL + href));
                }
            }
        }
        return nexts;
    }

    @Override
    public boolean isSupport(Context Context) {
        return Context.getRequest().getUrl().startsWith(HEAD_URL);
    }

    public static void main(String[] args) {
        String url="https://www.dotcpp.com/oj/problemset.php?page=62";


        SpiderConfig config = SpiderConfig.builder()
                .setName("spider")
                .setThreadCount(10)
                .setSleepTime(5000)
                .setPageHandlerChain(new PageHandlerChain().addPageHandler(new LQPageHandler()))
                .setPipelineChain(new PipelineChain().addPipeline(new ConsolePipeline()))
                .build();

        Spider spider = Spider.create(config).addUrls(url);
        spider.start();
    }


}


