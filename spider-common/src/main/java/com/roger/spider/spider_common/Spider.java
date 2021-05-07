package com.roger.spider.spider_common;

import com.roger.spider.spider_common.downloader.Downloader;
import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.pipeline.Pipeline;
import com.roger.spider.spider_common.scheduler.Scheduler;
import com.roger.spider.spider_common.utils.ArgUtils;
import com.roger.spider.spider_common.utils.ObjectUtils;
import com.roger.spider.spider_common.workflow.WorkflowFactory;
import com.roger.spider.spider_common.workflow.impl.SingleWorkflowFactory;
import com.roger.spider.spider_common.workflow.impl.WorkflowThreadFactory;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.resources.cldr.ff.LocaleNames_ff;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Spider implements Runnable{

    private static final Logger LOGGER= LoggerFactory.getLogger(Spider.class);

    private static final int STARTING = 2;
    private static final int RUNNING = 1;
    private static final int STOPPED = 0;
    private static final int STOPPING =-1;

    private SpiderConfig spiderConfig;

    private Thread thread;

    private AtomicInteger state;

    private ExecutorService executorService;

    private CountDownLatch finalization;

    private Spider(SpiderConfig spiderConfig) {
        ArgUtils.notNull(spiderConfig);
        spiderConfig.check();
        this.spiderConfig=spiderConfig;
        this.executorService=buildExecutor();
        this.state=new AtomicInteger(STOPPED);
        this.finalization=new CountDownLatch(getThreadCount());
    }

    public static Spider create(SpiderConfig spiderConfig){
        return new Spider(spiderConfig);
    }

    public void start(){
        if(!state.compareAndSet(STOPPED,STARTING)){
            LOGGER.error("Spider [{}] has started",getName());
            return;
        }
        LOGGER.info("Spider [{}] Startup",getName());
        Thread.currentThread().setName(getName()+"-thread");
        run();
    }

    public void stop(){
        if(!state.compareAndSet(RUNNING,STOPPING) && !state.compareAndSet(STARTING,STOPPING)){
            LOGGER.error("Spider [{}] has stopped",getName());
            return;
        }
        LOGGER.info("Spider [{}] is stopping",getName());
        executorService.shutdownNow();
        closeComponents();
        state.compareAndSet(STOPPING,STOPPED);
        LOGGER.info("spider [{}] has stopped",getName());
    }
    @Override
    public void run() {
        if(state.get() != STARTING){
            throw new IllegalArgumentException("The current state is not starting");
        }
        LOGGER.info("Spider [{}] is running",getName());
        WorkflowFactory workflowFactory=buildWorkflowFactory(spiderConfig);
        for(int i=0;i<getThreadCount();i++){
            executorService.execute(workflowFactory.newWorkflow());
        }
        state.compareAndSet(STARTING,RUNNING);
        try{
            finalization.await();
        }catch (InterruptedException e){
            LOGGER.warn("Thread [{}] interrupted",Thread.currentThread().getName());
        }
        stop();
    }

    private void closeComponents(){
        destroy(getDownloader());
        destroy(getScheduler());
        destroy(getPageHandler());
        destroy(getPipeline());
    }

    private void destroy(Object object) {
        if (object instanceof Closeable) {
            try {
                ((Closeable) object).close();
            } catch (IOException e) {
                LOGGER.warn("Failed to close [{}]: {}", ObjectUtils.getSimpleName((Closeable)object),e.getMessage());
            }
        }
    }

    public Spider addUrls(String... urls){
        getScheduler().put(Arrays.stream(urls).map(Request::new).toArray(Request[]::new));
        return this;
    }
    public Spider addUrls(Collection<String> urls){
        getScheduler().put(urls.stream().map(Request::new).collect(Collectors.toList()));
        return this;
    }
    public Spider addRequests(Request... requests){
        getScheduler().put(requests);
        return this;
    }
    public Spider addRequests(Collection<Request> requests){
        getScheduler().put(requests);
        return this;
    }

    public Downloader getDownloader(){
        return spiderConfig.getDownloader();
    }
    public Scheduler getScheduler(){
        return spiderConfig.getScheduler();
    }
    public PageHandler getPageHandler(){
        return spiderConfig.getPageHandlerChain();
    }
    public Pipeline getPipeline(){
        return spiderConfig.getPipelineChain();
    }
    public int getThreadCount(){
        return spiderConfig.getThreadCount();
    }
    public String getName(){
        return spiderConfig.getName();
    }

    private WorkflowFactory buildWorkflowFactory(SpiderConfig config){
        return new SingleWorkflowFactory(finalization,config);
    }
    private ExecutorService buildExecutor(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(getThreadCount(),
                getThreadCount(), 1L, TimeUnit.NANOSECONDS,
                new LinkedBlockingQueue<Runnable>(), new WorkflowThreadFactory(getName()));
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
}
