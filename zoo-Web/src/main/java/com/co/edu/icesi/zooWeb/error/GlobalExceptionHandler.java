package com.co.edu.icesi.zooWeb.error;

import com.co.edu.icesi.zooWeb.error.exception.BlackSwanError;
import com.co.edu.icesi.zooWeb.error.exception.BlackSwanException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<BlackSwanError> handleException(BlackSwanException blackSwanException){
        return new ResponseEntity<>(blackSwanException.getError(), blackSwanException.getHttpStatus());
    }
}