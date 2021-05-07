package com.roger.QuestionBankSpider.entity;

import java.util.List;

public class Paper {
    private int id;
    private String grade;
    private String subject;
    private String title;
    private List<String> questions;
    private String url;

    public Paper(String grade, String subject, String title, List<String> questions,String url) {
        this.grade = grade;
        this.subject = subject;
        this.title = title;
        this.questions = questions;
        this.url=url;
    }
}
