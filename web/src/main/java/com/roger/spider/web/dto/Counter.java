package com.roger.spider.web.dto;

public class Counter {
    private Long total;
    private Long newData;

    public Counter(Long total, Long newData) {
        this.total = total;
        this.newData = newData;
    }

    @Override
    public String toString() {
        return "Counter{" +
                "total=" + total +
                ", newData=" + newData +
                '}';
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getNewData() {
        return newData;
    }

    public void setNewData(Long newData) {
        this.newData = newData;
    }
}
