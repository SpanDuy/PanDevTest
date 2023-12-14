package com.example.telegrambottreeapi.exception;

public class ExceptionResponse extends Throwable{
    private String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
