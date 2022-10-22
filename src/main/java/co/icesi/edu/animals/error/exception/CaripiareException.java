package co.icesi.edu.animals.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class CaripiareException extends RuntimeException{

    private HttpStatus httpStatus;
    private CaripiareError caripiareError;
}
