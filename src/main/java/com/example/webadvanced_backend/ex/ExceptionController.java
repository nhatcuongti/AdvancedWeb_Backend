package com.example.webadvanced_backend.ex;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Le Hoang Nhat a.k.a Rei202
 * @Date 6/26/2023
 */
@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ExceptionController {
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NotFoundException.class})
    public ExceptionResponse handleNotFoundException(NotFoundException ex){
        return new ExceptionResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
