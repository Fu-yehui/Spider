package com.roger.spider_proxy.enums;

public enum ProxyScore {
    MAX_SCORE(100),
    MIN_SCORE(0),
    INITIAL_SCORE(50);

    double score;
     ProxyScore(double score){
         this.score=score;
     }

    public double getScore() {
        return score;
    }
}

