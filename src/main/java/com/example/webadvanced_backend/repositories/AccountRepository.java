package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);

    Account findByEmailAddress(String emailAddress);
    Boolean existsByUsername(String username);
    Boolean existsByEmailAddress(String emailAddress);

}
