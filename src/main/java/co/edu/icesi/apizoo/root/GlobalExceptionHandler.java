package co.edu.icesi.apizoo.root;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import co.edu.icesi.apizoo.root.exception.AnimalError;
import co.edu.icesi.apizoo.root.exception.AnimalException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler
    public ResponseEntity<AnimalError>handlerException(AnimalException animalException) {
        return new ResponseEntity<>(animalException.getAnimalError(), animalException.getHttpStatus());
    }
}
