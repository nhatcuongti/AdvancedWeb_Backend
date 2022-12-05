package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.Presentation;
import com.example.webadvanced_backend.models.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Integer> {
    List<Slide> findByPresentation(Presentation presentation);
    Slide findById(int id);

}