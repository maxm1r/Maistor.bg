package com.finalproject.finalproject.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;


public class BadRequestException extends RuntimeException {

    public BadRequestException(String msg){
        super(msg);
    }
}
