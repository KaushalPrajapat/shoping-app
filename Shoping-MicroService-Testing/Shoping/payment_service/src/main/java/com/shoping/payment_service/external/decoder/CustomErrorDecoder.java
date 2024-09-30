package com.shoping.payment_service.external.decoder;
import java.io.IOException;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoping.payment_service.errors.CustomException;
import com.shoping.payment_service.models.ResponseException;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());

        if (response.body() != null) {
            try {
                String responseBody = new String(response.body().asInputStream().readAllBytes()); // Read the body as bytes
                log.info("Response body: {}", responseBody);

                ResponseException errorResponse = objectMapper.readValue(responseBody, ResponseException.class);

                return new CustomException(errorResponse.getMessage(),
                        errorResponse.getHttpStatus());
            } catch (IOException e) {
                log.error("Error while reading the error response body: {}", e.getMessage());
                return new CustomException("Internal Server Error",
                        "INTERNAL_SERVER_ERROR");
            }
        }

        return new CustomException("Empty response body", "EMPTY_RESPONSE_BODY");
    }
}