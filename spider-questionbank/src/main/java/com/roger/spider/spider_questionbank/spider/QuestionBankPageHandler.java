package com.roger.spider.spider_questionbank.spider;


import com.roger.spider.spider_questionbank.entity.Question;
import com.xiepuhuan.reptile.handler.ResponseHandler;
import com.xiepuhuan.reptile.model.Request;
import com.xiepuhuan.reptile.model.ResponseContext;
import com.xiepuhuan.reptile.model.Result;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@Component
public class QuestionBankPageHandler implements ResponseHandler {
    private static final String HEAD_URL="https://www.dotcpp.com";
    private static final String LIST_URL="https://www\\.dotcpp.com/oj/problemset\\.php\\?page=\\d+";
    private static final Pattern LIST_URL_PATTERN=Pattern.compile(LIST_URL);
    private static final String DETAIL_URL="https://www\\.dotcpp\\.com/oj/problem\\d+\\.html";
    private static final Pattern DETAIL_URL_PATTERN=Pattern.compile(DETAIL_URL);

    @Override
    public List<Request> handle(ResponseContext responseContext, Result result) throws Throwable {
        String currentUrl=responseContext.getRequest().getUrl();
        Document document=responseContext.getResponse().getContent().getHtmlContent();
        List<String> list=new ArrayList<>();
        List<Request> nexts=new ArrayList<>();

        //将result默认忽略，不给予consumer进行处理
        result.setIgnore(true);
        if(LIST_URL_PATTERN.matcher(currentUrl).matches()){
            Elements links=document.getElementsByTag("a");
            for(Element link:links){
                String href=link.attr("href");
                String next=HEAD_URL+href;
                if(LIST_URL_PATTERN.matcher(next).matches() || DETAIL_URL_PATTERN.matcher(next).matches()) {
                    nexts.add(new Request(HEAD_URL + href));
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
                result.setResult("question",new Question(title,list.get(0),list.get(1),list.get(2),list.get(3),list.get(4),list.get(5),list.get(6)));
            }
            //只有在处理detail url时，此时的result才会有用
            result.setIgnore(false);
        }
        return nexts;
    }

    @Override
    public boolean isSupport(ResponseContext responseContext) {
        return responseContext.getRequest().getUrl().startsWith(HEAD_URL);
    }
}
