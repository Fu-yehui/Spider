package com.roger.spider_proxy.check;

import com.roger.spider.spider_common.model.Proxy;
import com.roger.spider_proxy.dao.RedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Autowired;


public class AsyncProxyCheckTask implements Runnable {

    private static Logger LOGGER= LoggerFactory.getLogger(ProxyCheckTask.class);

    private Proxy proxy;

    private RedisDao redisDao;
//    private static RedisDao redisDao=new RedisDao("8.129.23.141",6379);


    private static final String TEST_URL="https://www.dotcpp.com/oj/problemset.html";
    private static CloseableHttpAsyncClient asyncClient;
    private RequestBuilder  requestBuilder;

    public AsyncProxyCheckTask(Proxy proxy,RedisDao redisDao){
        this.proxy=proxy;
        this.redisDao=redisDao;
        //配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
                setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setSoKeepAlive(true)
                .build();
        //设置连接池大小
        ConnectingIOReactor ioReactor=null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            LOGGER.error(e.getMessage(),e);
        }
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        int count = (int)redisDao.count();
        connManager.setMaxTotal(count);
        connManager.setDefaultMaxPerRoute(count);


        asyncClient = HttpAsyncClients.custom().
                setConnectionManager(connManager)
                .build();
        asyncClient.start();
        requestBuilder=RequestBuilder
                .create("GET")
                .setUri(TEST_URL);
    }

    @Override
    public void run() {
        LOGGER.info(" [{}] is testing [{}]",Thread.currentThread().getName(),proxy.toString());

        requestBuilder.setConfig(RequestConfig
                .custom()
                .setProxy(proxy.buildHttpHost())
                .setConnectTimeout(50000)
                .setSocketTimeout(50000)
                .setConnectionRequestTimeout(1000)
                .build()
        );

        asyncClient.execute(requestBuilder.build(), new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    redisDao.max(proxy);
                }else{
                    redisDao.decrease(proxy);
                }

            }

            @Override
            public void failed(Exception e) {
                LOGGER.debug(e.getMessage()+proxy);
                redisDao.decrease(proxy);

            }

            @Override
            public void cancelled() {
                LOGGER.debug(proxy+" checkProxyValidity cancelled");

            }
        });
    }
}
