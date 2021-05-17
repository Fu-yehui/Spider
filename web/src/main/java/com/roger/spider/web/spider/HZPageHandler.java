package com.roger.spider.web.spider;

import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Result;
import com.roger.spider.web.entity.Question;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class HZPageHandler implements PageHandler {
    private static final String HEAD_URL="http://acm.hdu.edu.cn/";
    private static final String LIST_URL="http://acm\\.hdu\\.edu\\.cn/listproblem\\.php\\?vol=\\d+";
    private static final Pattern LIST_URL_PATTERN=Pattern.compile(LIST_URL);
    private static final String DETAIL_URL="http://acm\\.hdu\\.edu\\.cn/showproblem\\.php\\?pid=\\d+";
    private static final Pattern DETAIL_URL_PATTERN=Pattern.compile(DETAIL_URL);

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
//        System.out.println(currentUrl);
//        List<Request> list=new ArrayList<>();;
        Document document = context.getResponse().getDocument();
        result.setSkip(true);
        if(DETAIL_URL_PATTERN.matcher(currentUrl).matches()) {
            String title = document.select("h1").get(0).text();
            if (StringUtils.isNotEmpty(title)) {
                Elements contents = document.select("div.panel_content");
                if (contents.size() == 7) {
                    Question question=new Question(title,contents.get(0).text(),contents.get(1).text(),contents.get(2).text(),contents.get(3).text(),contents.get(4).text(),contents.get(6).text(), null);
                    result.set("question",question);
                    result.setSkip(false);
                }
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
        return context.getRequest().getUrl().startsWith(HEAD_URL);
    }
}
