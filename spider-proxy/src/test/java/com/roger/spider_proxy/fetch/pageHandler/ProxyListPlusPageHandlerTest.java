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
public class ProxyListPlusPageHandlerTest {
    @Autowired
    private ProxyListPlusPageHandler pageHandler;

    @Test
    public void process() {
        String url="https://list.proxylistplus.com/Fresh-HTTP-Proxy-List-";
        List<String> urls=new ArrayList<String>();
        for(int i=1;i<6;i++){
            urls.add(url+i);
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
        String url="https://list.proxylistplus.com/Fresh-HTTP-Proxy-List-2";
        System.out.println(pageHandler.isSupport(new Context(new Request(url),null)));
    }
}