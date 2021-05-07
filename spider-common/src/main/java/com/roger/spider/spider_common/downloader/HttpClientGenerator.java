package com.roger.spider.spider_common.downloader;

import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.4.0
 */
public class HttpClientGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientGenerator.class);


    public HttpClientGenerator() {

    }



    public CloseableHttpClient getClient(DownloaderConfig downloaderConfig) {
        return generateClient(downloaderConfig);
    }

    private CloseableHttpClient generateClient(DownloaderConfig downloaderConfig) {


        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(downloaderConfig.getSocketTimeout())
                .setConnectTimeout(downloaderConfig.getConnectTimeout())
                .setConnectionRequestTimeout(downloaderConfig.getConnectRequestTimeout())
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                .setUserAgent(downloaderConfig.getUserAgent())
                .setMaxConnPerRoute(downloaderConfig.getDefaultMaxPerRoute())
                .setMaxConnTotal(downloaderConfig.getMaxTotalConnections())
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .setConnectionManager(generateConnectionManager(downloaderConfig))
                .setProxy(downloaderConfig.getProxy() != null ? downloaderConfig.getProxy().buildHttpHost() : null);

        generateCookie(httpClientBuilder,downloaderConfig);
        generateHeader(httpClientBuilder,downloaderConfig);
        return httpClientBuilder.build();
        
        
    }


    private HttpClientConnectionManager generateConnectionManager(DownloaderConfig downloaderConfig) {
        PoolingHttpClientConnectionManager connectionManager;
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", buildSSLConnectionSocketFactory())
                .build();

        connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).build());
        connectionManager.setDefaultMaxPerRoute(downloaderConfig.getDefaultMaxPerRoute());
        return connectionManager;
    }

    private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
        try {
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, (chain, authType) -> true).build();

            return new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | RuntimeException e) {
            LOGGER.warn("Failed to build SSLConnectionSocketFactory");
        }

        return SSLConnectionSocketFactory.getSocketFactory();
    }


    private void generateCookie(HttpClientBuilder httpClientBuilder, DownloaderConfig downloaderConfig) {
        if (downloaderConfig.isDisableCookieManagement()) {
            httpClientBuilder.disableCookieManagement();
            return;
        }
        CookieStore cookieStore = new BasicCookieStore();
        for (Map.Entry<String, Map<String, String>> domainEntry : downloaderConfig.getCookies().entrySet()) {
            for (Map.Entry<String, String> cookieEntry : domainEntry.getValue().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                cookie.setDomain(domainEntry.getKey());
                cookieStore.addCookie(cookie);
            }
        }
        httpClientBuilder.setDefaultCookieStore(cookieStore);
    }

    public void generateHeader(HttpClientBuilder httpClientBuilder, DownloaderConfig downloaderConfig) {
        if(downloaderConfig.getHeaders() != null){
            List<BasicHeader> headers=new ArrayList<>();
            for(Map.Entry<String,String> header : downloaderConfig.getHeaders().entrySet()){
                headers.add(new BasicHeader(header.getKey(),header.getValue()));
            }
            httpClientBuilder.setDefaultHeaders(headers);
        }
    }
}
