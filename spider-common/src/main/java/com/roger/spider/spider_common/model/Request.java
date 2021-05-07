package com.roger.spider.spider_common.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Request {
    public static final String RETRY_COUNT="retry_count";

    public static final String METHOD_GET="GET";

    public static final String METHOD_POST="POST";

    private String url;

    private String method;

    private volatile Map<String,Object> attributions=new ConcurrentHashMap<>();

    private Map<String,String> cookies=new HashMap<>();

    private Map<String,String> headers=new HashMap<>();

    private long priority;

    public Request(String url) {
        this(url,METHOD_GET);
    }

    public Request(String url,String method){
        this.url=url;
        this.method=method;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Object getAttribution(String key) {
        return attributions.get(key);
    }

    public Request setAttribution(String key,Object value) {
        attributions.put(key,value);
        return this;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Request setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Request setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public long getPriority() {
        return priority;
    }

    public Request setPriority(long priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", attributions=" + attributions +
                ", cookies=" + cookies +
                ", headers=" + headers +
                ", priority=" + priority +
                '}';
    }
}
