package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.*;
import com.example.webadvanced_backend.repositories.AccountRepository;
import com.example.webadvanced_backend.repositories.GroupRepository;
import com.example.webadvanced_backend.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
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
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping(path="/1")
    @ResponseBody
    public ResponseEntity<?> getMyGroup(HttpServletRequest httpServletRequest, Principal principal, @RequestParam int id){
        try{
//            String username = httpServletRequest.getHeader("username");
            List<UserGroup> list = userGroupRepository.findByGroup(groupRepository.findById(id));
            return ResponseEntity.ok(new ResponseListUserInGroup(list, findUserinSpecficGr(id,principal.getName(), list)));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping(path = "/2")
    public ResponseEntity<?> upgradeRole(@RequestBody UpgradeRoleRequest upgradeRoleRequest, Principal principal){
        try {
            String username = upgradeRoleRequest.getUsername();
            List<UserGroup> list = userGroupRepository.findByGroup(groupRepository.findById(upgradeRoleRequest.getGroupId()));
            UserGroup userGroup = findUserinSpecficGr(upgradeRoleRequest.getGroupId(), username,list);
            userGroup.setRoleUserInGroup(RoleUserInGroup.ROLE_COOWNER);
//            userGroupRepository.save(userGroup);
            return ResponseEntity.ok(userGroupRepository.save(userGroup));

        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage()) ;
        }

    }
    @PostMapping(path = "/4")
    public ResponseEntity<?> upgraRole(@RequestBody UpgradeRoleRequest upgradeRoleRequest, Principal principal){
        try {
            String username = upgradeRoleRequest.getUsername();
            List<UserGroup> list = userGroupRepository.findByGroup(groupRepository.findById(upgradeRoleRequest.getGroupId()));
            UserGroup userGroup = findUserinSpecficGr(upgradeRoleRequest.getGroupId(), username,list);
            userGroup.setRoleUserInGroup(RoleUserInGroup.ROLE_MEMBER);
            return ResponseEntity.ok(userGroupRepository.save(userGroup));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage()) ;
        }

    }

    @PostMapping(path = "/3")
    public ResponseEntity<?> deleteUserGroup(@RequestBody UpgradeRoleRequest upgradeRoleRequest, Principal principal){
        try {
            String username = upgradeRoleRequest.getUsername();
            List<UserGroup> list = userGroupRepository.findByGroup(groupRepository.findById(upgradeRoleRequest.getGroupId()));
            UserGroup userGroup = findUserinSpecficGr(upgradeRoleRequest.getGroupId(), username,list);
            userGroupRepository.delete(userGroup);
            return ResponseEntity.ok(userGroup);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage()) ;
        }

    }

    public UserGroup findUserinSpecficGr(int id, String username, List<UserGroup> list ) {
        UserGroup myAccount = null;
        for (UserGroup u : list) {
            if (u.getUser() == accountRepository.findByUsername(username))
                return u;
        }
        return null;
    }

}
