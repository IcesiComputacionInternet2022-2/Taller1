package co.edu.icesi.apizoo.root.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class AnimalException extends RuntimeException{
    private HttpStatus httpStatus;
    private AnimalError animalError;
}
