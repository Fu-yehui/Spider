package com.roger.spider_proxy.fetch.pageHandler;

import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Proxy;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Result;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class ProxyPageHandler implements PageHandler {
    //https://www.kuaidaili.com/free/inha/8/
    //https://www.kuaidaili.com/free/intr/8/
    private static final String LIST_URL = "https://www\\.kuaidaili\\.com/free/in\\w{2}/\\d+/";
    private static final Pattern listPattern = Pattern.compile(LIST_URL);
    private static final String urlHeader = "https://www.kuaidaili.com";

    @Override
    public List<Request> process(Context context, Result result) throws Throwable {
        String currentUrl = context.getRequest().getUrl();
        Document document = context.getResponse().getDocument();
        List<Proxy> list = new ArrayList<>();
        List<Request> nexts = new ArrayList<>();
        if (listPattern.matcher(currentUrl).matches()) {
            Elements proxies = document.select("div.body div#content div.con-body div div#list table.table.table-bordered.table-striped tbody tr");
            for (Element proxy : proxies) {
                Proxy p = new Proxy(proxy.select("td[data-title='IP']").text(), Integer.parseInt(proxy.select("td[data-title='PORT']").text()));
                list.add(p);
            }
        }
        Elements links = document.getElementsByTag("a");
        for (Element link : links) {
            String url = urlHeader + link.attr("href");
            if (listPattern.matcher(url).matches()) {
                nexts.add(new Request(url));
            }
        }
        if (CollectionUtils.isEmpty(list)){
            result.setSkip(true);
        }else{
            result.set("proxies", list);
        }

        return nexts;
    }

    /**
     * 判断是否支持处理该响应内容
     *
     * @param context
     * @return 支不支持处理
     */
    @Override
    public boolean isSupport(Context context) {
        String currentUrl = context.getRequest().getUrl();
        return currentUrl.startsWith(urlHeader);
    }

}

