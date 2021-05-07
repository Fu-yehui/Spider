package com.roger.spider_proxy.check;

import com.roger.spider_proxy.dao.RedisDao;
import com.roger.spider_proxy.entity.Proxy;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author roger
 */
@Component
public class Checker implements Closeable {

    private static Logger LOGGER= LoggerFactory.getLogger(Checker.class);

    @Autowired
    private RedisDao redisDao;

    private ThreadPoolExecutor checkProxyValidityExecutor;
    private static final String TEST_URL="https://www.kuaidaili.com";

    public Checker(){
        checkProxyValidityExecutor=new ThreadPoolExecutor(50, 100,0L , TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000),new TaskThreadFactory("checkProxyValidityThread"));
    }

    public void check(){
        Set<Proxy> set=redisDao.all();
//        Set<Proxy> set=new HashSet<>();
//        set.add(new Proxy("60.191.11.241",3128));
//        set.add(new Proxy("60.191.11.251",3128));
//        set.add(new Proxy("113.238.142.208",3128));
//        set.add(new Proxy("115.150.81.171",8118));
//        set.add(new Proxy("121.199.76.16",8091));
//        set.add(new Proxy("49.89.85.6",9999));

        for(Proxy proxy: set){
            checkProxyValidityExecutor.execute(new ProxyCheckTask(proxy,redisDao));
        }
    }

    public void asyncCheck(){
//        Set<Proxy> set=new HashSet<>();
//        set.add(new Proxy("60.191.11.241",3128));
//        set.add(new Proxy("60.191.11.251",3128));
        Set<Proxy> set=redisDao.all();
        for(Proxy proxy: set){
            checkProxyValidityExecutor.execute(new AsyncProxyCheckTask(proxy,redisDao));
        }
    }



    public static void main(String[] args) {
        long start=System.currentTimeMillis();
        new Checker().asyncCheck();
        System.out.println(System.currentTimeMillis()-start);
    }



    @Override
    public void close()  {
        if(checkProxyValidityExecutor!=null) {
            try {
                checkProxyValidityExecutor.shutdownNow();
            } catch (Exception e) {
                LOGGER.error("Failed to close [{}]: {}",this.getClass().getSimpleName(),e.getMessage());;
            }
        }
        LOGGER.info("The [{}] has closed successful",this.getClass().getSimpleName());

    }
}


