package com.roger.spider.spider_common.downloader.provider;

import java.util.List;
import java.util.Random;

public class ArrayUserAgentProvider implements UserAgentProvider {
    private List<String> userAgents=UserAgentEnum.all();
    private Random random=new Random();
    private final int range=userAgents.size();

    @Override
    public String provide() {
        return userAgents.get(random.nextInt(range));
    }
}
