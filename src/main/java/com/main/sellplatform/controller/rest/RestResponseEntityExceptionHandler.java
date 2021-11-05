package com.main.sellplatform.controller.rest;

import com.main.sellplatform.exception.userexception.EmailException;
import com.main.sellplatform.exception.userexception.UserNotFoundByEmailException;
import com.main.sellplatform.exception.userexception.UserPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { UserPasswordException.class})
    public ResponseEntity<Object> handlerUserPasswordException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = { UserNotFoundByEmailException.class})
    public ResponseEntity<Object> handlerUserNotFoundByEmailException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EmailException.class})
    public ResponseEntity<Object> handlerEmailException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
