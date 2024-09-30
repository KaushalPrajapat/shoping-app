package com.shoping.order_service.errors;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ResponseException {
    private String message;
    private String httpStatus;
}
