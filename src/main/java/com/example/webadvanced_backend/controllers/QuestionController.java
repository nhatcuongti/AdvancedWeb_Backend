package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.models.*;
import com.example.webadvanced_backend.repositories.*;
import com.example.webadvanced_backend.requestentities.CreateQuestionRequest;
import com.example.webadvanced_backend.requestentities.DeletePresentationRequest;
import com.example.webadvanced_backend.requestentities.EditPresentationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{preId}")
    ResponseEntity<?> getQuestion(@PathVariable int preId){
        try {
            List<Question> listQuestion = questionRepository.findByPresentation(presentationRepository.findById(preId));
            return ResponseEntity.ok(listQuestion);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }
    @PostMapping(path = "/answer-question/{questionId}")
    public ResponseEntity<?> answerQuestion(@PathVariable int questionId){
        try {
            Optional<Question> optional = questionRepository.findById(questionId);
            Question question = optional.get();
            question.setIsAnswered(true);
            return ResponseEntity.ok(questionRepository.save(question));
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/create-question")
    public ResponseEntity<?> createQuestion(@RequestBody CreateQuestionRequest request) {
        try {
            Presentation presentation = presentationRepository.findById(request.getPreId());
            Question question = Question.builder()
                    .presentation(presentation)
                    .question(request.getQuestion())
                    .isAnswered(false)
                    .build();
            return ResponseEntity.ok(questionRepository.save(question));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
}
