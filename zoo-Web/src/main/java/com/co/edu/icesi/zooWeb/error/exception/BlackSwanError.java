package com.co.edu.icesi.zooWeb.error.exception;

import com.co.edu.icesi.zooWeb.constants.BlackSwanErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlackSwanError {
    private BlackSwanErrorCode code;
    private String message;
}