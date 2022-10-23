package co.edu.icesi.calizoowebapp.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class AfricanLionException extends RuntimeException{
    private HttpStatus httpStatus;
    private AfricanLionError error;
}
