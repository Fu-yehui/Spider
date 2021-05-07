package com.roger.QuestionBankSpider.entity;

/**
 * 题目最小单位
 */
public class QuestionView {
    private String content;
    private String image;

    public QuestionView(String content, String image) {
        this.content = content;
        this.image = image;
    }

    public QuestionView(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
