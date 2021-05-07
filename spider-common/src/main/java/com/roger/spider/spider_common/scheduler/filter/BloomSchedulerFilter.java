package com.roger.spider.spider_common.scheduler.filter;


import com.google.common.hash.BloomFilter;
import com.roger.spider.spider_common.model.Request;

import java.nio.charset.Charset;


/**
 * @author xiepuhuan
 */
public class BloomSchedulerFilter implements Filter {

    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    private final BloomFilter<Request> bloomFilter;

    private static final int DEFAULT_EXPECTED_INSERTIONS = 30000000;
    private static final double DEFAULT_FPP = 0.01;

    public BloomSchedulerFilter(int expectedInsertions, double fpp) {

        bloomFilter = BloomFilter.create((from, into) -> {
            into.putString(from.getUrl(), DEFAULT_CHARSET);
        }, expectedInsertions, fpp);
    }

    public BloomSchedulerFilter(double fpp) {
        this(DEFAULT_EXPECTED_INSERTIONS, fpp);
    }

    public BloomSchedulerFilter(int expectedInsertions) {
        this(expectedInsertions, DEFAULT_FPP);
    }

    public BloomSchedulerFilter() {
        this(DEFAULT_EXPECTED_INSERTIONS, DEFAULT_FPP);
    }

    @Override
    public boolean filter(Request request) {
        if (bloomFilter.mightContain(request)) {
            return true;
        }

        bloomFilter.put(request);
        return false;
    }
}
