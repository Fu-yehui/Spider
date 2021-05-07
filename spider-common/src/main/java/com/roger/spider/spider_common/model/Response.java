package com.roger.spider.spider_common.model;

import org.apache.http.Header;
import org.jsoup.nodes.Document;

import java.util.Arrays;

public class Response {
    private final int statusCode;
    private final Header[] headers;
    private Document document;



    public Response(int statusCode, Header[] headers, Document document) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.document = document;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "Response{" +
                "statusCode=" + statusCode +
                ", headers=" + Arrays.toString(headers) +
                ", document=" + document +
                '}';
    }
}
