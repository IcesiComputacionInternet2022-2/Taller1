package co.edu.icesi.spring_zoo_cusumbo.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@AllArgsConstructor
public class CusumboException extends RuntimeException{

    private HttpStatus httpStatus;

    private CusumboError error;

}
