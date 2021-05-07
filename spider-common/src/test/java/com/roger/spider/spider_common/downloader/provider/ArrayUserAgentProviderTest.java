package com.roger.spider.spider_common.downloader.provider;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ArrayUserAgentProviderTest {

    private UserAgentProvider provider=new ArrayUserAgentProvider();
    private Map<String,Integer> counter=new HashMap<>();
    @Test
    public void provide() {
        for(int i=0;i<Integer.MAX_VALUE;i++){
//            System.out.println(provider.provide());
        count(provider.provide());
        }

        for(Map.Entry<String,Integer> entry : counter.entrySet()){
            System.out.println(entry.getValue()+"     "+entry.getKey());
//            System.out.format("%s85  %d",entry.getKey(),entry.getValue());
//            System.out.println();
        }
    }

    private void count(String userAgent){
        if(counter.containsKey(userAgent)){
            counter.put(userAgent,counter.get(userAgent)+1);
        }else{
            counter.put(userAgent,1);
        }

    }
}