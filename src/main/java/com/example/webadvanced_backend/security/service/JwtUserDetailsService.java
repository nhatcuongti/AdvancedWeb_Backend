package com.example.webadvanced_backend.security.service;

import com.example.webadvanced_backend.models.Account;
import com.example.webadvanced_backend.models.AccountDTO;
import com.example.webadvanced_backend.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = accountRepository.findByUsername(username);
//        Account user = null;
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    public Account save(AccountDTO user) {
        Account newAccount = Account.builder()
                        .username(user.getUsername())
                        .password(bcryptEncoder.encode(user.getPassword()))
                        .image(user.getImage())
                        .fullName(user.getFullName())
                        .emailAddress(user.getEmailAddress())
                        .build();

        return accountRepository.save(newAccount);
    }


}
