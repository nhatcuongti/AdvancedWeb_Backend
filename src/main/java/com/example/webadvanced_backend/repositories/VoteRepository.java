package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.COption;
import com.example.webadvanced_backend.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    List<Vote> findByOption(COption option);
}