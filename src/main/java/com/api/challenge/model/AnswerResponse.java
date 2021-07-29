package com.api.challenge.model;

public class AnswerResponse {
    private boolean success;
    private String message;
    private long totalMilliseconds;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTotalMilliseconds() {
        return totalMilliseconds;
    }

    public void setTotalMilliseconds(long totalMilliseconds) {
        this.totalMilliseconds = totalMilliseconds;
    }
}
