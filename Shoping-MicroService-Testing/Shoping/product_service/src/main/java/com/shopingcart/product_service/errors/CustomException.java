package com.shopingcart.product_service.errors;

public class CustomException extends RuntimeException {
    private String httpStatus;
    public CustomException(String message, String httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
    public String getHttpStatus() {
        return httpStatus;
    }
    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }
    
}