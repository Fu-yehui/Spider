package com.roger.spider_proxy.check;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class CheckerTest {

    @Autowired
    private Checker checker;

    @Test
    public void check() throws IOException {
        long start=System.currentTimeMillis();
        checker.check();
        System.out.println(System.currentTimeMillis()-start);
        checker.close();
//        while (!checker.checkProxyValidityExecutor.isTerminated()){
//
//        }
//        System.out.println("main thread is closed");

    }


    @Test
    public void asyncCheck() throws IOException {
        long start=System.currentTimeMillis();
        checker.asyncCheck();
        System.out.println(System.currentTimeMillis()-start);
        checker.close();
//        while (!checker.checkProxyValidityExecutor.isTerminated()){
//
//        }
//        System.out.println("main thread is closed");
    }
}