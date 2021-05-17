package com.roger.spider_proxy.fetch.pageHandler;

import com.roger.spider.spider_common.Spider;
import com.roger.spider.spider_common.SpiderConfig;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class IpPageHandlerTest {

    @Autowired
    private IpPageHandler pageHandler;
    @Test
    public void process() {
        String url="https://www.89ip.cn/index_1.html";
        List<String> urls=new ArrayList<String>();
        for(int i=1;i<76;i++){
            urls.add("https://www.89ip.cn/index_"+i+".html");
        }
        SpiderConfig config= SpiderConfig.builder()
                .addPageHandler(pageHandler)
                .build();
        Spider spider=Spider.create(config);
        spider.addUrls(urls);
        spider.start();
    }

    @Test
    public void isSupport() {
        String url1="https://list.proxylistplus.com/Fresh-HTTP-Proxy-List-2";
        String url2="https://www.89ip.cn/index_1.html";
        String url3="https://www.89ip.cn/index_44rhtml";
        System.out.println(pageHandler.isSupport(new Context(new Request(url1),null)));
        System.out.println(pageHandler.isSupport(new Context(new Request(url2),null)));
        System.out.println(pageHandler.isSupport(new Context(new Request(url3),null)));
    }
}