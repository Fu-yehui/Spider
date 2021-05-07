package com.roger.spider.spider_common.pipeline;

import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.handler.PageHandlerChain;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Result;
import com.roger.spider.spider_common.utils.ArgUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PipelineChain implements CloseablePipeline {
    private List<Pipeline> pipelines;

    public PipelineChain(){
        pipelines=new ArrayList<>();
    }

    public PipelineChain(List<Pipeline> pipelines){
        this.pipelines=pipelines;
    }
    public PipelineChain(Pipeline pipeline){
        pipelines=new ArrayList<>();
        if(Optional.ofNullable(pipeline).isPresent()){
            pipelines.add(pipeline);
        }
    }

    public PipelineChain addPipeline(Pipeline pipeline){
        Optional.ofNullable(pipeline).ifPresent(pipelines::add);
        return this;
    }
    public PipelineChain addPipeline(Pipeline[] p){
        if (Optional.ofNullable(p).isPresent()) {
            Arrays.stream(p).forEach(this::addPipeline);
        }
        return this;
    }
    public PipelineChain addPipeline(Collection<Pipeline> p){
        if (Optional.ofNullable(p).isPresent()) {
            p.stream().forEach(this::addPipeline);
        }
        return this;
    }

    /**
     * Process the results
     *
     * @param result
     */
    @Override
    public void process(Result result) throws Throwable {


            if(result != null) {
                if (!ArgUtils.isEmpty(pipelines)) {
                    for (Pipeline pipeline : pipelines) {
                        pipeline.process(result);
                    }
                }
            }

    }

    @Override
    public void close() throws IOException {
        for(Pipeline pipeline : pipelines){
            if(pipeline instanceof CloseablePipeline){
                ((CloseablePipeline) pipeline).close();
            }
        }
    }
}
