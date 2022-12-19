package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.Content;
import com.example.webadvanced_backend.models.ContentHeading;
import com.example.webadvanced_backend.models.ContentMultichoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ContentHeadingRepository extends JpaRepository<ContentHeading, Integer> {
    List<ContentHeading> findByContent(Content content);
    ContentHeading findById(int id);
}