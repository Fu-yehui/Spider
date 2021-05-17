package com.roger.spider_proxy.fetch.pageHandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@Component
public class IpPageHandler implements PageHandler {
    private static String MAIN_URL="https://www.89ip.cn";
    private static String regex="https://www\\.89ip\\.cn/index_\\d+\\.html";
    private static Pattern pattern = Pattern.compile(regex);
    @Override
    public List<Request> process(Context context, Result result) throws Throwable {
        if(pattern.matcher(context.getRequest().getUrl()).matches()){
            Document document=context.getResponse().getDocument();
            Elements tbody=document.select("div.layui-row.layui-col-space15 div.layui-col-md8 div.fly-panel div.layui-form table.layui-table tbody");
            List<Proxy> proxies=new ArrayList<>();
            for(Element element : tbody.select("tr")){
                proxies.add(new Proxy(element.select("td").get(0).text(),Integer.parseInt(element.select("td").get(1).text())));
            }

            if(CollectionUtils.isEmpty(proxies)) {
                result.setSkip(true);
            }else{
                result.set("proxies", proxies);
            }
        }
        return null;
    }

    @Override
    public boolean isSupport(Context context) {
        String currentUrl=context.getRequest().getUrl();
        return currentUrl.startsWith(MAIN_URL);

    }

}
