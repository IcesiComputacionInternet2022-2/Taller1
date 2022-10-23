package co.edu.icesi.zoocanyonriver.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class TigerDemoException extends RuntimeException{
    private HttpStatus httpStatus;
    private TigerDemoError error;
}