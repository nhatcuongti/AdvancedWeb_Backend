package com.example.webadvanced_backend.repositories;

import com.example.webadvanced_backend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByPresentationGroupId(Integer presentationId);
}