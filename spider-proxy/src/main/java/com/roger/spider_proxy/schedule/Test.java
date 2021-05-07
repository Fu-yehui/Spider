package com.roger.spider_proxy.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    public  static final AtomicInteger counter=new AtomicInteger(1);
    public static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private  static AtomicBoolean END_FLAG=new AtomicBoolean(false);

    public static void end() {
        END_FLAG.set(true);
    }


    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(3);
        System.out.println("提交时间: " + sdf.format(new Date()));
        scheduledExecutorService.scheduleWithFixedDelay(new TestRunnable(),2,5, TimeUnit.SECONDS);

        while(END_FLAG.get()){
//            scheduledExecutorService.shutdown();
            scheduledExecutorService.shutdownNow();
        }

    }



    static class TestRunnable implements Runnable{




        @Override
        public void run() {
                System.out.println("运行时间: " + sdf.format(new Date()) + "-----第" + counter.getAndIncrement() + "执行");
        }
    }
}

