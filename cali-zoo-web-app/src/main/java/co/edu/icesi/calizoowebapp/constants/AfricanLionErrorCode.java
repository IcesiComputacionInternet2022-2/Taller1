package co.edu.icesi.calizoowebapp.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AfricanLionErrorCode {

    CODE_01("Lion not found"),
    CODE_02("Lion's name already exists"),
    CODE_03("Lion's parents have the same sex"),
    CODE_04("Lion's height is not in race standards"),
    CODE_05("Lion's age is not in race standards"),
    CODE_06("Lion's weight is not in race standards"),
    CODE_07("Lion's name exceeds 120 chars"),
    CODE_08("Only letters and spaces are allowed in lion's name"),
    CODE_09("Arrive date is after current date"),
    CODE_10("Lion's parents does not exists");

    private String message;
}
