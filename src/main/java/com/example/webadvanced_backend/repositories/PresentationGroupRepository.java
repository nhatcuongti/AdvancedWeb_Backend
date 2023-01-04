package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.Presentation;
import com.example.webadvanced_backend.models.PresentationGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresentationGroupRepository extends JpaRepository<PresentationGroup, Integer> {
    PresentationGroup findById(int preId);
    PresentationGroup findPresentationGroupByPresentationAndIsPresenting(Presentation presentation, boolean isPresenting);
    List<PresentationGroup> findByPresentation(Presentation presentation);
    List<PresentationGroup> findPresentationGroupByGroupIdAndAndIsPresenting(int groupId, boolean isPresenting);
    List<PresentationGroup> findByGroupId(int groupId);
}