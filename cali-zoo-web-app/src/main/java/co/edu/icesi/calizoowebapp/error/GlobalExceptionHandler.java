package co.edu.icesi.calizoowebapp.error;

import co.edu.icesi.calizoowebapp.error.exception.AfricanLionError;
import co.edu.icesi.calizoowebapp.error.exception.AfricanLionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AfricanLionError> handleException(AfricanLionException africanLionException){
        return new ResponseEntity<>(africanLionException.getError(), africanLionException.getHttpStatus());
    }
}
