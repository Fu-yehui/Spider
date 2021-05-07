package com.roger.spider.spider_common.model;

public class Context {
    private Request request;
    private Response response;

    public Context(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Context{" +
                "request=" + request +
                ", response=" + response +
                '}';
    }
}
