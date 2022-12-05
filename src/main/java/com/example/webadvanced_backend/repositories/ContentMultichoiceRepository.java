package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.Content;
import com.example.webadvanced_backend.models.ContentMultichoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentMultichoiceRepository extends JpaRepository<ContentMultichoice, Integer> {
    List<ContentMultichoice> findByContent(Content content);
    ContentMultichoice findById(int id);

}