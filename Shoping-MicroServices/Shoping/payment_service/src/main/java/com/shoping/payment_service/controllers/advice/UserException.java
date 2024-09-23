package com.shoping.payment_service.controllers.advice;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.shoping.payment_service.errors.CustomError;
import com.shoping.payment_service.models.ResponseException;



@ControllerAdvice
public class UserException {

    // Handling Product doesn't exists exception
    @ExceptionHandler(CustomError.class)
    @ResponseBody
    // @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException userCustomException(CustomError ex){
        return ResponseException.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .build();
    }

    // Handing Generic Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseException genericHandler(Exception ex){
        return ResponseException.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message(ex.getMessage())
                .build();
    }
    
}
