package co.edu.icesi.zoo.error;

import co.edu.icesi.zoo.constant.AnimalErrorCode;
import co.edu.icesi.zoo.error.exception.AnimalError;
import co.edu.icesi.zoo.error.exception.AnimalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AnimalError> handleException(AnimalException animalException){
        return new ResponseEntity<>(animalException.getError(), animalException.getHttpStatus());
    }


}
