package com.roger.QuestionBankSpider.webmagic.pageProcessor;

import com.roger.QuestionBankSpider.webmagic.downloader.FixHttpClientDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class LQPageProcessor implements PageProcessor {
    private final Logger LOGGER= LoggerFactory.getLogger(this.getClass());
    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(5000)
            .setUserAgent("Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:84.0) Gecko/20100101 Firefox/84.0");

    @Override
    public void process(Page page) {
        System.out.println(page.getUrl());
        page.putField("title",page.getHtml().xpath("//div[@class='contest_msg_title']/p[1]/text()"));
        System.out.println("close");
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new LQPageProcessor())
                .setDownloader(new FixHttpClientDownloader())
                .addUrl("https://www.dotcpp.com/oj/problem1023.html")
                .thread(1)
                .run();
    }
}
