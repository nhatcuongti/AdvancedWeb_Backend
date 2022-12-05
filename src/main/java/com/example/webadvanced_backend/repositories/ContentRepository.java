package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
    Content findById(int id);
}