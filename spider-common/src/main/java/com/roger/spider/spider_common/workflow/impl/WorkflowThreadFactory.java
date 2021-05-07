package com.roger.spider.spider_common.workflow.impl;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkflowThreadFactory implements ThreadFactory {
    private final AtomicInteger id=new AtomicInteger(1);
    private final String namePrefix;
    private final ThreadGroup group;

    public WorkflowThreadFactory(String name) {
        SecurityManager s=System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = name + "-workflowThread-";
    }

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread t=new Thread(group,r,namePrefix+id.getAndIncrement(),0);
        t.setDaemon(false);
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
