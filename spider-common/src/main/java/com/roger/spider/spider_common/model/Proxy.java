package com.roger.spider.spider_common.model;

import org.apache.http.HttpHost;

public class Proxy {
    private String ip;
    private int port;

    public Proxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public HttpHost buildHttpHost(){
        return new HttpHost(ip,port);
    }


    @Override
    public String toString() {
        return ip +":"+ port;
    }
}
