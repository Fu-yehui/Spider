package com.roger.spider.spider_common.pipeline;

import com.roger.spider.spider_common.model.Result;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ConsolePipeline implements Pipeline {

    private AtomicLong count=new AtomicLong(0);
    /**
     * Process the results
     *
     * @param result
     */
    @Override
    public void process(Result result) {
        System.out.println(String.format("Consuming the %dth result\n", count.incrementAndGet()));
        for(Map.Entry<String,Object> entry : result.getFields().entrySet()){
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }
}
