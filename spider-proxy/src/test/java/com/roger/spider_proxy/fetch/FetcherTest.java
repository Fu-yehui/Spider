package com.roger.spider_proxy.fetch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class FetcherTest {

    @Autowired
    private Fetcher fetcher;

    @Test
    public void fetch() throws IOException {
        fetcher.fetch();
    }
}