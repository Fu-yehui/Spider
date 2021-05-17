package com.roger.spider.web.spider;


import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Result;
import com.roger.spider.web.entity.Question;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class LQPageHandler implements PageHandler {
    private static final String HEAD_URL="https://www.dotcpp.com";
    private static final String LIST_URL="https://www\\.dotcpp.com/oj/problemset\\.php\\?page=\\d+";
    private static final Pattern LIST_URL_PATTERN=Pattern.compile(LIST_URL);
    private static final String DETAIL_URL="https://www\\.dotcpp\\.com/oj/problem\\d+\\.html";
    private static final Pattern DETAIL_URL_PATTERN=Pattern.compile(DETAIL_URL);

    @Override
    public List<Request> process(Context context, Result result) throws Throwable {
        String currentUrl=context.getRequest().getUrl();
        Document document=context.getResponse().getDocument();
        List<String> list=new ArrayList<>();
        List<Request> nexts=new ArrayList<>();

        //将result默认忽略，不给予pipeline进行处理
        result.setSkip(true);
        if(LIST_URL_PATTERN.matcher(currentUrl).matches()){
            Elements links=document.getElementsByTag("a");
            for(Element link:links){
                String href=link.attr("href");
                String next=HEAD_URL+href;
                if(LIST_URL_PATTERN.matcher(next).matches() || DETAIL_URL_PATTERN.matcher(next).matches()) {
                    nexts.add(new Request(next));
                }
            }
        } else if(DETAIL_URL_PATTERN.matcher(currentUrl).matches()){
            String title=document.select("h4.head_box_text_w").first().text();
            String[] strs=title.split(":");
            if(strs.length==2){
                title=strs[1];
            }
            Elements body=document.getElementsByClass("panel_prob_body");
            for(Element element:body){
                list.add(element.text());
            }
            if(CollectionUtils.isNotEmpty(list) || list.size()==7){
                result.set("question",new Question(title,list.get(0),list.get(1),list.get(2),list.get(3),list.get(4),list.get(5),list.get(6)));
            }
            //只有在处理detail url时，此时的result才会有用
            result.setSkip(false);
        }
        return nexts;
    }

    @Override
    public boolean isSupport(Context context) {
        return context.getRequest().getUrl().startsWith(HEAD_URL);
    }
}
