package com.example.scheduler.producer.exception;

public class CustomException extends RuntimeException{

    private String errorCode;
    private String errorMessage;

    public CustomException(String message){
        super(message);
        this.errorMessage=message;
    }

    public CustomException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

