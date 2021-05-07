package com.roger.spider.spider_common.downloader;

import com.roger.spider.spider_common.downloader.provider.ArrayUserAgentProvider;
import com.roger.spider.spider_common.downloader.provider.ProxyProvider;
import com.roger.spider.spider_common.downloader.provider.UserAgentProvider;
import com.roger.spider.spider_common.model.Proxy;

import java.util.HashMap;
import java.util.Map;

public class DownloaderConfig {
    public static final DownloaderConfig DEFAULT_DOWNLOADER_CONFIG=builder().build();
    private Map<String, Map<String,String>> cookies;
    private Map<String,String> headers;
    private int sleepTime;
    private int timeOut;
    private boolean disableCookieManagement;
    private boolean useGzip;
    private String userAgent;
    private Proxy proxy;
    private int defaultMaxPerRoute;
    private int maxTotalConnections;
    private int socketTimeout;
    private int connectTimeout;
    private int connectRequestTimeout;
    private UserAgentProvider userAgentProvider;
    private ProxyProvider proxyProvider;



    public DownloaderConfig(Map<String, Map<String, String>> cookies, Map<String, String> headers, int sleepTime, int timeOut, boolean disableCookieManagement, boolean useGzip, String userAgent, Proxy proxy, int defaultMaxPerRoute, int maxTotalConnections, int socketTimeout, int connectTimeout, int connectRequestTimeout, UserAgentProvider userAgentProvider, ProxyProvider proxyProvider) {
        this.cookies = cookies;
        this.headers = headers;
        this.sleepTime = sleepTime;
        this.timeOut = timeOut;
        this.disableCookieManagement = disableCookieManagement;
        this.useGzip = useGzip;
        this.userAgent = userAgent;
        this.proxy = proxy;
        this.defaultMaxPerRoute = defaultMaxPerRoute;
        this.maxTotalConnections = maxTotalConnections;
        this.socketTimeout = socketTimeout;
        this.connectTimeout = connectTimeout;
        this.connectRequestTimeout = connectRequestTimeout;
        this.userAgentProvider=userAgentProvider;
        this.proxyProvider=proxyProvider;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Map<String, Map<String, String>> getCookies() {
        return cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public boolean isDisableCookieManagement() {
        return disableCookieManagement;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public boolean isUseGzip() {
        return useGzip;
    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getConnectRequestTimeout() {
        return connectRequestTimeout;
    }

    public UserAgentProvider getUserAgentProvider() {
        return userAgentProvider;
    }

    public ProxyProvider getProxyProvider() {
        return proxyProvider;
    }

    public static DownloaderConfigBuilder builder(){
        return new DownloaderConfigBuilder();
    }

    public static class DownloaderConfigBuilder{
        private Map<String, Map<String,String>> cookies=new HashMap<>();
        private Map<String,String> headers=new HashMap<>();
        private int sleepTime=5000;
        private int timeOut=5000;
        private boolean disableCookieManagement=false;
        private boolean useGzip=true;
        private String userAgent="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36";
        private int defaultMaxPerRoute = 100;
        private int maxTotalConnections = 200;
        private int socketTimeout = 2000;
        private int connectTimeout = 2000;
        private int connectRequestTimeout = 500;
        private Proxy proxy;
        private UserAgentProvider userAgentProvider=new ArrayUserAgentProvider();
        private ProxyProvider proxyProvider;

        public DownloaderConfigBuilder setCookies(Map<String, Map<String, String>> cookies) {
            this.cookies = cookies;
            return this;
        }

        public DownloaderConfigBuilder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public DownloaderConfigBuilder setSleepTime(int sleepTime) {
            this.sleepTime = sleepTime;
            return this;
        }

        public DownloaderConfigBuilder setProxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public DownloaderConfigBuilder setTimeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public DownloaderConfigBuilder setDisableCookieManagement(boolean disableCookieManagement) {
            this.disableCookieManagement = disableCookieManagement;
            return this;
        }

        public DownloaderConfigBuilder setUseGzip(boolean useGzip) {
            this.useGzip = useGzip;
            return this;
        }

        public DownloaderConfigBuilder setUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public DownloaderConfigBuilder setDefaultMaxPerRoute(int defaultMaxPerRoute) {
            this.defaultMaxPerRoute = defaultMaxPerRoute;
            return this;
        }

        public DownloaderConfigBuilder setMaxTotalConnections(int maxTotalConnections) {
            this.maxTotalConnections = maxTotalConnections;
            return this;
        }

        public DownloaderConfigBuilder setSocketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public DownloaderConfigBuilder setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public DownloaderConfigBuilder setConnectRequestTimeout(int connectRequestTimeout) {
            this.connectRequestTimeout = connectRequestTimeout;
            return this;
        }

        public DownloaderConfigBuilder setUserAgentProvider(UserAgentProvider userAgentProvider) {
            this.userAgentProvider = userAgentProvider;
            return this;
        }

        public DownloaderConfigBuilder setProxyProvider(ProxyProvider proxyProvider) {
            this.proxyProvider = proxyProvider;
            return this;
        }

        public DownloaderConfig build(){
            return new DownloaderConfig(this.cookies,this.headers,this.sleepTime,this.timeOut,this.disableCookieManagement,this.useGzip,this.userAgent,this.proxy,this.defaultMaxPerRoute, this.maxTotalConnections, this.socketTimeout, this.connectTimeout, this.connectRequestTimeout,this.userAgentProvider,this.proxyProvider);
        }
    }
}
