package com.roger.spider.web.spider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class RedisProxyProviderTest {

    @Autowired
    private RedisProxyProvider proxyProvider;

    @Test
    public void provide() {
        for(int i=0;i<20;i++){
            System.out.println(proxyProvider.provide());
        }
    }
}