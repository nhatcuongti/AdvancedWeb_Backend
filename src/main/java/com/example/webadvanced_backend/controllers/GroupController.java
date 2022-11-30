package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.*;
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
@RequestMapping(path ="/api/group")
@CrossOrigin
public class GroupController {
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupRepository groupRepository;
    @GetMapping(path ="/1")
    public ResponseEntity<?> getMyGroup( Principal principal){
        try{
            Account account = accountRepository.findByUsername(principal.getName());
            List<UserGroup> list = userGroupRepository.findByUser(account);
            return ResponseEntity.ok(list);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(path = "/2")
    public ResponseEntity<?> createAGroup( @RequestBody CreateGroupRequest createGroupRequest, Principal principal){
        try {
            GroupInfo groupInfo;
            UserGroup userGroup;

//            groupInfo.setGroupName();
            groupInfo = GroupInfo.builder().groupName(createGroupRequest.getGroupName()).build();
            userGroup = UserGroup.builder().group(groupInfo).user(accountRepository.findByUsername(principal.getName()))
                    .roleUserInGroup(RoleUserInGroup.ROLE_OWNER).build();

            groupRepository.save(groupInfo);
            UserGroup userGroup1 = userGroupRepository.save(userGroup);

            return ResponseEntity.ok(userGroup1);
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
}
