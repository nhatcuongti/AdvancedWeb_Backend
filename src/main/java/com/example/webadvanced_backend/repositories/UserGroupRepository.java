package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.Account;
import com.example.webadvanced_backend.models.GroupInfo;
import com.example.webadvanced_backend.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
    List<UserGroup> findByUser(Account user);
    List<UserGroup> findByGroup(GroupInfo groupInfo);

    UserGroup findByUserAndGroup(Account user, GroupInfo groupInfo);

}
