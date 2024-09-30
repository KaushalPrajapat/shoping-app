package com.shoping.order_service.errors;

public class CustomException extends RuntimeException {
    private String httpStatus;
    public CustomException(String message, String httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public String getHttpStatus(){
        return this.httpStatus;
    }

}
