package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
