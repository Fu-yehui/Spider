package com.roger.spider_proxy.check;

import com.roger.spider_proxy.dao.RedisDao;
import com.roger.spider_proxy.entity.Proxy;

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


    private static final String TEST_URL="https://www.kuaidaili.com";
    private static CloseableHttpClient client=HttpClients.createDefault();

    public ProxyCheckTask(Proxy proxy,RedisDao redisDao){
        this.proxy=proxy;
        this.redisDao=redisDao;

    }

    @Override
    public void run() {
        LOGGER.info(" [{}] is testing [{}]",Thread.currentThread().getName(),proxy.toString());

        long start=System.currentTimeMillis();
        try {
            HttpHost host = new HttpHost(proxy.getIp(), proxy.getPort());
            RequestBuilder requestBuilder = RequestBuilder.create("GET")
                    .setUri(TEST_URL)
                    .setConfig(RequestConfig.custom().setProxy(host).build());
            CloseableHttpResponse response = client.execute(requestBuilder.build());
            if(response.getStatusLine().getStatusCode()==200){
                redisDao.max(proxy);
            }else{
                redisDao.decrease(proxy);
            }
            System.out.println(System.currentTimeMillis()-start);

        }catch (Exception e){
//            LOGGER.error(e.getMessage(),e);
            LOGGER.debug(e.getMessage()+proxy);
            redisDao.decrease(proxy);
            System.out.println(System.currentTimeMillis()-start);

        }
    }
}
