package com.roger.spider.spider_common.downloader;

import com.roger.spider.spider_common.model.Request;
import com.roger.spider.spider_common.model.Response;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class HttpClientDownloaderTest {

    @Test
    public void download() throws IOException {
        String url="https://www.dotcpp.com/oj/problemset.php?page=62";
        Downloader downloader=new HttpClientDownloader();
        Request request=new Request(url);
        Response response=downloader.download(request);
        System.out.println(response.getStatusCode());
        System.out.println(Arrays.toString(response.getHeaders()));

    }
}