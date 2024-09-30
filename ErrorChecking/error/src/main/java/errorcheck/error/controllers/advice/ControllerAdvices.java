package errorcheck.error.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import errorcheck.error.errors.CustomException;
import errorcheck.error.external.ResponseException3;

@ControllerAdvice
public class ControllerAdvices {

    // Handling Product doesn't exists exception
    // @SuppressWarnings({ "rawtypes", "unchecked" })
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    // @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> userCustomException(CustomException ex){
        return new ResponseEntity(ResponseException3.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .build(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}