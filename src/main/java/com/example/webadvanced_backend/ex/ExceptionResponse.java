package com.example.webadvanced_backend.ex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Le Hoang Nhat a.k.a Rei202
 * @Date 6/26/2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private HttpStatus status;
    private String message;
}
