package com.roger.QuestionBankSpider.util;

public class Child extends Parent {
    public int id;
    public Child(){
        super();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public static void main(String[] args) {
        System.out.println(new Child().getId());
    }
}
