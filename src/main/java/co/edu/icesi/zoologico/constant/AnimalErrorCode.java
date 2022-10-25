package co.edu.icesi.zoologico.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AnimalErrorCode {

    public static String CODE_01="Animal name have an incorrect format";
    public static String CODE_02="Wrong animal arrival date";
    public static String CODE_03="The height of the animal must be between 10 and 90";
    public static String CODE_04="The weight of the animal must be between 10 and 50";
    public static String CODE_05="The age of the animal must be between 0 and 90";
    public static String CODE_06="Animal name exist in the system";
    public static String CODE_07="Animal's mother does not exist in the system";
    public static String CODE_08="Animal's mother is male";
    public static String CODE_09="Animal's father does not exist in the system";
    public static String CODE_10="Animal's father is female";

}