package com.example.webadvanced_backend.controllers;

import com.example.webadvanced_backend.repositories.*;
import com.example.webadvanced_backend.requestentities.CreatePresentationRequest;
import com.example.webadvanced_backend.models.*;
import com.example.webadvanced_backend.requestentities.DeletePresentationRequest;
import com.example.webadvanced_backend.requestentities.EditPresentationRequest;
import com.example.webadvanced_backend.requestentities.EditSlideTitleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
@RequestMapping(path = "api/v1/presentation")
public class PresentationController {
    @Autowired
    PresentationRepository presentationRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SlideRepository slideRepository;
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

    @GetMapping()
    ResponseEntity<?> getPresentationList(Principal principal){
        try {
            Account currentUser = accountRepository.findByUsername(principal.getName());
            List<Presentation> listPresentation = presentationRepository.findByUser(currentUser);
            return ResponseEntity.ok(listPresentation);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }
    @PostMapping(path = "/add")
    public ResponseEntity<?> createAPresentation(@RequestBody CreatePresentationRequest request, Principal principal){
        try {
            Presentation presentation;
            presentation = Presentation.builder().name(request.getPresentationName())
                    .user(accountRepository.findByUsername(principal.getName()))
                    .createdTime( request.getCreatedTime())
                    .build();
            return ResponseEntity.ok(presentationRepository.save(presentation));
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }

    @PostMapping(path = "/edit/{id}")
    public ResponseEntity<?> editPresentationName(@RequestBody EditPresentationRequest request, @PathVariable String id) {
        try {
            Optional<Presentation> presentation = presentationRepository.findById(Integer.valueOf(id));
            presentation.get().setName(request.getPresentationName());
            presentation.get().setModifiedTime(request.getEditTime());
            return ResponseEntity.ok(presentationRepository.save(presentation.get()));
        } catch (Exception err) {
            return ResponseEntity.internalServerError().body(err.getMessage());
        }

    }


    @PostMapping(path = "/delete")
    public ResponseEntity<?> deletePresentation(@RequestBody DeletePresentationRequest request, Principal principal){
        try {
            Presentation presentation = presentationRepository.findById(request.getPreId());
            // tim list slide
            List<Slide> listSlide = slideRepository.findByPresentation(presentation);
            // tim cac content id cua cac slide
            List<Integer> listContentId = new ArrayList<>();
            for (Slide s: listSlide) {
                   listContentId.add(s.getContent().getId());
            }
            //tim cac list content
            List<Content> listContent = contentRepository.findAllById(listContentId);
            //xoa content multichoice
            List<ContentMultichoice> listMultichoice = new ArrayList<>();
            List<ContentParagraph> listParagraph = new ArrayList<>();
            List<ContentHeading> listHeading = new ArrayList<>();
            for(Content c: listContent ){
                if(c.getSlideType() == 1)
                    listMultichoice.addAll(multichoiceRepository.findByContent(c));
                else if(c.getSlideType() == 2)
                    listParagraph.addAll(paragraphRepository.findByContent(c));
                else if(c.getSlideType() == 3)
                    listHeading.addAll(headingRepository.findByContent(c));
            }
            if(!listMultichoice.isEmpty()){
                //delete votes of each option
                List<Vote> listVote = new ArrayList<>();
                for(ContentMultichoice m : listMultichoice){
                    listVote.addAll(voteRepository.findByOption(m.getOption()));
                }
                voteRepository.deleteAll(listVote);
                multichoiceRepository.deleteAll(listMultichoice);

            }
            if(!listParagraph.isEmpty())
                headingRepository.deleteAll(listHeading);
            if(!listParagraph.isEmpty())
                paragraphRepository.deleteAll(listParagraph);
            // xoa cac slide
            slideRepository.deleteAll(listSlide);
            //xoa question
            List<Question> listQuestion= questionRepository.findByPresentation(presentation);
            questionRepository.deleteAll(listQuestion);
            //
            presentationRepository.delete(presentation);
            return ResponseEntity.ok(presentation);
        }
        catch (Exception err){
            return ResponseEntity.internalServerError().body(err.getMessage());
        }
    }
}
