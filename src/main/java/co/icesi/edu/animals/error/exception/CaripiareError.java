package co.icesi.edu.animals.error.exception;

import co.icesi.edu.animals.constant.CaripiareErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaripiareError {

    private CaripiareErrorCode caripiareErrorCode;
    private String message;
}
