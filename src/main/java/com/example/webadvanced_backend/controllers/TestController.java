package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.Account;
import com.example.webadvanced_backend.models.UserGroup;
import com.example.webadvanced_backend.repositories.AccountRepository;
import com.example.webadvanced_backend.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path="/api")
@CrossOrigin
public class TestController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;

    @PostMapping(path = "/add-account")
    public Account addAccount(
            @RequestBody Account newAccount
    ) {
        System.out.println(newAccount);
        return accountRepository.save(newAccount);
    }

    @GetMapping(path = "/list")
    public List<Account> getListAcconut() {
        return accountRepository.findAll();
    }

    @GetMapping(path = "/list-group")
    public List<UserGroup> getListGroup() {
        return userGroupRepository.findAll();
    }

    @GetMapping(path = "/redeploy")
    public String getData() {
        return "Redeploy";
    }



    @GetMapping(path = "/oauth2/t/callback")
    public String getData1() {

        return "Hellu";
    }


}
