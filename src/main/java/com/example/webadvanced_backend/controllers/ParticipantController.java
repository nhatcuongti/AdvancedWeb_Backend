package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.Account;
import com.example.webadvanced_backend.models.GroupInfo;
import com.example.webadvanced_backend.models.UserGroup;
import com.example.webadvanced_backend.repositories.AccountRepository;
import com.example.webadvanced_backend.repositories.GroupRepository;
import com.example.webadvanced_backend.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path ="api/participant")
@CrossOrigin
public class ParticipantController {
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private GroupRepository groupRepository;

    @GetMapping()
    @ResponseBody
    public ResponseEntity<?> getMyGroup(HttpServletRequest httpServletRequest, Principal principal, @RequestParam int id){
        try{
//            String username = httpServletRequest.getHeader("username");

            List<UserGroup> list = userGroupRepository.findByGroup(groupRepository.findById(id));
            return ResponseEntity.ok(list);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
