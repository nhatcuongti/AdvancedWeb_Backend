package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.*;
import com.example.webadvanced_backend.repositories.*;
import com.example.webadvanced_backend.requestentities.CreateQuestionRequest;
import com.example.webadvanced_backend.requestentities.DeletePresentationRequest;
import com.example.webadvanced_backend.requestentities.EditPresentationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
@RequestMapping(path = "api/v1/question")
public class QuestionController {
    @Autowired
    PresentationRepository presentationRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ContentMultichoiceRepository multichoiceRepository;
    @Autowired
    ContentHeadingRepository headingRepository;
    @Autowired
    ContentParagraphRepository paragraphRepository;
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    PresentationGroupRepository presentationGroupRepository;

    @GetMapping("/load-old-question/{preId}")
    ResponseEntity<?> getQuestion(@PathVariable int preId){
        try {
            List<Question> listQuestion = questionRepository.findByPresentationGroup(presentationGroupRepository.findById(preId));
            return ResponseEntity.ok(listQuestion);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }
    @PostMapping(path = "/answer-question/{preId}/{questionId}")
    public ResponseEntity<?> answerQuestion(@PathVariable int questionId, @PathVariable int preId){
        try {
            Optional<Question> optional = questionRepository.findById(questionId);
            Question question = optional.get();
            question.setIsAnswered(true);
            Question savedQuestion = questionRepository.save(question);
            simpMessagingTemplate.convertAndSend("/topic/question/" + preId, savedQuestion);
            return ResponseEntity.ok(questionRepository.save(question));
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
    @PostMapping(path = "/upvote-question/{preId}/{questionId}")
    public ResponseEntity<?> upVote(@PathVariable int questionId, @PathVariable int preId){
        try {
            Optional<Question> optional = questionRepository.findById(questionId);
            Question question = optional.get();
            question.setNumberVote(question.getNumberVote() + 1);
            Question savedQuestion = questionRepository.save(question);
            simpMessagingTemplate.convertAndSend("/topic/question/" + preId, savedQuestion);
            return ResponseEntity.ok(questionRepository.save(question));
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/create- /{preId}")
    public ResponseEntity<?> createQuestion(@RequestBody CreateQuestionRequest request, @PathVariable int preId) {
        try {
            PresentationGroup presentationGroup = presentationGroupRepository.findById(preId);
            Question question = Question.builder()
                    .presentationGroup(presentationGroup)
                    .question(request.getQuestion())
                    .isAnswered(false)
                    .numberVote(0)
                    .createdTime(request.getCreatedTime())
                    .build();
            Question savedQuestion = questionRepository.save(question);
            simpMessagingTemplate.convertAndSend("/topic/question/" + preId, savedQuestion);
            return ResponseEntity.ok(savedQuestion);
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
}
