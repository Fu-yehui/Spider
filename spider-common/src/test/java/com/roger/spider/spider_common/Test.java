package com.roger.spider.spider_common;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {
//        String url="https://ip.ihuan.me/tqdl.html";
        String url="http://www.bjdcfy.com/qita/bylwrws/2017-6/945756.html";
        Connection conn=Jsoup.connect(url);
        Map<String,String> datas=new HashMap<>();
        //num=100&
        // port=&
        // kill_port=&
        // address=&
        // kill_address=&
        // anonymity=&
        // type=&
        // post=&
        // sort=&
        // key=402c21f0fb5e2dd3c4c4f9f2cb63192d
        datas.put("num","3000");
//        datas.put("port","");
//        datas.put("kill_port","");
//        datas.put("address","");
//        datas.put("kill_address","");
//        datas.put("anonymity","");
//        datas.put("type","");
//        datas.put("post","");
//        datas.put("sort","");
        datas.put("key","402c21f0fb5e2dd3c4c4f9f2cb63192d");


        Map<String,String> cookies = new HashMap<>();
        //__cfduid	"d49853784aad54a0fdf279be4bba1e18e1620441184"
        //statistics	"8d3accf422cf6000421adecb7599136f"
        cookies.put("__cfduid","d49853784aad54a0fdf279be4bba1e18e1620441184");
        cookies.put("statistics","8d3accf422cf6000421adecb7599136f");
        Connection.Response response=conn.ignoreContentType(true) // 忽略类型验证
                .followRedirects(true) // 禁止重定向
                .postDataCharset("utf-8")
                .header("Upgrade-Insecure-Requests","1")
                .header("Accept","Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("X-Requested-With","XMLHttpRequest")
                .header("User-Agent","Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:84.0) Gecko/20100101 Firefox/84.0")
                .header("Referer","https://ip.ihuan.me/ti.html")
                .header("Host","ip.ihuan.me")
                .header("Origin","https://ip.ihuan.me")
                .data(datas)
                .cookies(cookies)
                .method(Connection.Method.POST)
                .execute();
        Document document=response.parse();
//        System.out.println(document.html());
        String proxies=document.select("div.site_wraper div.newsdsp_left div.newsdsp_border div.newsdsp_content div#contentdiv.artTxt span#str_content").get(0).text();
        System.out.println(proxies);
    }
}
