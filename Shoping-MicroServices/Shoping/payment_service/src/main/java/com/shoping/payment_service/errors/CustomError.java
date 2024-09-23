package com.shoping.payment_service.errors;

public class CustomError extends RuntimeException {
    private String httpStatus;
    public CustomError(String message, String httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
    public String getHttpStatus(){
        return this.httpStatus;
    }
    
}
