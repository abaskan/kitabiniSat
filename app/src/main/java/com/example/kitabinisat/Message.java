package com.example.kitabinisat;

public class Message {
    private String userId;
    private String customerId;
    private String message;
    private String date;
    private String from;

    public Message() {
    }

    public Message(String userId, String customerId, String message, String date, String from) {
        this.userId = userId;
        this.customerId = customerId;
        this.message = message;
        this.date = date;
        this.from = from;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
