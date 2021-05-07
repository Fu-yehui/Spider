package com.roger.spider_proxy.fetch.consumer;

import com.roger.spider_proxy.dao.RedisDao;
import com.roger.spider_proxy.entity.Proxy;
import com.xiepuhuan.reptile.consumer.Consumer;
import com.xiepuhuan.reptile.model.Result;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RedisConsumer implements Consumer {
    @Autowired
    private RedisDao redisDao;



    @Override
    public void consume(Result result) throws Throwable {
        List<com.xiepuhuan.reptile.downloader.model.Proxy> list=result.getResult("proxies");
        if(CollectionUtils.isNotEmpty(list)){
            list.stream()
                    .map(proxy -> {return new Proxy(proxy.getHost(),proxy.getPort());})
                    .forEach(redisDao::add);
        }
    }
}
