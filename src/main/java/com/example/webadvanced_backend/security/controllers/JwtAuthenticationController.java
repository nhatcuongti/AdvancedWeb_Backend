package com.example.webadvanced_backend.security.controllers;

import com.example.webadvanced_backend.models.AccountDTO;
import com.example.webadvanced_backend.models.ResponseGoogleToken;
import com.example.webadvanced_backend.repositories.AccountRepository;
import com.example.webadvanced_backend.security.models.JwtRequest;
import com.example.webadvanced_backend.security.models.JwtResponse;
import com.example.webadvanced_backend.security.models.MessageResponse;
import com.example.webadvanced_backend.security.service.JwtTokenUtil;
import com.example.webadvanced_backend.security.service.JwtUserDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private AccountRepository accountRepository;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token, null));
    }



    @PostMapping(value = "/register")
    public ResponseEntity<?> registerAccount(@RequestBody AccountDTO account) throws Exception {
        // Check account is exists or not
        if (accountRepository.existsByUsername(account.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));

        // Check email is exists or not
        if (accountRepository.existsByEmailAddress(account.getEmailAddress()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));

        // Create user acconut
        return ResponseEntity.ok(userDetailsService.save(account));
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody AccountDTO account) throws Exception {
        // Authenticate user is valid or not
        String jwt = null;
        UserDetails userDetails = null;
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword()));

            // Set user is global
            SecurityContextHolder.getContext().setAuthentication(authentication);
            userDetails = (UserDetails) authentication.getPrincipal();

            jwt = jwtTokenUtil.generateToken(userDetails);
        } catch (Exception e) {
            return ResponseEntity.ok("Errors : Username or password is incorrect");
        }

        return ResponseEntity.ok(
                new JwtResponse(jwt, userDetails.getUsername())
        );
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);    
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    // get authorization code from google response
    @ResponseBody
    @GetMapping(path = "/oauth2/code/callback")
    public void getGoogleAccessToken(@RequestParam String code) throws JsonProcessingException {
        // return object containing properties about token
        ResponseGoogleToken response = getAuthenTokenGoogle(code);
        System.out.println("response");

        // redirect and send token to client
    }

    // exchange authorization code to google token
    public ResponseGoogleToken getAuthenTokenGoogle(String code) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<String>(headers);
        String access_token_url = "https://oauth2.googleapis.com/token";
        access_token_url += "?code=" + code;
        access_token_url += "&grant_type=authorization_code";
        access_token_url += "&redirect_uri=http://localhost:8080/auth/oauth2/code/callback";
        access_token_url += "&client_id=947258420566-o8mj2pfqrs96i6mski8k990taa83mt9j.apps.googleusercontent.com";
        access_token_url += "&client_secret=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";
        ResponseEntity<String> response = null;
        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), ResponseGoogleToken.class);
    }

}
