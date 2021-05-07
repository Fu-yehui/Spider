package com.roger.spider.spider_common.handler;

import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Result;
import com.roger.spider.spider_common.pipeline.CloseablePipeline;
import com.roger.spider.spider_common.pipeline.Pipeline;
import com.roger.spider.spider_common.utils.ArgUtils;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author roger
 */
public class PageHandlerChain implements CloseablePageHandler{
    private List<PageHandler> handlers;

    public PageHandlerChain(){
        handlers=new ArrayList<>();
    }

    public PageHandlerChain(List<PageHandler> pageHandlers){
        this.handlers=pageHandlers;
    }

    public PageHandlerChain addPageHandler(PageHandler pageHandler){
        Optional.ofNullable(pageHandler).ifPresent(handlers::add);
        return this;
    }
    public PageHandlerChain addPageHandler(PageHandler[] pageHandlers){
        if (Optional.ofNullable(pageHandlers).isPresent()) {
            Arrays.stream(pageHandlers).forEach(this::addPageHandler);
        }
        return this;
    }
    public PageHandlerChain addPageHandler(Collection<PageHandler> pageHandlers){
        if (Optional.ofNullable(pageHandlers).isPresent()) {
            pageHandlers.stream().forEach(this::addPageHandler);
        }
        return this;
    }

    @Override
    public List<Request> process(Context context, Result result) throws Throwable {

        List<Request> requests=new ArrayList<>();
        for(PageHandler handler : handlers){
            if(handler.isSupport(context)){
                List<Request> lists=handler.process(context,result);
                if(!ArgUtils.isEmpty(lists)) {
                    requests.addAll(lists);
                }
            }
        }
        return requests;
    }

    /**
     * Whether the PageHandler supports the current URL
     *
     * @param context
     * @return
     */
    @Override
    public boolean isSupport(Context context) {
        return true;
    }

    @Override
    public void close() throws IOException {
        for(PageHandler handler : handlers){
            if(handler instanceof CloseablePageHandler){
                ((CloseablePageHandler) handler).close();
            }
        }
    }
}
