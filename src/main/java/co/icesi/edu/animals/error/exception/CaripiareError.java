package co.icesi.edu.animals.error.exception;

import co.icesi.edu.animals.constant.CaripiareErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CaripiareError {

    private CaripiareErrorCode caripiareErrorCode;
    private String message;
}
