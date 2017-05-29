package com.scmspain.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.StringJoiner;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Not all InvalidArgumentException shall be 4XX error, only the input validation
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Object methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new Object() {
            public String message = parseErrors(ex.getBindingResult());
            public String exceptionClass = ex.getClass().getSimpleName();
        };
    }

    private String parseErrors(BindingResult bindingResult) {
        final StringJoiner sj = new StringJoiner("; ");
        for (ObjectError error : bindingResult.getAllErrors()) {
            sj.add(error.getDefaultMessage());
        }
        return sj.toString();
    }
}
