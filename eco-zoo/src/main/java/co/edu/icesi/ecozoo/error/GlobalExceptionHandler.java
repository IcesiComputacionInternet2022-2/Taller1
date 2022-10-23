package co.edu.icesi.ecozoo.error;

import co.edu.icesi.ecozoo.constant.AnimalErrorCode;
import co.edu.icesi.ecozoo.error.exception.AnimalError;
import co.edu.icesi.ecozoo.error.exception.AnimalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AnimalError> handleException(AnimalException animalException) {
        return new ResponseEntity<>(animalException.getError(), animalException.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<AnimalError> handleAnnotationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        AnimalException animalException = new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(AnimalErrorCode.CODE_06, Objects.requireNonNull(methodArgumentNotValidException.getFieldError()).getDefaultMessage()));
        return new ResponseEntity<>(animalException.getError(), animalException.getHttpStatus());
    }
    @ExceptionHandler
    public ResponseEntity<AnimalError> handleConstraintException(ConstraintViolationException constraintViolationException) {
        AnimalException animalException = new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(AnimalErrorCode.CODE_06, Objects.requireNonNull(constraintViolationException.getConstraintViolations().stream().reduce("",(s, constraintViolation) ->  constraintViolation.getMessage(), (s, s2) -> s + s2))));
        return new ResponseEntity<>(animalException.getError(), animalException.getHttpStatus());
    }
}
