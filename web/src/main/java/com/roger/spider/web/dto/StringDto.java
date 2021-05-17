package com.roger.spider.web.dto;

/**
 * @author roger
 */
public class StringDto {
    private String message;

    public StringDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message ;
    }
}
