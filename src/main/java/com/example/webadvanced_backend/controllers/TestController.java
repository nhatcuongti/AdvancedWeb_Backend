package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.Account;
import com.example.webadvanced_backend.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(path="/api")
public class TestController {

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping(path = "/add-account")
    public Account addAccount(
        @RequestBody Account newAccount
    ) {
        System.out.println(newAccount);
        return accountRepository.save(newAccount);
    }

    @GetMapping(path ="/list")
    public List<Account> getListAcconut() {
        return accountRepository.findAll();
    }

    @GetMapping(path = "/redeploy")
    public String getData() {
        return "Redeploy";
    }

}
