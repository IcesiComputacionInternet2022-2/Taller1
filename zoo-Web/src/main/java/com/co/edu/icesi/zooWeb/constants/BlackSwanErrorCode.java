package com.co.edu.icesi.zooWeb.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BlackSwanErrorCode {
    CODE_01("Swan doesn't exists"),
    CODE_02("Only letters and spaces"),
    CODE_03("Swan's height isn't correct"),
    CODE_04("Swan's age isn't correct"),
    CODE_05("Swan's parents have the same sex"),
    CODE_06("Swan's weight isn't correct"),
    CODE_07("Swan's name too large"),
    CODE_08("Swan's name already exists"),
    CODE_09("Arrive date is after current date"),
    CODE_10("Swan's parents doesn't exists"),
    CODE_11("Swan already exists"),
    CODE_12("Swan's parents have incorrect sex"),
    CODE_13("Can not change swan sex, is already marked as a parent");

    private String message;
}
