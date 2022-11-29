package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.GroupInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<GroupInfo, Integer> {


}
