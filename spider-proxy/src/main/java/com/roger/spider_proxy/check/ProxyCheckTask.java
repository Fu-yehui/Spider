package com.roger.spider_proxy.check;

import com.roger.spider.spider_common.model.Proxy;
import com.roger.spider_proxy.dao.RedisDao;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyCheckTask implements Runnable {

    private static Logger LOGGER= LoggerFactory.getLogger(ProxyCheckTask.class);

    private Proxy proxy;

//    private static RedisDao redisDao=new RedisDao("8.129.23.141",6379);
    private RedisDao redisDao;


    private static final String TEST_URL="https://www.dotcpp.com/oj/problemset.html";
    private static CloseableHttpClient client=HttpClients.createDefault();

    public ProxyCheckTask(Proxy proxy,RedisDao redisDao){
        this.proxy=proxy;
        this.redisDao=redisDao;

    }

    @Override
    public void run() {
        LOGGER.info(" [{}] is testing [{}]",Thread.currentThread().getName(),proxy.toString());

        try {
            HttpHost host = proxy.buildHttpHost();
            RequestBuilder requestBuilder = RequestBuilder.create("GET")
                    .setUri(TEST_URL)
                    .setConfig(RequestConfig.custom().setProxy(host).build());
            CloseableHttpResponse response = client.execute(requestBuilder.build());
            if(response.getStatusLine().getStatusCode()==200){
                redisDao.max(proxy);
            }else{
                redisDao.decrease(proxy);
            }

        }catch (Exception e){
            LOGGER.warn("Failed to test proxy [{}],{}",proxy,e.getMessage());
            redisDao.decrease(proxy);

        }
    }
}
