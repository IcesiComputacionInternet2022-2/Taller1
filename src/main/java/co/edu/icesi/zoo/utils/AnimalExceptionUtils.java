package co.edu.icesi.zoo.utils;

import co.edu.icesi.zoo.constant.AnimalErrorCode;
import co.edu.icesi.zoo.error.exception.AnimalError;
import co.edu.icesi.zoo.error.exception.AnimalException;
import org.springframework.http.HttpStatus;

public class AnimalExceptionUtils {

    public static void throwAnimalException(HttpStatus httpStatus, AnimalErrorCode code){
        throw new AnimalException(httpStatus, new AnimalError(code, code.getMessage()));
    }
}
