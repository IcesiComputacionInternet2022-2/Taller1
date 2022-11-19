package co.edu.icesi.zoo.error.exception;

import co.edu.icesi.zoo.constant.AnimalErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnimalError{

    private AnimalErrorCode code;
    private String message;

}
