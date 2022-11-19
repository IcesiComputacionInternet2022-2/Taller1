package co.edu.icesi.zoo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AnimalErrorCode {
    CODE_C11("Field 'name' has to be 120 characters or shorter"),
    CODE_C12("Field 'name' can only contain letters and spaces"),
    CODE_C2("Field 'gender' can only be M or F"),
    CODE_C3("Field 'date' cannot be after current date"),

    CODE_S1("The Weight (kg) is not on the correct range, it has to be between 4 and 9"),
    CODE_S2("The Age (years) is not on the correct range, it has to be between 0 and 35"),
    CODE_S3("The Height (cm) is not on the correct range, it has to be between 120 and 150"),
    CODE_S4("The parents gender cannot be the same. Please modify your selection"),
    CODE_S5("This name is already in use. Please try another one");

    private final String errorMessage;
}
