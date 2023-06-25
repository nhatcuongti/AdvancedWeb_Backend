package com.example.webadvanced_backend.ex;

/**
 * @author Le Hoang Nhat a.k.a Rei202
 * @Date 6/26/2023
 */

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
