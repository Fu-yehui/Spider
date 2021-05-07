package com.roger.spider.spider_common.downloader.provider;

import java.util.ArrayList;
import java.util.List;

public enum UserAgentEnum {
    SAFARI_FOR_MAC("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50"),
    IE_9_FOR_WIN ("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;"),
    IE_8_FOR_WIN ( "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)"),
    IE_7_FOR_WIN ( "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)"),
    FIREFOX_FOR_MAC ( "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1"),
    OPERA_FOR_MAC( "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11"),
    CHROME_FOR_LINUX( "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36"),
    CHROME_FOR_MAC ( "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36"),
    TENCENT_TT   ( "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)"),
    THE_WORLD    ( "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; The World)"),
    SOUGOU       ( "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)"),
    QIHU_360     ( "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)");
    private String description;

    public String getDescription() {
        return description;
    }

    private UserAgentEnum(String description){
        this.description=description;
    }

    public static List<String> all(){
        List<String> values=new ArrayList<>();
        for(UserAgentEnum userAgentEnum:values()){
            values.add(userAgentEnum.getDescription());
        }
        return values;
    }
    @Override
    public String toString() {
        return description;
    }
}
