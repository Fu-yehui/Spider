package com.roger.QuestionBankSpider.entity;

import java.util.List;

public class Question {
    private int id;
    private String grade;
    private String subject;
    private Title title;
    private List<QuestionView> questionViews;
    private String url;

    public Question(String grade, String subject, Title title, String url) {
        this.grade = grade;
        this.subject = subject;
        this.title = title;
        this.url = url;
    }

    public Question(String grade, String subject, Title title, List<QuestionView> questionViews, String url) {
        this.grade = grade;
        this.subject = subject;
        this.title = title;
        this.questionViews = questionViews;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public List<QuestionView> getQuestionViews() {
        return questionViews;
    }

    public void setQuestionViews(List<QuestionView> questionViews) {
        this.questionViews = questionViews;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
