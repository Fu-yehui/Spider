package com.roger.spider_proxy.fetch.pipeline;

;
import com.roger.spider.spider_common.model.Proxy;
import com.roger.spider.spider_common.model.Result;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class RedisConsumerTest {

    @Autowired
    private RedisPipeline redisPipeline;
    @Test
    public void consume() throws Throwable {
        List<Proxy> proxies = new ArrayList<>();
        proxies.add(new Proxy("127.0.0.1",8888));
        proxies.add(new Proxy("192.128.0.6",9832));
        proxies.add(new Proxy("112.8.0.6",9002));
        Result result=new Result();
        result.set("proxies",proxies);
        redisPipeline.process(result);
    }
}