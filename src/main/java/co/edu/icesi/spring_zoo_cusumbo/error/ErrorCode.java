package co.edu.icesi.spring_zoo_cusumbo.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    CODE_ATR_01A("Name format error, letters (a-z and A-Z) and spaces are allowed, name should be less than 120 characters long"),
    CODE_ATR_01B("Repeated name error, a cusumbo already has this name"),
    CODE_ATR_02("Arrival date error, date should be before the present date and time"),
    CODE_ATR_03("Age error, cusumbos should only live up to 15 years"),
    CODE_ATR_04("Height error, cusumbos should only measure up to 42 centimeters form head to beginning of tail"),
    CODE_ATR_05("Weight error, cusumbos should weight up to 10 kilograms"),
    CODE_ATR_06("Sex error, permitted sexes are 'M' and 'F'"),
    CODE_ATR_07A("Parents error, at least one parent with the given ids does not exist"),
    CODE_ATR_07B("Parents sex error, both parent must have different sex"),
    CODE_ATR_07C("Parents sex error, the father must be male ('M') and the mother must be female ('F')"),
    CODE_SEARCH_01("Cusumbo with given name not found");

    private final String message;
}
