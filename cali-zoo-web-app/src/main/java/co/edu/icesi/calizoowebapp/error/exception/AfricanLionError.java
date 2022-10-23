package co.edu.icesi.calizoowebapp.error.exception;

import co.edu.icesi.calizoowebapp.constants.AfricanLionErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AfricanLionError {
    private AfricanLionErrorCode code;
    private String message;
}
