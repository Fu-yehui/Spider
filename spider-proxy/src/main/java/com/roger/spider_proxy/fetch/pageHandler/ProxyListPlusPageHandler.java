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
public class ProxyListPlusPageHandler implements PageHandler {
    private static String MAIN_URL="https://list.proxylistplus.com/";
    private static String regex="https://list\\.proxylistplus\\.com/Fresh-HTTP-Proxy-List-\\d+";
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
            Elements elements=document.select("body.home.page.page-id-2.page-template.page-template-blog.page-template-blog-php.logged-in.admin-bar.no-customize-support.single-author.sidebar div#page.hfeed.site table.bg tbody tr.cells");

            List<Proxy> proxies=new ArrayList<Proxy>();
            for(Element element : elements){
                proxies.add(new Proxy(element.select("td").get(1).text(),Integer.parseInt(element.select("td").get(2).text())));
            }
            if(CollectionUtils.isEmpty(proxies)) {
                result.setSkip(true);
            }else{
                result.set("proxies", proxies);
            }
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
