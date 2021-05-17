package com.roger.spider_proxy.fetch.pageHandler;

import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Proxy;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class ShenJiPageHandler implements PageHandler {
    private static String MAIN_URL="http://www.shenjidaili.com/";
    private static String regex="http://www\\.shenjidaili\\.com/product/open/";
    private static Pattern pattern = Pattern.compile(regex);

    /**
     * The content of the url is processing by the current PageHandler
     *
     * @param context including current URL and corresponding responsive content
     * @param result  the result that the responsive content is processed by the PageHandler
     * @return URLs that need to be crawled
     */
    @Override
    public List<Request> process(Context context, Result result) throws Throwable {
        if(pattern.matcher(context.getRequest().getUrl()).matches()){
            Document document=context.getResponse().getDocument();
            Elements tbody=document.select("div.w-100.position-relative.open_iplink div.container div.row.scroll.ip_link div.col div.pt-2 div#pills-tabContent.tab-content div#pills-stable_https.tab-pane.fade.show.active.p-3 table.table.table-hover.text-white.text-center.table-borderless tbody");
            List<Proxy> proxies=new ArrayList<>();
            for(int i=1;i<tbody.select("tr").size();i++){
                Element e=tbody.select("tr").get(i);
                proxies.add(convert(e.select("td").get(0).text()));
            }

            if(CollectionUtils.isEmpty(proxies)) {
                result.setSkip(true);
            }else{
                result.set("proxies", proxies);
            }
        }
        return null;
    }

    private Proxy convert(String proxyStr){
        String[] strings=proxyStr.split(":");
        if(strings.length==2){
            return new Proxy(strings[0],Integer.parseInt(strings[1]));
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
        if(StringUtils.isNotEmpty(context.getRequest().getUrl())) {
            return context.getRequest().getUrl().startsWith(MAIN_URL);
        }
        return false;
    }
}