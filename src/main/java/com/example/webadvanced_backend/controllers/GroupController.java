package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.requestentities.CreateGroupRequest;
import com.example.webadvanced_backend.models.*;
import com.example.webadvanced_backend.repositories.AccountRepository;
import com.example.webadvanced_backend.repositories.GroupRepository;
import com.example.webadvanced_backend.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/group")
@CrossOrigin
public class GroupController {
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupRepository groupRepository;

    @GetMapping(path = "/1")
    public ResponseEntity<?> getMyGroup(Principal principal) {
        try {
            Account account = accountRepository.findByUsername(principal.getName());
            List<UserGroup> list = userGroupRepository.findByUser(account);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/list")
    public ResponseEntity<?> getListGroup(Principal principal) {
        try {
            return ResponseEntity.ok(groupRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/user-group/list")
    public ResponseEntity<?> getUserGroupList(Principal principal) {
        try {
            return ResponseEntity.ok(userGroupRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(path = "/2")
    public ResponseEntity<?> createAGroup(@RequestBody CreateGroupRequest createGroupRequest, Principal principal) {
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
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @GetMapping(path = "/link/{groupId}") // Tạo link để mời.
    public ResponseEntity<?> retrieveInviteLink(
            HttpServletRequest httpServletRequest,
            @PathVariable Integer groupId
    ) {
        try {
            String username = httpServletRequest.getHeader("username");

            // Get Username
            Account account = accountRepository.findByUsername(username);
            GroupInfo groupInfo = groupRepository.findById(groupId).get();
            if (groupInfo == null) throw new Exception("Group is not exists");
            // Check username is owner or not
            UserGroup userGroup = userGroupRepository.findByUserAndGroup(account, groupInfo);
            if (userGroup == null) throw new Exception("User is not the member of this group");
            if (userGroup.getRoleUserInGroup() == RoleUserInGroup.ROLE_MEMBER)
                throw new Exception("User is not Owner / Co-owner of this group");
            return ResponseEntity.ok(String.format("http://localhost:3000/invite/%s", groupId));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @GetMapping(path = "/invite/{groupId}") // Dùng để mời user
    public ResponseEntity<?> inviteMemberToLink(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @PathVariable Integer groupId
    ) {
        try {
            String username = httpServletRequest.getHeader("username");
            if (username == null) {
                httpServletResponse.sendRedirect(
                        String.format("http://localhost:3000/login?redirect_url=http://localhost:8080/api/group/invite/%s", groupId)
                );
                return null;
            }

            // Get Username
            Account account = accountRepository.findByUsername(username);
            GroupInfo groupInfo = groupRepository.findById(groupId).get();
            if (groupInfo == null) throw new Exception("Group is not exists");
            // Check username is owner or not
            UserGroup userGroup = UserGroup.builder().roleUserInGroup(RoleUserInGroup.ROLE_MEMBER)
                    .user(account).group(groupInfo).build();
            userGroupRepository.save(userGroup);
//            httpServletResponse.sendRedirect(String.format("http://localhost:3000/group?id=%s", groupId));
            return ResponseEntity.ok("OK");
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
}
