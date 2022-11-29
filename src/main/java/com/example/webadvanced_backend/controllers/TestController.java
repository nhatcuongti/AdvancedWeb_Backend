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
<<<<<<< HEAD
@RequestMapping(path = "/")
=======
@RequestMapping(path="/api")
@CrossOrigin
>>>>>>> mid-term
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

    @ResponseBody
    @GetMapping(path = "/oauth2/code/callback")
    public void getGoogleAccessToken(@RequestParam String code) throws JsonProcessingException {
        // return object containing properties about token
        ResponseGoogleToken response = getAuthenTokenGoogle(code);
        System.out.println("ds");
    }

    @GetMapping(path = "/oauth2/t/callback")
    public String getData1() {

        return "Hellu";
    }

    public ResponseGoogleToken getAuthenTokenGoogle(String code) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<String>(headers);
        String access_token_url = "https://oauth2.googleapis.com/token";
        access_token_url += "?code=" + code;
        access_token_url += "&grant_type=authorization_code";
        access_token_url += "&redirect_uri=http://localhost:8080/oauth2/code/callback";
        access_token_url += "&client_id=947258420566-o8mj2pfqrs96i6mski8k990taa83mt9j.apps.googleusercontent.com";
        access_token_url += "&client_secret=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";
        ResponseEntity<String> response = null;
        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), ResponseGoogleToken.class);
    }
}
