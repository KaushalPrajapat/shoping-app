package errorcheck.error.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import errorcheck.error.external.ResponseException3;
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseException3> handleCustomException(CustomException exception) {
            return new ResponseEntity<>(ResponseException3.builder()
                    .message(exception.getMessage())
                    .httpStatus(exception.getHttpStatus())
                    .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
    }
}