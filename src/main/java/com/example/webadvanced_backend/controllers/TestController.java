package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.Account;
import com.example.webadvanced_backend.models.ResponseGoogleToken;
import com.example.webadvanced_backend.repositories.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path="/api")
@CrossOrigin
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

    @GetMapping(path = "/list")
    public List<Account> getListAcconut() {
        return accountRepository.findAll();
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
