package com.roger.spider_proxy.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) {

        System.out.println("----start invoke end()----"+ sdf.format(new Date()));
        Test.end();
        System.out.println("----invoke end() completion----"+ sdf.format(new Date()));

    }
}
