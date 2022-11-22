package co.edu.icesi.zoo.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import co.edu.icesi.zoo.error.exception.OstrichError;
import co.edu.icesi.zoo.error.exception.OstrichException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<OstrichError> handleException(OstrichException documentException){
        return new ResponseEntity<>(documentException.getError(), documentException.getHttpStatus());
    }

}
