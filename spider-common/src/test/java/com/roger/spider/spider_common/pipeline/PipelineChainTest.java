package com.roger.spider.spider_common.pipeline;

import com.roger.spider.spider_common.model.Result;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PipelineChainTest {
    private PipelineChain chain=new PipelineChain(new ConsolePipeline());

    @Test
    public void addPipeline() {
        Pipeline pipeline=null;
        chain.addPipeline(pipeline);
    }

    @Test
    public void testAddPipeline() {
        Pipeline[] pipelines=null;
        chain.addPipeline(pipelines);
    }

    @Test
    public void testAddPipeline1() {
        List<Pipeline> pipelineList=null;
        chain.addPipeline(pipelineList);
    }

    @Test
    public void process() throws Throwable {
        Result result=new Result();
        result.set("key","value");
        chain.process(result);
    }

}