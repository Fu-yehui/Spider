package com.roger.spider_proxy.fetch.pageHandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.roger.spider.spider_common.handler.PageHandler;
import com.roger.spider.spider_common.model.Context;
import com.roger.spider.spider_common.model.Proxy;
import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Result;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@Component
public class FanqiePageHandler implements PageHandler {
    private static String MAIN_URL="http://x.fanqieip.com";
    private static String regex="http://x\\.fanqieip\\.com/index\\.php\\?s=/Api/IpManager/adminFetchFreeIpRegionInfoList&uid=15075&ukey=24896d3c212f5b27ea1a6b4f62977b5b&limit=20&format=1&page=\\d";
    private static Pattern pattern = Pattern.compile(regex);
    @Override
    public List<Request> process(Context context, Result result) throws Throwable {
        if(pattern.matcher(context.getRequest().getUrl()).matches()){
            String content=context.getResponse().getDocument().body().text();
            JSONObject jsonObject=JSONObject.parseObject(content);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            List<Proxy> list =jsonArray.stream()
                    .map(json -> {
                        String jsonString=String.valueOf(json);
                        JSONObject item= JSONObject.parseObject(jsonString);
                        return new Proxy(item.getString("ip"),Integer.parseInt(item.getString("port")));
                    })
                    .collect(toList());
            if (CollectionUtils.isEmpty(list)){
                result.setSkip(true);
            }else{
                result.set("proxies", list);
            }
        }
        return null;
    }

    @Override
    public boolean isSupport(Context context) {
        String currentUrl=context.getRequest().getUrl();
        return currentUrl.startsWith(MAIN_URL);

    }

}
