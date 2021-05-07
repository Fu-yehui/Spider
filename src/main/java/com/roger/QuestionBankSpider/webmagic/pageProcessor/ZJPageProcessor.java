package com.roger.QuestionBankSpider.webmagic.pageProcessor;

import com.roger.QuestionBankSpider.enums.GradeEnum;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

public class ZJPageProcessor implements PageProcessor {
    private final Logger LOGGER=LoggerFactory.getLogger(this.getClass());
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        String questionUrl="https://www.zujuan.com/question/detail-\\d++.shtml";
        String mapListUrl="http://tiku.zujuan.com/map/\\w+-\\d-\\d+-\\d-\\d.shtml";
        String paperListUrl="http://tiku.zujuan.com/map/paper-\\d-\\d+-\\d-\\d+.shtml";
        String paperUrl="http://tiku.zujuan.com/map/paper-\\d+.shtml";
        page.addTargetRequests(page.getHtml().links().regex(questionUrl).all());
        page.addTargetRequests(page.getHtml().links().regex(mapListUrl).all());
        page.addTargetRequests(page.getHtml().links().regex(paperListUrl).all());
        page.addTargetRequests(page.getHtml().links().regex(paperUrl).all());
        if(page.getUrl().regex(questionUrl).match()){

        }else if(page.getUrl().regex(paperUrl).match()){
            List<String> questionsUrl=page.getHtml().links().regex(questionUrl).all();
            String title="";
            String url=page.getUrl().toString();

        }else if(page.getUrl().regex(paperListUrl).match()){
             String str=page.getUrl().regex("\\d-\\d+-\\d-\\d+").toString();
             String[] strs=str.split("-");
             if(Arrays.isNullOrEmpty(strs)||strs.length!=4){
                 LOGGER.warn("the url parsing error");
             }
             List<String> papersUrl=page.getHtml().links().regex(paperUrl).all();
            String grade= GradeEnum.valueOf(strs[0]).getDescription();
            String subject=GradeEnum.valueOf(strs[1]).getDescription();
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new ZJPageProcessor())
                .addUrl("https://github.com/code4craft")
                .thread(5)
                .run();
    }
}

