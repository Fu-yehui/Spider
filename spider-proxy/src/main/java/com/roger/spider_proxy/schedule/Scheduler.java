package com.roger.spider_proxy.schedule;

import com.roger.spider_proxy.check.Checker;
import com.roger.spider_proxy.fetch.Fetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Scheduler implements Closeable {

    private static Logger LOGGER= LoggerFactory.getLogger(Scheduler.class);
    private AtomicBoolean END_FLAG=new AtomicBoolean(false);

    @Autowired
    private Fetcher fetcher;

    @Autowired
    private Checker checker;

    private ScheduledExecutorService scheduledExecutorService;

    private CountDownLatch latch = new CountDownLatch(1);

    public Scheduler(){
        scheduledExecutorService= Executors.newScheduledThreadPool(3);

    }


    public void end() {
        latch.countDown();
        LOGGER.info("The [{}] is closing",this.getClass().getSimpleName());
    }


    public void schedule(){


        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                fetcher.fetch();
            }catch (IOException e) {
                LOGGER.error(e.getMessage(),e);
            }
        },0,30, TimeUnit.MINUTES);

        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            checker.asyncCheck();
        },0,10, TimeUnit.MINUTES);


        try {
            latch.await();
            close();
        } catch (InterruptedException e) {
            LOGGER.warn("Thread [{}] interrupted", Thread.currentThread().getName());
        }


    }


    @Override
    public void close()  {
        if(scheduledExecutorService!=null){
            try{
                scheduledExecutorService.shutdownNow();
            }catch (Exception e){
                LOGGER.error("Failed to close [{}]: {}",this.getClass().getSimpleName(),e.getMessage());;
            }

        }
        LOGGER.info("The [{}] has closed successful",this.getClass().getSimpleName());

        if(checker!=null) {
            checker.close();
        }
        if(fetcher!=null) {
            fetcher.close();
        }
    }
}
