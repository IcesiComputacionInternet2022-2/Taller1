package co.edu.icesi.zoo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OstrichErrorCode {

    CODE_01("Ostrich name is already use"),
    CODE_02("Maximun 120 characteres in the name"),
    CODE_03("Only letters and spaces are allowed in name"),
    CODE_04("Arrival date cannot be later than the current date"),
	CODE_05("Ostrich weight is between 63 and 140 kilograms"),
	CODE_06("Ostrich age is between 0 and 75 years"),
	CODE_07("Ostrich height is between 1.70 and 2.80 meters"),
	CODE_08("Ostrich gender is 0 for female or 1 for male"),
	CODE_09("None of the ostriches match father id"),
	CODE_10("None of the ostriches match mother id"),
	CODE_11("Father's gender is not male"),
	CODE_12("Mother's gender is not female"),
	CODE_13("Ostrich not found");

    private final String message;
}
