package co.edu.icesi.apizoo.root.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnimalError {
    private String code;
    private String message;
}
