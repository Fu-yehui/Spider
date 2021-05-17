package com.roger.spider.web.spider;

import com.roger.spider.spider_common.downloader.provider.ProxyProvider;
import com.roger.spider.spider_common.model.Proxy;
import com.roger.spider_proxy.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisProxyProvider implements ProxyProvider {
    @Autowired
    private RedisDao redisDao;
    @Override
    public Proxy provide() {
        return redisDao.random();
    }
}
