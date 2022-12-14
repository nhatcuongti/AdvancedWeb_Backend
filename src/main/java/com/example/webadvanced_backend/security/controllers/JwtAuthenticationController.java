package com.example.webadvanced_backend.security.controllers;

import com.example.webadvanced_backend.models.*;
import com.example.webadvanced_backend.repositories.AccountRepository;
import com.example.webadvanced_backend.repositories.UserGroupRepository;
import com.example.webadvanced_backend.responseentities.ResponseGoogleToken;
import com.example.webadvanced_backend.security.models.JwtRequest;
import com.example.webadvanced_backend.security.models.JwtResponse;
import com.example.webadvanced_backend.security.models.MessageResponse;
import com.example.webadvanced_backend.security.service.JwtTokenUtil;
import com.example.webadvanced_backend.security.service.JwtUserDetailsService;
import com.example.webadvanced_backend.services.EmailSenderService;
import com.example.webadvanced_backend.utils.UrlUltils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class JwtAuthenticationController  {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UrlUltils urlUltils;


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
        Thread threadEmail = new Thread(new Runnable() {
            @Override
            public void run() {
                emailSenderService.sendEmail(account.getEmailAddress(), "Activated your account",
                        "Hello, if you need to activate your account from my application, please click link : " +
                                String.format(urlUltils.getUrl() + "/auth/activate/%s", account.getUsername()));
            }
        });
        threadEmail.start();
        // Create user acconut
        return ResponseEntity.ok(userDetailsService.save(account));
    }

    @Autowired
    UserGroupRepository userGroupRepository;
    @PostMapping(value = "/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody AccountDTO account) throws Exception {
        // Authenticate user is valid or not
        String jwt = null;
        UserDetails userDetails = null;
        try {
            List<UserGroup> listUserGroup = userGroupRepository.findAll();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword()));

            // Set user is global
            SecurityContextHolder.getContext().setAuthentication(authentication);
            userDetails = (UserDetails) authentication.getPrincipal();

            Account loginAccount = accountRepository.findByUsername(account.getUsername());
            if (null == loginAccount.getActivate()) throw new Exception("Account is not activate");

            jwt = jwtTokenUtil.generateToken(userDetails);
        } catch (Exception e) {
            String errorMsg = "Account is not activate !!";
            if (e instanceof AuthenticationException)
                errorMsg = "Username or password is not correct !!";
            return ResponseEntity.status(401).body(errorMsg);
        }

        return ResponseEntity.ok(
                new JwtResponse(jwt, userDetails.getUsername())
        );
    }

    @PostMapping(value = "/forgot-password-event")
    public ResponseEntity<?> forgotPasswordEvent(@RequestBody AccountDTO accountDTO) throws Exception {
        // Authenticate user is valid or not
        String emailAddress;
        try {
            emailAddress = accountDTO.getEmailAddress();
            System.out.println(emailAddress);
            Account accountByEmail = accountRepository.findByEmailAddress(emailAddress);
            if (accountByEmail == null) return ResponseEntity.badRequest().body("Your Email isn't exists");
            Thread threadEmail = new Thread(new Runnable() {
                @Override
                public void run() {
                    String bodyMessage = "\n" +
                            "Hi,\n" +
                            "\n" +
                            "You have chosen to reset your password. Please click this link to create a new one: \n" +
                            "\n" +
                            String.format(urlUltils.getClientUrl() + "/reset_password/%s", accountByEmail.getUsername().toString()) +
                            "\n" +
                            "If you did not reset your password, please ignore this email. If you have any questions or feedback you can just reply directly to this email.\n" +
                            "\n" +
                            "Thanks for using our service!\n" +
                            "Kind regards, Team";
                    emailSenderService.sendEmail(accountByEmail.getEmailAddress(), "Reset password mail", bodyMessage);
                }
            });
            threadEmail.start();
        } catch (Exception e) {
            String errorMsg = "Account is not activate !!";
            if (e instanceof AuthenticationException)
                errorMsg = "Username or password is not correct !!";
            return ResponseEntity.status(401).body(errorMsg);
        }

        return ResponseEntity.ok(emailAddress);

    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody AccountDTO accountDTO) throws Exception {
        // Authenticate user is valid or not
        String username;
        String newPassword ;
        Account accountByUsername;
        try {
            username = accountDTO.getUsername();
            newPassword = accountDTO.getPassword();
            accountByUsername = accountRepository.findByUsername(username);
            if (accountByUsername == null) return ResponseEntity.badRequest().body("Username is not valid");
            accountByUsername.setPassword(passwordEncoder.encode(newPassword));
        } catch (Exception e) {
            String errorMsg = "Account is not activate !!";
            if (e instanceof AuthenticationException)
                errorMsg = "Username or password is not correct !!";
            return ResponseEntity.status(401).body(errorMsg);
        }

        return ResponseEntity.ok(accountRepository.save(accountByUsername));

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
    public void getGoogleAccessToken(@RequestParam String code,
                                     HttpServletResponse httpServletResponse) throws JsonProcessingException {
        // return object containing properties about token
        try{
            ResponseGoogleToken response = getAuthenTokenGoogle(code);
            System.out.println(response.getId_token());
            JSONObject authUserInfor = getAuthUserInfor(response.getId_token());
            String email = authUserInfor.getAsString("email");
            Account account = accountRepository.findByEmailAddress(email);
            if (account == null) {
                account = Account.builder().username(UUID.randomUUID().toString())
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .activate(true)
                        .emailAddress(email)
                        .image(authUserInfor.getAsString("picture"))
                        .fullName(authUserInfor.getAsString("name"))
                        .build();
                accountRepository.save(account);
            }

            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(account.getUsername());
            String urlRedirect = String.format("%s?access_token=%s&username=%s", urlUltils.getClientUrl(), jwtTokenUtil.generateToken(userDetails), account.getUsername());
            httpServletResponse.sendRedirect(urlRedirect);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    public JSONObject getAuthUserInfor(String idToken) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<String>(headers);
        String access_token_url = String.format("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=%s", idToken);
        ResponseEntity<String> response = null;
        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
        JSONObject jsonObject = (JSONObject) (new JSONParser()).parse(response.getBody());

        return jsonObject;
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
        access_token_url += "&redirect_uri="+ urlUltils.getUrl()+"/auth/oauth2/code/callback";
        access_token_url += "&client_id=947258420566-o8mj2pfqrs96i6mski8k990taa83mt9j.apps.googleusercontent.com";
        access_token_url += "&client_secret=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";
        ResponseEntity<String> response = null;
        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), ResponseGoogleToken.class);
    }

    @GetMapping(path = "/activate/{username}")
    public void activateAccount(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @PathVariable String username
    ) {
        try {
            Account activatedAccount = accountRepository.findByUsername(username);
            activatedAccount.setActivate(true);
            accountRepository.save(activatedAccount);
            httpServletResponse.sendRedirect(urlUltils.getClientUrl() + "/login");
        } catch (Exception e ) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping(path = "/resend/activate/{username}")
    public ResponseEntity<?> resendActivateAccount(
            HttpServletRequest httpServletRequest,
            @PathVariable String username
    ) {
        try {
            Account account = accountRepository.findByUsername(username);
            Thread threadEmail = new Thread(new Runnable() {
                @Override
                public void run() {
                    emailSenderService.sendEmail(account.getEmailAddress(), "Activated your account",
                            "Hello, if you need to activate your account from my application, please click link : " +
                                    String.format(urlUltils.getUrl()+ "/api/user/activate/%s", account.getUsername()));
                }
            });
            threadEmail.start();

            return ResponseEntity.ok("OK");
        } catch (Exception e ) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

}
