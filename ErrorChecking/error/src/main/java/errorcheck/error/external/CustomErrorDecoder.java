package errorcheck.error.external;

import com.fasterxml.jackson.databind.ObjectMapper;

import errorcheck.error.errors.CustomException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

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

                ResponseException3 errorResponse = objectMapper.readValue(responseBody, ResponseException3.class);

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