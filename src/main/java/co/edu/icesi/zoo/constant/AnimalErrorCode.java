package co.edu.icesi.zoo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AnimalErrorCode {

    CODE_01("Wrong name format: name cannot exceed 120 digits length and can only contain letters and spaces"),
    CODE_02_01("Impossible date: the arrival date cannot be later than the current date"),
    CODE_02_02("Wrong date format: text cannot be parsed to a date, make sure you are using ISO 8601 yyyy-mm-ddThh:mm:ss format"),
    CODE_03(String.format("Wrong specie characteristics: age must be between 0 up to %1$S (years), height between 0 and %2$S (meters) and weight between 0 and %3$S (kg) (0 is always excluded)", BurmesePython.MAX_AGE, BurmesePython.MAX_HEIGHT, BurmesePython.MAX_WEIGHT)),
    CODE_04("Not unique name: the name of the animal must be unique"),
    CODE_05_01("Wrong mother sex: mother has to be female (sex = 'F')"),
    CODE_05_02("Wrong father sex: father has to be male (sex='M')"),
    CODE_06("Invalid Id: you have entered an id that cannot be parsed to UUID");
    /*
    CODE_01("CODE_01"),
    CODE_02("CODE_02"),
    CODE_03("CODE_03"),
    CODE_04("CODE_04"),
    CODE_05("CODE_05"),
    CODE_06("CODE_06");*/

    private String message;
}
