package com.roger.spider_proxy.fetch.pipeline;

import com.roger.spider.spider_common.model.Proxy;
import com.roger.spider.spider_common.model.Result;
import com.roger.spider.spider_common.pipeline.Pipeline;
import com.roger.spider_proxy.dao.RedisDao;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RedisPipeline implements Pipeline {
    @Autowired
    private RedisDao redisDao;


    /**
     * Process the results
     *
     * @param result
     */
    @Override
    public void process(Result result) throws Throwable {
        if (result != null && !result.isSkip()) {
            List<Proxy> list = result.get("proxies");
            if (CollectionUtils.isNotEmpty(list)) {
                list.stream().forEach(redisDao::add);
            }
        }

    }


}
