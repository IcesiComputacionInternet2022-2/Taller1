package co.edu.icesi.zoocanyonriver.error;

import co.edu.icesi.zoocanyonriver.error.exception.TigerDemoError;
import co.edu.icesi.zoocanyonriver.error.exception.TigerDemoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<TigerDemoError> handleException(TigerDemoException userDemoException){
        return new ResponseEntity<>(userDemoException.getError(),userDemoException.getHttpStatus());
    }
}