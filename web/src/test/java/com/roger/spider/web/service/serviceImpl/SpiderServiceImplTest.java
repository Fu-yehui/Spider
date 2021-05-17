package com.roger.spider.web.service.serviceImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class SpiderServiceImplTest {

    @Autowired
    private SpiderServiceImpl spiderService;

    @Test
    public void fetch() throws InterruptedException {
        System.out.println("1");
        spiderService.fetch();

//        Thread.sleep(10000000);
        stop();

    }

    @Test
    public void stop() throws InterruptedException {
        Thread.sleep(20000);

        System.out.println(spiderService.count());
        spiderService.stop();
        System.out.println(spiderService.count());
    }
}