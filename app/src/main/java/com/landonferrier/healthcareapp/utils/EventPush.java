package com.landonferrier.healthcareapp.utils;

public class EventPush {

    public String message;
    public String searchType;

    public EventPush(String message, String searchType) {
        this.message = message;
        this.searchType = searchType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}