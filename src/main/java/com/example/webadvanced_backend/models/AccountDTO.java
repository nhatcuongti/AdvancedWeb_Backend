package com.example.webadvanced_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private int id;
    private String username;
    private String password;
    private String emailAddress;
    private String facebookId;
    private String image;
    private String fullName;
    private Boolean activate;
}
