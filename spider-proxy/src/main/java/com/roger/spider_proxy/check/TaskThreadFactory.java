package com.roger.spider_proxy.check;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskThreadFactory implements ThreadFactory {
    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */

    private final AtomicInteger counter=new AtomicInteger(1);
    private final String namePrefix;

    public TaskThreadFactory(String poolName){
        namePrefix=poolName;
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread t=new Thread(r,namePrefix+"-"+ counter.getAndIncrement());
        t.setDaemon(false);
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
