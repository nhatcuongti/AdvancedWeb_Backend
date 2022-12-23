package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.Content;
import com.example.webadvanced_backend.models.ContentHeading;
import com.example.webadvanced_backend.models.ContentParagraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ContentParagraphRepository extends JpaRepository<ContentParagraph, Integer> {
    List<ContentParagraph> findByContent(Content content);
    ContentParagraph findById(int id);
}