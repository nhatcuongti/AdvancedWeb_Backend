package com.example.webadvanced_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table()
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "facebook_id")
    private String facebookId;

    @OneToOne(mappedBy = "accountInfo")
    private UserInfo userInfo;
}
