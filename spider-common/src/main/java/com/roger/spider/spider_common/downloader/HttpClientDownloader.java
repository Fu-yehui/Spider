package com.roger.spider.spider_common.downloader;

import com.roger.spider.spider_common.downloader.provider.*;
import com.roger.spider.spider_common.model.Proxy;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Map;

/**
 * @author roger
 */
public class HttpClientDownloader implements Downloader {

    private static Logger logger = LoggerFactory.getLogger(HttpClientDownloader.class);
    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();
    private CloseableHttpClient httpClient ;
    private DownloaderConfig downloaderConfig;


    public HttpClientDownloader(){
        this(DownloaderConfig.DEFAULT_DOWNLOADER_CONFIG);
    }

    public HttpClientDownloader(DownloaderConfig downloaderConfig){
        this.downloaderConfig=downloaderConfig;
        httpClient=httpClientGenerator.getClient(downloaderConfig);
    }


    @Override
    public Response download(Request request) throws IOException {
        if(request == null){
            return null;
        }


        RequestBuilder requestBuilder= RequestBuilder.create(request.getMethod())
                .setUri(request.getUrl());

        if(request.getHeaders() != null){
            for(Map.Entry<String,String> header : request.getHeaders().entrySet()){
                requestBuilder.addHeader(new BasicHeader(header.getKey(),header.getValue()));
            }
        }
        if(downloaderConfig.getUserAgentProvider()!=null){
            requestBuilder.setHeader(UserAgentProvider.USER_AGENT_NAME, downloaderConfig.getUserAgentProvider().provide());
        }else{
            requestBuilder.setHeader(UserAgentProvider.USER_AGENT_NAME, downloaderConfig.getUserAgent());
        }
        if(downloaderConfig.getProxyProvider()!=null){
            requestBuilder.setConfig(RequestConfig.custom().setProxy(downloaderConfig.getProxyProvider().provide().buildHttpHost()).build());
        }else if(downloaderConfig.getProxy()!=null){
            requestBuilder.setConfig(RequestConfig.custom().setProxy(downloaderConfig.getProxy().buildHttpHost()).build());
        }
        CloseableHttpResponse httpResponse=httpClient.execute(requestBuilder.build());

        return new Response(httpResponse.getStatusLine().getStatusCode(),httpResponse.getAllHeaders(),handleResponse(httpResponse));


    }



    private Document handleResponse( HttpResponse httpResponse) throws IOException {
        String content = EntityUtils.toString(httpResponse.getEntity());
        return Jsoup.parse(content);
    }


}
