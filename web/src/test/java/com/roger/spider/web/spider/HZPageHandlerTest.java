package com.roger.spider.web.spider;

import com.roger.spider.spider_common.Spider;
import com.roger.spider.spider_common.SpiderConfig;
import com.roger.spider.spider_common.downloader.HttpClientDownloader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class HZPageHandlerTest {
    @Autowired
    private HZPageHandler pageHandler;

    private static final String HEAD_URL="http://acm.hdu.edu.cn/";
//    private static final String LIST_URL="http://acm\\.hdu\\.edu\\.cn/listproblem\\.php\\?vol=\\d+";
//    private static final Pattern LIST_URL_PATTERN=Pattern.compile(LIST_URL);
//    private static final String DETAIL_URL="http://acm\\.hdu\\.edu\\.cn/showproblem\\.php\\?pid=\\d+";
//    private static final Pattern DETAIL_URL_PATTERN=Pattern.compile(DETAIL_URL);

    @Test
    public void process() {
//        String listUrl="http://acm.hdu.edu.cn/listproblem.phpvol=1";
//        String detailUrl="http://acm.hdu.edu.cn/showproblem.php?pid=1032";
//        System.out.println(LIST_URL_PATTERN.matcher(listUrl).matches());
//        System.out.println(DETAIL_URL_PATTERN.matcher(detailUrl).matches());

        SpiderConfig config = SpiderConfig.builder()
                .setName("HZ-spider")
                .setThreadCount(1)
                .setSleepTime(5000)
                .addPageHandler(pageHandler)
                .build();
        String url="http://acm.hdu.edu.cn/listproblem.php?vol=15";
        Spider spider = Spider.create(config).addUrls(url);
        spider.start();
    }

    @Test
    public void isSupport() {
        String url1 = "http://acm.hdu.edu.cn/listproblem.php?vol=15";
        String url2 = "http://acm.hdu.edu.cn/showproblem.php?pid=3764";
        String url3 = "https://www.dotcpp.com/oj/problemset.php?page=1";
        System.out.println(url1.startsWith(HEAD_URL));
        System.out.println(url2.startsWith(HEAD_URL));
        System.out.println(url3.startsWith(HEAD_URL));
    }
}