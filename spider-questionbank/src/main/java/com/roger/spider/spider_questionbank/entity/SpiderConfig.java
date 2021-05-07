package com.roger.spider.spider_questionbank.entity;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class SpiderConfig {

    @Min(value = 1 ,message = "threadCount必须 >= 1")
    private int threadCount;
    @Min(value = 0 ,message = "sleepTime >= 0")
    private int sleepTime;
    @Min(value = 0 ,message = "retryCount >= 0")
    private int retryCount;

    private boolean useProxy;

//    @Pattern(regexp = " /^((ht|f)tps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:\\/~\\+#]*[\\w\\-\\@?^=%&\\/~\\+#])?$/;",message = "URL必须符合标准格式")
    @URL
    private String url;

    public SpiderConfig(){

    }
    public SpiderConfig(@Min(value = 1, message = "threadCount必须 >= 1") int threadCount, @Min(value = 0, message = "sleepTime >= 0") int sleepTime, @Min(value = 0, message = "retryCount >= 0") int retryCount, boolean useProxy, @URL String url) {
        this.threadCount = threadCount;
        this.sleepTime = sleepTime;
        this.retryCount = retryCount;
        this.useProxy = useProxy;
        this.url = url;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public boolean isUseProxy() {
        return useProxy;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SpiderConfig{" +
                "threadCount=" + threadCount +
                ", sleepTime=" + sleepTime +
                ", retryCount=" + retryCount +
                ", useProxy=" + useProxy +
                ", url='" + url + '\'' +
                '}';
    }
}
