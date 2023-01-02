package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.Presentation;
import com.example.webadvanced_backend.models.PresentationGroup;
import com.example.webadvanced_backend.models.Question;
import com.example.webadvanced_backend.models.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByPresentationGroup(PresentationGroup presentationGroup);

}