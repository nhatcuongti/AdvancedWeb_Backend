package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.Account;
import com.example.webadvanced_backend.requestentities.EditProfileRequest;
import com.example.webadvanced_backend.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path="/api")
@CrossOrigin
public class UserController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(path = "/user")
    public ResponseEntity<?> getUser(HttpServletRequest httpServletRequest) {
        try {
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("username", "nhatks147");
            session.getAttribute("username");
            httpServletRequest.getSession().getId();

            String username = httpServletRequest.getHeader("username");
            return ResponseEntity.ok(this.accountRepository.findByUsername(username));
        } catch (Exception e ) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PostMapping(path = "/user/edit-profile")
    public ResponseEntity<?> editProfile(
            HttpServletRequest httpRequest,
            @RequestBody EditProfileRequest editProfile
    ) {
        // Check xem request của người dùng là thuộc trường hợp nào
        try {
            // Tìm userID của thằng gửi
            String username = httpRequest.getHeader("username");
            // Lấy User từ userID đó
            Account updatedAccount = this.accountRepository.findByUsername(username);
            updatedAccount.setImage(editProfile.getImg());
            updatedAccount.setFullName(editProfile.getFullname());
            // Check password
            if (editProfile.getNewPassword() != "" && editProfile.getConfirmNewPassword() != "" && editProfile.getOldPassword() != "") {
                if (passwordEncoder.matches(editProfile.getOldPassword(), updatedAccount.getPassword())) {
                    if (editProfile.getNewPassword().equals(editProfile.getConfirmNewPassword()))
                        updatedAccount.setPassword(passwordEncoder.encode(editProfile.getNewPassword()));
                    else throw new RuntimeException("New Password and confirm password is not the same");
                } else throw new RuntimeException("Old password is wrong !!");
            }
            return ResponseEntity.ok(accountRepository.save(updatedAccount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}
