package com.roger.spider.spider_common.downloader.provider;

import com.roger.spider.spider_common.model.Proxy;

public class ProxyProviderTest implements ProxyProvider {
    @Override
    public Proxy provide() {
        return new Proxy("182.23.29.114",8080);
    }
}
