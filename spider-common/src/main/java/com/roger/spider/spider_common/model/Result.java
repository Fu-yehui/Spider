package com.roger.spider.spider_common.model;

import java.util.HashMap;
import java.util.Map;

public class Result  {
    private Map<String,Object> fields;
    private boolean skip;

    public Result() {
        fields=new HashMap<>();
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public <T> T get(String key){
        Object o=fields.get(key);
        if(o!=null){
            return (T)o;
        }
        return null;
    }

    public <T> Result set(String key,T value){
        fields.put(key,value);
        return this;
    }

    @Override
    public String toString() {
        return "Result{" +
                "fields=" + fields +
                ", skip=" + skip +
                '}';
    }
}
