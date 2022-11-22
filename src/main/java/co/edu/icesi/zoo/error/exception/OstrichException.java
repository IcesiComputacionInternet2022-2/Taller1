package co.edu.icesi.zoo.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class OstrichException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private HttpStatus httpStatus;
    private OstrichError error;
    
}
