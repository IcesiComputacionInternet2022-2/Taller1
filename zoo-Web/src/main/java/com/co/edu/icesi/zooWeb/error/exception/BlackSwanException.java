package com.co.edu.icesi.zooWeb.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class BlackSwanException extends RuntimeException{
    private HttpStatus httpStatus;
    private BlackSwanError error;
}

