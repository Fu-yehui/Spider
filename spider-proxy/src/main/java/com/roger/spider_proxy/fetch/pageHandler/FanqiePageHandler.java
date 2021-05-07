package com.roger.spider_proxy.fetch.pageHandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiepuhuan.reptile.downloader.model.Proxy;
import com.xiepuhuan.reptile.handler.ResponseHandler;
import com.xiepuhuan.reptile.model.Request;
import com.xiepuhuan.reptile.model.ResponseContext;
import com.xiepuhuan.reptile.model.Result;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class FanqiePageHandler implements ResponseHandler {
    private static String MAIN_URL="http://x.fanqieip.com";
    private static String regex="http://x\\.fanqieip\\.com/index\\.php\\?s=/Api/IpManager/adminFetchFreeIpRegionInfoList&uid=15075&ukey=24896d3c212f5b27ea1a6b4f62977b5b&limit=20&format=1&page=\\d";
    private static Pattern pattern = Pattern.compile(regex);
    @Override
    public List<Request> handle(ResponseContext responseContext, Result result) throws Throwable {
        if(pattern.matcher(responseContext.getRequest().getUrl()).matches()){
            String content=responseContext.getResponse().getContent().getHtmlContent().body().text();
            JSONObject jsonObject=JSONObject.parseObject(content);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            List<Proxy> list =jsonArray.stream()
                    .map(json -> {
                        String jsonString=String.valueOf(json);
                        JSONObject item= JSONObject.parseObject(jsonString);
                        return new Proxy(item.getString("ip"),Integer.parseInt(item.getString("port")));
                    })
                    .collect(toList());
            result.setResult("proxies",list);
        }
        return null;
    }

    @Override
    public boolean isSupport(ResponseContext responseContext) {
        String currentUrl=responseContext.getRequest().getUrl();
        return currentUrl.startsWith(MAIN_URL);

    }

    public static void main(String[] args) {
        String url="http://x.fanqieip.com/index.php?s=/Api/IpManager/adminFetchFreeIpRegionInfoList&uid=15075&ukey=24896d3c212f5b27ea1a6b4f62977b5b&limit=20&format=1&page=2";
//        String url1="http://x.fanqieip.com/index.php?s=/Api/IpManager/adminFetchFreeIpRegionInfoList&uid=15075&ukey=24896d3c212f5b27ea1a6b4f62977b5b&limit=20&format=1&page=a";
        String url1="http://x.fanqieip.com/";
        String fakeUrl="https://www.kuaidaili.com";
//        System.out.println(url.startsWith(MAIN_URL));
//        System.out.println(url.startsWith(fakeUrl));
        System.out.println(pattern.matcher(url).matches());
        System.out.println(pattern.matcher(url1).matches());
    }
}
