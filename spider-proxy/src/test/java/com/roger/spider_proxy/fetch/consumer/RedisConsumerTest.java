package com.roger.spider_proxy.fetch.consumer;

import com.xiepuhuan.reptile.downloader.model.Proxy;
import com.xiepuhuan.reptile.model.Result;
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
    private RedisConsumer redisConsumer;
    @Test
    public void consume() throws Throwable {
        List<Proxy> proxies = new ArrayList<>();
        proxies.add(new Proxy("127.0.0.1",8888));
        proxies.add(new Proxy("192.128.0.6",9832));
        Result result=new Result();
        result.setResult("proxies",proxies);
        redisConsumer.consume(result);
    }
}