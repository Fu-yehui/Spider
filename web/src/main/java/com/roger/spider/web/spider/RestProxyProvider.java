package com.roger.spider.web.spider;

import com.alibaba.fastjson.JSONObject;
import com.roger.spider.spider_common.downloader.provider.ProxyProvider;
import com.roger.spider.spider_common.model.Proxy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestProxyProvider implements ProxyProvider {
    private RestTemplate restTemplate=new RestTemplate();
    private static final String URL="http://localhost:8080/proxy/random";

    @Override
    public Proxy provide() {
        String str=restTemplate.getForObject(URL,String.class);
        return convert(str);
    }
    private Proxy convert(String str){
        JSONObject jsonObject= JSONObject.parseObject(str);
        return new Proxy(jsonObject.getString("ip"),jsonObject.getInteger("port"));
    }
}
