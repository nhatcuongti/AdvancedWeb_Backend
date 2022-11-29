package com.example.webadvanced_backend.security.controllers;

import com.example.webadvanced_backend.models.AccountDTO;
import com.example.webadvanced_backend.repositories.AccountRepository;
import com.example.webadvanced_backend.security.models.JwtRequest;
import com.example.webadvanced_backend.security.models.JwtResponse;
import com.example.webadvanced_backend.security.models.MessageResponse;
import com.example.webadvanced_backend.security.service.JwtTokenUtil;
import com.example.webadvanced_backend.security.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
}
