package com.roger.spider.spider_common.handler;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PageHandlerChainTest {

    private PageHandlerChain chain=new PageHandlerChain();
    @Test
    public void addPageHandler() {
        PageHandler[] pageHandlers=null;
        chain.addPageHandler(pageHandlers);
    }

    @Test
    public void testAddPageHandler() {
        PageHandler pageHandler=null;
        chain.addPageHandler(pageHandler);
    }

    @Test
    public void testAddPageHandler1() {
        List<PageHandler> list=null;
        chain.addPageHandler(list);
    }

    @Test
    public void process() throws Throwable {
        chain.addPageHandler(new Test1()).addPageHandler(new Test2());
        chain.process(null,null);
    }

    @Test
    public void isSupport() {
    }
}