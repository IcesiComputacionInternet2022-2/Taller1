package co.icesi.edu.animals.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CaripiareErrorCode {

    CODE_01("'name' already exists."),
    CODE_02("Caripiare not found."),
    CODE_03("Parent 'gender' validation failed.");

    private final String message;
}
